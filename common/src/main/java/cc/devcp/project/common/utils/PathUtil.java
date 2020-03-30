package cc.devcp.project.common.utils;

import java.io.File;

/**
 * @author deep
 */
public class PathUtil {

    public static final String JAR = ".jar";

    public static String instance() {
        // 开发环境下
        String path = System.getProperty("user.dir");
        String classPath = System.getProperty("java.class.path");
        if (classPath.indexOf(File.pathSeparator) < 0) {
            File f = new File(classPath);
            classPath = f.getAbsolutePath();
            if (classPath.endsWith(JAR)) {
                // 使用环境下
                path = classPath.substring(0, classPath.lastIndexOf(File.separator));
            }
        }
        return path;
    }
}
