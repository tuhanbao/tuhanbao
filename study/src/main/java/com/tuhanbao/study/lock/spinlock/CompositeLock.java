package com.tuhanbao.study.lock.spinlock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.Condition;

/**
 * 限时有界队列锁，并且直接不限数量的线程 由于是有界的队列，所以争用激烈，可以复合回退锁的概念，减少高争用 分为三步:
 * 第一步是取得一个State为FREE的节点，设置为WAITING 第二步是把这个节点加入队列，获取前一个节点 第三步是在前一个节点上自旋
 * 
 * 优点是L个锁的空间复杂度是O(L)，而限时无界队列锁的空间复杂度为O(Ln)
 * **/
public class CompositeLock implements TryLock {

	enum State {
		FREE, WAITING, RELEASED, ABORTED
	}

	class QNode {
		AtomicReference<State> state = new AtomicReference<CompositeLock.State>(
				State.FREE);
		volatile QNode preNode;
	}

	private final int SIZE = 10;

	private final int MIN_BACKOFF = 1;

	private final int MAX_BACKOFF = 10;

	private Random random = new Random();

	// 有界的QNode数组，表示队列总共可以使用的节点数
	private QNode[] waitings = new QNode[10];

	// 指向队尾节点，使用AtomicStampedReference带版本号的原子引用变量，可以防止ABA问题，因为这个算法实现需要对同一个Node多次进出队列
	private AtomicStampedReference<QNode> tail = new AtomicStampedReference<CompositeLock.QNode>(
			null, 0);

	// 每个线程维护一个QNode引用
	private ThreadLocal<QNode> myNode = new ThreadLocal<CompositeLock.QNode>() {
		public QNode initialValue() {
			return null;
		}
	};

	public CompositeLock() {
		for (int i = 0; i < SIZE; i++) {
			waitings[i] = new QNode();
		}
	}

	@Override
	public void lock() {
		Backoff backoff = new Backoff(MIN_BACKOFF, MAX_BACKOFF);
		QNode node = waitings[random.nextInt(SIZE)];

		// 第一步: 先获得数组里的一个Node，并把它的状态设置为WAITING，否则就自旋
		GETNODE: while (true) {
			while (node.state.get() != State.FREE) {
				// 因为释放锁时只是设置了State为RELEASED,由后继的线程来设置RELEASED为FREE
				// 如果该节点已经是队尾节点了并且是RELEASED，那么可以直接可以被使用
				// 获取当前原子引用变量的版本号
				int[] currentStamp = new int[1];
				QNode tailNode = tail.get(currentStamp);
				if (tailNode == node && tailNode.state.get() == State.RELEASED) {
					if (tail.compareAndSet(tailNode, null, currentStamp[0],
							currentStamp[0] + 1)) {
						node.state.set(State.WAITING);
						break GETNODE;
					}
				}
			}
			if (node.state.compareAndSet(State.FREE, State.WAITING)) {
				break;
			}
			try {
				backoff.backoff();
			} catch (InterruptedException e) {
				throw new RuntimeException(
						"Thread interrupted, stop to get the lock");
			}
		}
		// 第二步加入队列
		int[] currentStamp = new int[1];
		QNode preTailNode = null;
		do {
			preTailNode = tail.get(currentStamp);
		}
		// 如果没加入队列，就一直自旋
		while (!tail.compareAndSet(preTailNode, node, currentStamp[0],
				currentStamp[0] + 1));

		// 第三步在前一个节点自旋，如果前一个节点为null，证明是第一个加入队列的节点
		if (preTailNode != null) {
			// 在前一个节点的状态自旋
			while (preTailNode.state.get() != State.RELEASED) {
			}
			// 设置前一个节点的状态为FREE,可以被其他线程使用
			preTailNode.state.set(State.FREE);
		}

		// 将线程的myNode指向获得锁的node
		myNode.set(node);
		return;
	}

	@Override
	public void unlock() {
		QNode node = myNode.get();
		node.state.set(State.RELEASED);
		myNode.set(null);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		long startTime = System.currentTimeMillis();
		long duration = TimeUnit.MILLISECONDS.convert(time, unit);
		long expectedTime = startTime + duration;

		Backoff backoff = new Backoff(MIN_BACKOFF, MAX_BACKOFF);
		QNode node = waitings[random.nextInt(SIZE)];

		// 第一步: 先获得数组里的一个Node，并把它的状态设置为WAITING，否则就自旋
		GETNODE: while (true) {
			while (node.state.get() != State.FREE) {
				// 因为释放锁时只是设置了State为RELEASED,由后继的线程来设置RELEASED为FREE
				// 如果该节点已经是队尾节点了并且是RELEASED，那么可以直接可以被使用
				// 获取当前原子引用变量的版本号
				int[] currentStamp = new int[1];
				QNode tailNode = tail.get(currentStamp);
				if (tailNode == node
						&& (tailNode.state.get() == State.ABORTED || tailNode.state
								.get() == State.RELEASED)) {
					QNode preNode = null;
					// 如果最后一个节点是ABORTED状态，就把tail指向它的前一个节点
					if (tailNode.state.get() == State.ABORTED) {
						preNode = tailNode.preNode;
					}

					if (tail.compareAndSet(tailNode, preNode, currentStamp[0],
							currentStamp[0] + 1)) {
						node.state.set(State.WAITING);
						break GETNODE;
					}
				}
			}
			if (node.state.compareAndSet(State.FREE, State.WAITING)) {
				break;
			}
			try {
				backoff.backoff();
			} catch (InterruptedException e) {
				return false;
			}
			if (timeout(expectedTime, System.currentTimeMillis())) {
				return false;
			}
		}
		// 第二步加入队列
		int[] currentStamp = new int[1];
		QNode preTailNode = null;
		do {
			preTailNode = tail.get(currentStamp);
			// 如果未加入队列前超时，就设置node状态为FREE，给后续线程使用
			if (timeout(expectedTime, System.currentTimeMillis())) {
				node.state.set(State.FREE);
				return false;
			}
		}
		// 如果没加入队列，就一直自旋
		while (!tail.compareAndSet(preTailNode, node, currentStamp[0],
				currentStamp[0] + 1));

		// 第三步在前一个节点自旋，如果前一个节点为null，证明是第一个加入队列的节点
		if (preTailNode != null) {
			// 在前一个节点的状态自旋
			State s = preTailNode.state.get();
			while (s != State.RELEASED) {
				if (s == State.ABORTED) {
					QNode temp = preTailNode;
					preTailNode = preTailNode.preNode;
					// 可以释放该节点
					temp.state.set(State.FREE);
				}
				if (timeout(expectedTime, System.currentTimeMillis())) {
					node.preNode = preTailNode;
					node.state.set(State.ABORTED);
					return false;
				}
				s = preTailNode.state.get();
			}
			// 设置前一个节点的状态为FREE,可以被其他线程使用
			preTailNode.state.set(State.FREE);
		}

		// 将线程的myNode指向获得锁的node
		myNode.set(node);
		return true;
	}

	private boolean timeout(long expectedTime, long currentTimeMillis) {
		return expectedTime >= currentTimeMillis;
	}

}