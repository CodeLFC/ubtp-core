package gaozhi.online.ubtb.core.net;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 消息通信类型
 * @date 2022/2/10 21:12
 */
public enum UCommunicationType {
    C2C,
    C2SUBS,
    S2S,
    C2S,
    S2C,
    Center2S,
    S2Center;
    /**
     * @description: TODO 获取通信的类型
     * @author LiFucheng
     * @date 2022/2/10 21:45
     * @version 1.0
     */
    public static UCommunicationType getType(long fromId, long toId) {
        UserType fromType = UserType.getType(fromId);
        UserType toType = UserType.getType(toId);
        if (fromType == UserType.UClient && toType == UserType.UClient) {
            return C2C;
        }
        if (fromType == UserType.UClient && toType == UserType.USubject) {
            return C2SUBS;
        }
        if (fromType == UserType.UServer && toType == UserType.UServer) {
            return S2S;
        }
        if (fromType == UserType.UClient && toType == UserType.UServer) {
            return C2S;
        }
        if (fromType == UserType.UServer && toType == UserType.UClient) {
            return S2C;
        }
        if(fromType == UserType.UCenter && toType == UserType.UServer){
            return Center2S;
        }
        if(fromType == UserType.UServer && toType == UserType.UCenter){
            return S2Center;
        }
        return null;
    }
}