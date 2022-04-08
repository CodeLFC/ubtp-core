package gaozhi.online.ubtb.core.net;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 消息类型    相同消息类别的type不可以重复，不同消息类别的type可以重复
 * @date 2022/1/2 13:11
 */
public enum UMsgType {
    //center service
    S2Center_BEAT_REQUEST(0,"服务器向注册中心注册的心跳包"),
    Center2S_BEAT_RESPONSE(1,"注册中心向服务器返回的心跳响应"),

    //server service
    C2S__BEAT_REQUEST(0,"客户端向服务器发送的心跳包"),
    S2C__BEAT_RESPONSE(1,"服务器向客户端返回的心跳响应"),

    //C2C
    C2C__USER_NOT_ONLINE(0,"用户不在线消息");

    private final int type;
    private final String description;

    UMsgType(int type,String description){
        this.type =type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "UMsgType{" +
                "type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
