/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.td.ca.base.util.snowflake;

import com.td.ca.base.util.Assert;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.base.util.objutil.TimeUtil;
import com.td.ca.base.util.other.IPUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

@Slf4j
public class SnowFlakeUtil {
    private static final int MAX_NUM = 2560000;
    protected int workerId;
    protected final int datacenterId;
    protected int sequence = 0;
    protected long lastTimestamp = -1L;

    protected int maxSeq = 4095;

    // 允许时钟往回拨10s
    protected int maxBackward = 10000;

    private static final SnowFlakeUtil INSTANCE = new SnowFlakeUtil();

    public SnowFlakeUtil() {
        this.datacenterId = this.getDatacenterId(31);
        this.workerId = this.getMaxWorkerId(this.datacenterId, 31);
    }

    public SnowFlakeUtil(int workerId, int datacenterId) {
        Assert.isFalse(workerId > 31L || workerId < 0L, String.format("worker Id can't be greater than %d or less than 0", 31L));
        Assert.isFalse(datacenterId > 31L || datacenterId < 0L, String.format("datacenter Id can't be greater than %d or less than 0", 31L));
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    protected int getMaxWorkerId(int datacenterId, int maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!StringUtil.isEmpty(name)) {
            mpid.append(name.split("@")[0]);
        }

        return (mpid.toString().hashCode() & '\uffff') % (maxWorkerId + 1);
    }

    protected int getDatacenterId(int maxDatacenterId) {
        int id = 0;

        try {
            InetAddress inetAddress = IPUtil.getLocalHostExactAddress();
            if (inetAddress == null) {
                inetAddress = InetAddress.getLocalHost();
            }

            NetworkInterface network = NetworkInterface.getByInetAddress(inetAddress);
            if (null == network) {
                id = 1;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = (255 & mac[mac.length - 2] | 65280 & mac[mac.length - 1] << 8) >> 6;
                    id %= maxDatacenterId + 1;
                }
            }
        } catch (Exception var7) {
            log.warn(" getDatacenterId: " + var7.getMessage());
        }

        return id;
    }

    public synchronized long generatorId() {
        long timestamp = this.timeGen();

        // 两种情况，1. 批量获取ID把所有的ID提前预支了，2. 改了系统时间
        if (timestamp < this.lastTimestamp) {
            long offset = this.lastTimestamp - timestamp;
            if (offset > maxBackward) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
            }

            if (this.sequence == maxSeq) {
                timestamp = this.lastTimestamp + 1;
            } else {
                timestamp = this.lastTimestamp;
            }
        }

        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & maxSeq;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }

        this.lastTimestamp = timestamp;
        return createId(timestamp);
    }

    protected long createId(long timestamp) {
        return timestamp - 1700000074657L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
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

    public static long nextId() {
        return INSTANCE.generatorId();
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for (timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    protected long timeGen() {
        return TimeUtil.now();
    }
}
