package gaozhi.online.ubtb.core.net;


import gaozhi.online.ubtb.core.util.ByteUtil;

import java.nio.ByteBuffer;

/**
 * @author lfc
 * @title: PDUUtil
 * @projectName ubtp-server
 * @description: TODO 协议数据单元 data部分最长只有MTU - HeaderLen个字节，区分发送数据与接收数据，发送数据不可以直接修改
 * @date 2021/9/5 16:31
 */
class PDU implements Cloneable {

    private static final byte[] DEFAULT_PACK = new byte[1];

    private long param1;
    private long param2;
    private long param3;
    private long param4;
    private long param5;
    /**
     * @description: ${todo 缓冲区}
     */
    private byte[] data;

    public PDU() {
        data = DEFAULT_PACK;
    }

    public long getParam1() {
        return param1;
    }

    public long getParam2() {
        return param2;
    }

    public long getParam3() {
        return param3;
    }

    public long getParam4() {
        return param4;
    }

    public long getParam5() {
        return param5;
    }

    public byte[] getData() {
        return data;
    }

    public void setParam1(long param1) {
        this.param1 = param1;
    }

    public void setParam2(long param2) {
        this.param2 = param2;
    }

    public void setParam3(long param3) {
        this.param3 = param3;
    }

    public void setParam4(long param4) {
        this.param4 = param4;
    }

    public void setParam5(long param5) {
        this.param5 = param5;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        PDU pdu = (PDU) super.clone();
        pdu.data = new byte[data.length];
        System.arraycopy(data, 0, pdu.data, 0, data.length);
        return pdu;
    }

    @Override
    public String toString() {
        return "PDU{" +
                "param1=" + param1 +
                ", param2=" + param2 +
                ", param3=" + param3 +
                ", param4=" + param4 +
                ", param5=" + param5 +
                ", dataLen=" + data.length +
                '}';
    }

    /**
     * @description:(PDU编码解码器)
     * @author: gaozhi.online
     * @date: 2021/9/5 20:03
     */
    public static final class Codec {
        /**
         * @description:(Internet上的标准MTU值为576，所以Internet的UDP编程时数据长度最好在576－20－8＝548字节以内。所以将长度固定为548字节)
         * @author: gaozhi.online
         * @date: 2021/9/5 16:32
         * @throws:
         */
        private final int MTU;
        /**
         * @description: ${todo 报头长度}
         */
        public static final int HeaderLen = 40 + 2;

        public Codec(int MTU) {
            this.MTU = MTU;
        }

        public ByteBuffer generateBuffer() {
            return ByteBuffer.wrap(new byte[MTU]);
        }

        /**
         * @return gaozhi.online.ubtp.core.net.net.PDU
         * @description:(将接收到的数据包解码为pdu)
         * @author: gaozhi.online
         * @date: 2021/9/5 19:59
         */
        public PDU decode(ByteBuffer buffer) {
            byte[] data = buffer.array();
            short len = ByteUtil.byteToShort(ByteUtil.subByte(data, 40, 41));
            if (len <= 0 || len > MTU - HeaderLen) {
                return null;
            }
            PDU pdu = new PDU();

            pdu.param1 = ByteUtil.byteToLong(ByteUtil.subByte(data, 0, 7));
            pdu.param2 = ByteUtil.byteToLong(ByteUtil.subByte(data, 8, 15));
            pdu.param3 = ByteUtil.byteToLong(ByteUtil.subByte(data, 16, 23));
            pdu.param4 = ByteUtil.byteToLong(ByteUtil.subByte(data, 24, 31));
            pdu.param5 = ByteUtil.byteToLong(ByteUtil.subByte(data, 32, 39));
            pdu.data = ByteUtil.subByte(data, HeaderLen, HeaderLen + len - 1);
            if (len != pdu.data.length) {
                return null;
            }
            return pdu;
        }

        /**
         * @return java.nio.ByteBuffer
         * @description:(将数据包装编码为ByteBuffer)
         * @author: gaozhi.online
         * @date: 2021/9/5 16:09
         */
        public ByteBuffer encode(PDU pdu) {
            if(pdu.data==null){
                throw new NullPointerException("pdu data is null");
            }
            if (pdu.data.length > MTU - HeaderLen) {
                throw new OutOfMemoryError("pdu length is longer than MTU-HeaderLen");
            }
            byte[] pack = ByteUtil.byteMerge(ByteUtil.longToByte(pdu.param1), ByteUtil.longToByte(pdu.param2), ByteUtil.longToByte(pdu.param3), ByteUtil.longToByte(pdu.param4), ByteUtil.longToByte(pdu.param5), ByteUtil.shortToByte((short) pdu.data.length), pdu.data);
            return ByteBuffer.wrap(pack);
        }
    }
}
