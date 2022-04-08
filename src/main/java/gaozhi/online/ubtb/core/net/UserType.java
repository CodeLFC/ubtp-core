package gaozhi.online.ubtb.core.net;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 用户类型
 * @date 2022/2/10 21:06
 */
public enum UserType {
    USubject,
    UClient,
    UServer,
    UCenter;

    /**
     * @description: TODO 用户类型
     * @author LiFucheng
     * @date 2022/1/2 13:31
     * @version 1.0
     */
    public static UserType getType(long id) {

        if (id >= 0 && id < Integer.MAX_VALUE) {
            return UServer;
        }

        if(id<0 && id > Integer.MIN_VALUE){
            return UCenter;
        }

        if (id < Integer.MIN_VALUE) {
            return USubject;
        }

        return UClient;
    }
}