package cc.devcp.project.core.utils;

import cc.devcp.project.common.utils.IoUtils;
import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cc.devcp.project.core.utils.Constants.FUNCTION_MODE_PROPERTY_NAME;
import static org.apache.commons.lang3.CharEncoding.UTF_8;

/**
 * @author nacos
 */
public class SystemUtils {

    /**
     * Standalone mode or not
     * <p>
     * STANDALONE_MODE = Boolean.getBoolean(STANDALONE_MODE_PROPERTY_NAME);
     * </p>
     */
    public static final boolean STANDALONE_MODE = true;

    public static final String STANDALONE_MODE_ALONE = "standalone";
    public static final String STANDALONE_MODE_CLUSTER = "cluster";

    /**
     * server
     */
    public static final String FUNCTION_MODE = System.getProperty(FUNCTION_MODE_PROPERTY_NAME);

    public static final String FUNCTION_MODE_CONFIG = "config";
    public static final String FUNCTION_MODE_NAMING = "naming";


    private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
        .getOperatingSystemMXBean();

    /**
     * nacos local ip
     */
    public static final String LOCAL_IP = InetUtils.getSelfIp();

    /**
     * The home of spring.application.name.
     */
    public static final String APP_ROOT = "app";

    /**
     * The key of application home.
     */
    public static final String APP_HOME_KEY = "deep.home";

    /**
     * The home of application.
     */
    public static final String APP_HOME = getAppHome();

    /**
     * The file path of cluster conf.
     */
    public static final String CLUSTER_CONF_FILE_PATH = getClusterConfFilePath();

    public static List<String> getIPsBySystemEnv(String key) {
        String env = getSystemEnv(key);
        List<String> ips = new ArrayList<>();
        if (StringUtils.isNotEmpty(env)) {
            ips = Arrays.asList(env.split(","));
        }
        return ips;
    }

    public static String getSystemEnv(String key) {
        return System.getenv(key);
    }

    public static float getLoad() {
        return (float) operatingSystemMXBean.getSystemLoadAverage();
    }

    public static float getCPU() {
        return (float) operatingSystemMXBean.getSystemCpuLoad();
    }

    public static float getMem() {
        return (float) (1 - (double) operatingSystemMXBean.getFreePhysicalMemorySize() / (double) operatingSystemMXBean
            .getTotalPhysicalMemorySize());
    }

    private static String getAppHome() {
        String appHome = System.getProperty(APP_HOME_KEY);
        if (StringUtils.isBlank(appHome)) {
            appHome = System.getProperty("user.home") + File.separator + "." + APP_ROOT;
        }
        return appHome;
    }

    public static String getConfFilePath() {
        return APP_HOME + File.separator + "conf" + File.separator;
    }

    private static String getClusterConfFilePath() {
        return APP_HOME + File.separator + "conf" + File.separator + "cluster.conf";
    }

    public static List<String> readClusterConf() throws IOException {
        List<String> instanceList = new ArrayList<String>();
        try (Reader reader = new InputStreamReader(new FileInputStream(new File(CLUSTER_CONF_FILE_PATH)),
            StandardCharsets.UTF_8)) {
            List<String> lines = IoUtils.readLines(reader);
            String comment = "#";
            for (String line : lines) {
                String instance = line.trim();
                if (instance.startsWith(comment)) {
                    // # it is ip
                    continue;
                }
                if (instance.contains(comment)) {
                    // 192.168.71.52:8848 # Instance A
                    instance = instance.substring(0, instance.indexOf(comment));
                    instance = instance.trim();
                }
                int multiIndex = instance.indexOf(Constants.COMMA_DIVISION);
                if (multiIndex > 0) {
                    // support the format: ip1:port,ip2:port  # multi inline
                    instanceList.addAll(Arrays.asList(instance.split(Constants.COMMA_DIVISION)));
                } else {
                    //support the format: 192.168.71.52:8848
                    instanceList.add(instance);
                }
            }
            return instanceList;
        }
    }

    public static void writeClusterConf(String content) throws IOException {
        IoUtils.writeStringToFile(new File(CLUSTER_CONF_FILE_PATH), content, UTF_8);
    }

}
