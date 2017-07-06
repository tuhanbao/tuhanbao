package com.tuhanbao.base.util.objutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class RandomUtil {
    private static final Random RANDOM = new Random();

    /**
     * 包含start, 和end
     * 
     * @param start
     * @param end
     * @return
     */
    public static int randomInt(int start, int end) {
        if (start == end) return start;
        int min = start > end ? end : start;
        int GAP = start + end - min - min + 1;
        return RANDOM.nextInt(GAP) + min;
    }

    /**
     * 随机0到value的值,
     * @param value
     * @return  包含0，也包含value
     */
    public static int randomInt(int value) {
        //不考虑int越界
        return RANDOM.nextInt(value + 1);
    }

    /**
     * 包含start, 和end
     * 
     * @param start
     * @param end
     * @return
     */
    public static long randomLong(long start, long end) {
        if (start == end) return start;
        long min = start > end ? end : start;
        long GAP = start + end - min - min + 1;
        // nextLong可能是负数，导致出错
        return (Math.abs((RANDOM.nextLong())) % GAP) + min;
    }

    /**
     * 包含start, 和end
     * 
     * @param start
     * @param end
     * @return
     */
    public static double randomDouble(double start, double end, int decimal) {
        int decimalValue = (int)Math.pow(10, decimal);
        int startInt = (int)(start * decimalValue);
        int endInt = (int)(end * decimalValue);
        return randomInt(startInt, endInt) / 1d / decimalValue;
    }

    /**
     * 传入一个数组，数组每个元素代表一个基数。 返回最后随机到的基数序列。
     */
    public static int randomIndex(int... radixs) {
        // 如果radixs长度为0，返回-1
        if (radixs == null || radixs.length == 0) return -1;
        // 如果radixs长度为1，返回0
        if (radixs.length == 1) return 0;

        int sum = 0;
        for (int radix : radixs) {
            if (radix > 0) {
                sum += radix;
            }
        }

        // 所有人权值为0， 这种情况随机返回一个
        if (sum == 0) return randomInt(0, radixs.length - 1);

        int result = randomInt(1, sum);
        int index = 0;
        for (int radix : radixs) {
            if (result > radix) {
                result -= radix;
                index++;
            }
            else {
                break;
            }
        }

        return index;
    }

    /**
     * 通过百分比随机 返回随机到的次数
     * 
     * @param d
     * @return
     */
    public static int randomPercent(double d) {
        // 如果d > 1，则必中一次，
        int n = 0;
        while (d >= 1) {
            d -= 1;
            n++;
        }
        if (d == 0) return n;

        int value = NumberUtil.percent2Int(d);
        int randomValue = randomInt(1, NumberUtil.PERCENT_EXPAND_VALUE);
        if (randomValue <= value) {
            n++;
        }
        return n;
    }

    /**
     * 将n个对象的位置顺序进行随机打乱 如n=3,则将0,1,2随机打乱返回
     * 
     * @param n
     * @return
     */
    public static int[] disruptOrder(int n) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }

        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = list.remove(randomInt(0, n - 1 - i));
        }
        return order;
    }

    public static String randomLetterAndNumberString(int count) {
        return randomString(count, true, true);
    }

    public static String randomString(int count) {
        return randomString(count, false, false);
    }

    public static String randomString(int count, boolean letters, boolean numbers) {
        if (count == 0) {
            return "";
        }
        else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }

        int end = 'z' + 1;
        int start = ' ';
        if (!letters && !numbers) {
            start = 0;
            end = Integer.MAX_VALUE - 1;
        }

        char[] buffer = new char[count];

        while (count-- != 0) {
            char ch = (char)(randomInt(start, end));
            if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
                if (ch >= 56320 && ch <= 57343) {
                    if (count == 0) {
                        count++;
                    }
                    else {
                        buffer[count] = ch;
                        count--;
                        buffer[count] = (char)(55296 + randomInt(0, 127));
                    }
                }
                else if (ch >= 55296 && ch <= 56191) {
                    if (count == 0) {
                        count++;
                    }
                    else {
                        buffer[count] = (char)(56320 + randomInt(0, 127));
                        count--;
                        buffer[count] = ch;
                    }
                }
                else if (ch >= 56192 && ch <= 56319) {
                    count++;
                }
                else {
                    buffer[count] = ch;
                }
            }
            else {
                count++;
            }
        }
        return new String(buffer);
    }
}
