package gaozhi.online.ubtb.core.util;

import java.nio.charset.StandardCharsets;

/**
 * @author lfc
 * @title: PDUAnalyse
 * @projectName ubtp-server
 * @description: TODO byte工具
 * @date 2021/8/29 20:09
 */
public final class ByteUtil {

    /**
     * @param data1:
     * @param data2:
     * @return byte[]
     * @description:(合并两个byte数组)
     * @author: gaozhi.online
     * @date: 2021/9/5 16:20
     * @throws:
     */
    public static byte[] byteMerge(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    /**
     * @param data1:
     * @param data2:
     * @param data3:
     * @return byte[]
     * @description:(合并三个byte数组)
     * @author: gaozhi.online
     * @date: 2021/9/5 16:20
     * @throws:
     */
    public static byte[] byteMerge(byte[] data1, byte[] data2, byte[] data3) {
        byte[] data4 = new byte[data1.length + data2.length + data3.length];
        System.arraycopy(data1, 0, data4, 0, data1.length);
        System.arraycopy(data2, 0, data4, data1.length, data2.length);
        System.arraycopy(data3, 0, data4, data2.length, data3.length);
        return data4;

    }

    /**
     * @param values:
     * @return byte[]
     * @description:(合并多个byte数组)
     * @author: gaozhi.online
     * @date: 2021/9/5 16:17
     * @throws:
     */
    public static byte[] byteMerge(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    /**
     * @param src:
     * @param start:  开始截断的位置
     * @param end:  结束截断的位置
     * @return byte[]
     * @description: (截断取出部分Byte subByte(src, 0, 2): [0,122,32,54]->[0,122,32])
     * @author: gaozhi.online
     * @date: 2021/9/5 19:54
     * @throws:
     */
    public static byte[] subByte(byte[] src, int start, int end) {
        byte[] res = new byte[end - start + 1];
        System.arraycopy(src, start, res, 0, res.length);
        return res;
    }

    /**
     * short到字节数组的转换.
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = Integer.valueOf(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        return b;
    }

    /**
     * 字节数组到short的转换.
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }


    /**
     * int到字节数组的转换.
     */
    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = Integer.valueOf(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        return b;
    }

    /**
     * 字节数组到int的转换.
     */
    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }

    //int 合并为 long
    public static long intToLong(int high, int low) {
        return byteToLong(byteMerge(intToByte(high), intToByte(low)));
    }

    //取long的高字节
    public static int longHigh(long param) {
        return byteToInt(subByte(longToByte(param), 0, 3));
    }

    //取long的低字节
    public static int longLow(long param) {
        return byteToInt(subByte(longToByte(param), 4, 7));
    }

    /**
     * long类型转成byte数组
     */
    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = Long.valueOf(temp & 0xff).byteValue();// 将最低位保存在最低位 temp = temp
            temp = temp >> 8;// 向右移8位
        }
        return b;
    }

    /**
     * 字节数组到long的转换.
     */
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * double到字节数组的转换.
     */
    public static byte[] doubleToByte(double num) {
        byte[] b = new byte[8];
        long l = Double.doubleToLongBits(num);
        for (int i = 0; i < 8; i++) {
            b[i] = Long.valueOf(l).byteValue();
            l = l >> 8;
        }
        return b;
    }

    /**
     * 字节数组到double的转换.
     */
    public static double getDouble(byte[] b) {
        long m;
        m = b[0];
        m &= 0xff;
        m |= ((long) b[1] << 8);
        m &= 0xffff;
        m |= ((long) b[2] << 16);
        m &= 0xffffff;
        m |= ((long) b[3] << 24);
        m &= 0xffffffffl;
        m |= ((long) b[4] << 32);
        m &= 0xffffffffffl;
        m |= ((long) b[5] << 40);
        m &= 0xffffffffffffl;
        m |= ((long) b[6] << 48);
        m &= 0xffffffffffffffl;
        m |= ((long) b[7] << 56);
        return Double.longBitsToDouble(m);
    }

    /**
     * char到字节数组的转换.
     */
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    /**
     * 字节数组到char的转换.
     */
    public static char byteToChar(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }

    /**
     * string到字节数组的转换.
     */
    public static byte[] stringToByte(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 字节数组到String的转换.
     */
    public static String bytesToString(byte[] str) {
        return new String(str, StandardCharsets.UTF_8);
    }
}
