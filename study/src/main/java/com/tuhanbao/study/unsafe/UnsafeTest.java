package com.tuhanbao.study.unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import com.tuhanbao.study.ITest;

import sun.misc.Unsafe;

public class UnsafeTest implements ITest {
//	private static final Unsafe UNSAFE = Unsafe.getUnsafe();

	@Override
	public Object test() {
//		Child child = new Child("child");
//		UNSAFE.addressSize();
//		// instance.arrayIndexScale(arrayClass)
//		System.out.println(instance);
		testAllocateInstance();
		return null;
	}
	
	private void testAllocateInstance() {
		try {
			UnsafeBean bean1 = new UnsafeBean();//.class.newInstance();
		} catch (Throwable e) {
			e.printStackTrace();
		}
//		try {
//			UnsafeBean bean2 = (UnsafeBean) UNSAFE.allocateInstance(UnsafeBean.class); // unsafe
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static long sizeOf(Object o) {
		Unsafe u = Unsafe.getUnsafe();
		HashSet<Field> fields = new HashSet<Field>();
		Class<?> c = o.getClass();
		while (c != Object.class) {
			for (Field f : c.getDeclaredFields()) {
				if ((f.getModifiers() & Modifier.STATIC) == 0) {
					fields.add(f);
				}
			}
			c = c.getSuperclass();
		}

		// get offset
		long maxSize = 0;
		for (Field f : fields) {
			long offset = u.objectFieldOffset(f);
			if (offset > maxSize) {
				maxSize = offset;
			}
		}

		return ((maxSize / 8) + 1) * 8; // padding
	}

}
