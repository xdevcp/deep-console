package cc.devcp.project.common.utils;

/**
 * @author deep.wu
 * @version 1.0 on 2020/1/10 14:37.
 */
public class DebugUtil {

    public static final String OPTION_1 = "false";
    public static final String OPTION_2 = "0";

    /**
     * debug mode enabled
     *
     * @param debugFlag
     * @return
     */
    public static boolean enabled(String debugFlag) {
        if (debugFlag != null
                && !OPTION_1.equals(debugFlag)
                && !OPTION_2.equals(debugFlag)
                && !"".equals(debugFlag)) {
            return true;
        }
        return false;
    }

    /**
     * debug mode disabled
     *
     * @param debugFlag
     * @return
     */
    public static boolean disabled(String debugFlag) {
        return !enabled(debugFlag);
    }
}
