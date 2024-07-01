package com.td.ca.base.util.snowflake;

import com.td.ca.base.util.Assert;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.log.LogUtil;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.base.util.objutil.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;

@Slf4j
/**
 * 15位的雪花算法，js如果long型过大会有精度问题。
 * 15位雪花算法依赖pod名称的精准hash，同样一个服务最多只支持63个pod
 * 而且为了避免时间回拨问题，15位雪花算法做了时间节约，并不会随着时间往前变得越来越大，而是按照启动时间挨个生成，可以节省大量ID。
 *
 * 15位的雪花算法强依赖不同pod的nodeId必须不同。
 */
public class SnowFlakeUtil15 extends SnowFlakeUtil {

    private static final int MAX_NUM = 256000;
    private static SnowFlakeUtil INSTANCE;

    private static int MAX_WORKER_ID = 63;

    static {
        INSTANCE = createSnowFlakeUtil();
    }

    public static SnowFlakeUtil createSnowFlakeUtil() {
        try {
            return new SnowFlakeUtil15();
        } catch (Exception e) {
            log.error(LogUtil.getMessage(e));
            return new SnowFlakeUtil();
        }
    }

    public SnowFlakeUtil15() {
        this(getNodeId());
    }

    public SnowFlakeUtil15(int workerId) {
        Assert.isFalse(workerId > MAX_WORKER_ID || workerId < 0L, String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        this.workerId = workerId;
        this.maxSeq = 7;
        this.lastTimestamp = TimeUtil.now();
    }

    public static int getNodeId() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        log.info("pod name : " + name);
        if (!StringUtil.isEmpty(name)) {
            int index = name.lastIndexOf("-");
            if (index != -1) {
                int workerId = Integer.valueOf(name.substring(index + 1));
                log.info("workerId : " + workerId);
                return workerId;
            }
        }
        throw new AppException("connot get workid from pod name.");
    }

    public synchronized long generatorId() {
        long timestamp = this.timeGen();
        // 两种情况，1. 批量获取ID把所有的ID提前预支了，2. 改了系统时间
        if (timestamp < this.lastTimestamp) {
            long offset = this.lastTimestamp - timestamp;
            if (offset > maxBackward) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
            }
        }

        this.sequence = (this.sequence + 1) & maxSeq;
        if (this.sequence == 0) {
            this.lastTimestamp++;
        }
        return createId(this.lastTimestamp);
    }

    public synchronized long[] generatorIdBatch(int num) {
        if (num > MAX_NUM) {
            throw new AppException("cannot generator so many id once");
        }

        int seqNum = maxSeq - sequence;
        int needMss = ((num - seqNum) / (maxSeq + 1));
        if ((num - seqNum) % (maxSeq + 1) > 0) {
            needMss += 1;
        }
        long timestamp = this.timeGen();
        // 两种情况，1. 批量获取ID把所有的ID提前预支了，2. 改了系统时间
        if (timestamp + maxBackward <= this.lastTimestamp + needMss) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id.");
        }

        long[] result = new long[num];
        int index = 0;
        sequence += 1;
        for (; sequence <= maxSeq; sequence++) {
            result[index++] = createId(this.lastTimestamp);
        }

        for (int i = 1; i < needMss; i++) {
            for (sequence = 0; sequence <= maxSeq; sequence++) {
                result[index++] = createId(this.lastTimestamp + i);
            }
        }
        for (sequence = 0; index < num; sequence++) {
            result[index++] = createId(this.lastTimestamp + needMss);
        }
        // 节约一个seq
        sequence -= 1;
        this.lastTimestamp = this.lastTimestamp + needMss;
        return result;
    }

    @Override
    protected long createId(long timestamp) {
        return timestamp - 1704529260489L << 9 | this.workerId << 3 | this.sequence;
    }

    public static long nextId() {
        return INSTANCE.generatorId();
    }
}
