package cc.devcp.project.common.exception;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/11/15 11:02
 */
public class ExceptionUtil {

    public static String getSimpleMessage(Throwable e) {
        return null == e ? "" : e.getMessage();
    }

    public static String getMessage(Throwable e) {
        return null == e ? "" : String.format("[%s]: [%s]", new Object[]{e.getClass().getName(), e.getMessage()});
    }

    public static String getMessageWithStack(Throwable e) {
        return null == e ? "" : String.format("[%s]: [%s]: [%s]", new Object[]{e.getClass().getName(), e.getMessage(), getStackString(e, 100)});
    }

    public static String getStackString(Throwable e, int limit) {
        if (null == e) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = e.getStackTrace();
        int length = stackTrace.length;
        if (limit > 0 && limit < length) {
            length = limit;
        }
        for (int i = 0; i < length; i++) {
            sb.append("\n").append(stackTrace[i].toString());
        }
        return sb.toString();
    }

}
