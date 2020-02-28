package cc.devcp.project.common.utils;

import java.util.UUID;

/**
 * @author nkorange
 */
public class UuidUtils {

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
