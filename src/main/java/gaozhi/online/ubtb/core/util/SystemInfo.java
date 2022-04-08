package gaozhi.online.ubtb.core.util;
/**
 * @author lfc
 * @title: SystemInfoService
 * @projectName ubtp-server
 * @description: TODO
 * @date 2021/9/11 15:06
 */
public class SystemInfo {

    private SystemInfo(){

    }
    /**
      * @description:(获取CPU核心数)
      * @author: gaozhi.online
      * @date: 2021/9/11 19:56
      * @throws:
     * @return int
      */
    public static int getCPUCoreNum() {
        return Runtime.getRuntime().availableProcessors();
    }
}
