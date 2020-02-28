package cc.devcp.project.common.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author xingxuechao
 * on:2019/2/27 12:32 PM
 */
public class VersionUtils {

    public static String VERSION;
    /**
     * 获取当前version
     */
    public static final String VERSION_DEFAULT = "${project.version}";


    static {
        InputStream in = null;
        try {
            in = VersionUtils.class.getClassLoader()
                .getResourceAsStream("project-version.txt");
            Properties props = new Properties();
            props.load(in);
            String val = props.getProperty("version");
            if (val != null && !VERSION_DEFAULT.equals(val)) {
                VERSION = val;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
