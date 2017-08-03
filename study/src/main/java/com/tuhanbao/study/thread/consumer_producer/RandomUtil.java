package com.tuhanbao.study.thread.consumer_producer;


import java.util.Random;

public final class RandomUtil
{
    private static final Random RANDOM = new Random();
    
    /**
     * 包含start, 和end
     * @param start
     * @param end
     * @return
     */
    public static int randomInt(int start, int end)
    {
        if (start == end) return start;
        int min = start > end ? end : start;
        int GAP = start + end - min - min + 1;
        return RANDOM.nextInt(GAP) + min;
    }
    

    /**
     * 包含start, 和end
     * @param start
     * @param end
     * @return
     */
    public static long randomLong(long start, long end)
    {
        if (start == end) return start;
        long min = start > end ? end : start;
        long GAP = start + end - min - min + 1;
        //nextLong可能是负数，导致出错
        return (Math.abs((RANDOM.nextLong())) % GAP) + min;
    }
    
    /**
     * 包含start, 和end
     * @param start
     * @param end
     * @return
     */
    public static double randomDouble(double start, double end, int decimal)
    {
        int decimalValue = (int) Math.pow(10, decimal);
        int startInt = (int) (start * decimalValue);
        int endInt = (int) (end * decimalValue);
        return randomInt(startInt, endInt) / 1d / decimalValue;
    }
}
