package cc.devcp.project.provider.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Time util
 *
 * @author Nacos
 */
public class TimeUtils {

    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

     public static String getCurrentTimeStr() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return format.format(c.getTime());
    }
}
