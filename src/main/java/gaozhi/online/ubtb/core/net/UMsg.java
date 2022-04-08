package gaozhi.online.ubtb.core.net;



import gaozhi.online.ubtb.core.util.ByteUtil;

import java.util.Arrays;

/**
 * PDU消息协议单元
 */
public class UMsg {

    private long fromId;
    private long toId;
    private int routeCount;
    private int msgType;
    private long param4;
    private long param5;

    private byte[] data;

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public int getRouteCount() {
        return routeCount;
    }

    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getParam4() {
        return param4;
    }

    public void setParam4(long param4) {
        this.param4 = param4;
    }

    public long getParam5() {
        return param5;
    }

    public void setParam5(long param5) {
        this.param5 = param5;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UMsg{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", routeCount=" + routeCount +
                ", msgType=" + msgType +
                ", param4=" + param4 +
                ", param5=" + param5 +
                ", data=" + Arrays.toString(data) +
                '}';
    }


    /**
     * msg编解码器
     */
    public static final class Codec {
        public static PDU encode(UMsg msg) {
            PDU pdu = new PDU();
            pdu.setParam1(msg.fromId);
            pdu.setParam2(msg.toId);
            pdu.setParam3(ByteUtil.intToLong(msg.routeCount, msg.msgType));
            pdu.setParam4(msg.param4);
            pdu.setParam5(msg.param5);
            if (msg.data == null) {//防止空指针
                msg.data = new byte[]{0};
            }
            pdu.setData(msg.data);
            return pdu;
        }

        public static UMsg decode(PDU pdu) {
            UMsg msg = new UMsg();
            msg.fromId = pdu.getParam1();
            msg.toId = pdu.getParam2();
            msg.routeCount = ByteUtil.longHigh(pdu.getParam3());
            msg.msgType = ByteUtil.longLow(pdu.getParam3());
            msg.param4 = pdu.getParam4();
            msg.param5 = pdu.getParam5();
            msg.data = pdu.getData();
            return msg;
        }
    }
}
