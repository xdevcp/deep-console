package cc.devcp.project.provider.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip util
 *
 * @author Nacos
 */
@SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
public class IPUtil {

    public static boolean isIPV4(String addr) {
        if (null == addr) {
            return false;
        }
        String rexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();
        return ipAddress;
    }

    public static boolean isIPV6(String addr) {
        if (null == addr) {
            return false;
        }
        String rexp = "^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();
        return ipAddress;
    }
}
