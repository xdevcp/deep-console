package cc.devcp.project.core.utils;

import org.apache.commons.lang3.StringUtils;


/**
 * Common methods for exception
 *
 * @author nkorange
 * @since 1.2.0
 */
public class ExceptionUtil {

    public static String getAllExceptionMsg(Throwable e) {
        Throwable cause = e;
        StringBuilder strBuilder = new StringBuilder();

        while (cause != null && !StringUtils.isEmpty(cause.getMessage())) {
            strBuilder.append("caused: ").append(cause.getMessage()).append(";");
            cause = cause.getCause();
        }

        return strBuilder.toString();
    }
}
