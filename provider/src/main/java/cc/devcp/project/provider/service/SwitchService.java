package cc.devcp.project.provider.service;

import cc.devcp.project.common.utils.IoUtils;

import cc.devcp.project.provider.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Switch
 *
 * @author Nacos
 */
@Service
public class SwitchService {
    public static final String SWITCH_META_DATAID = "cc.devcp.project.meta.switch";

    public static final String FIXED_POLLING = "isFixedPolling";
    public static final String FIXED_POLLING_INTERVAL = "fixedPollingInertval";

    public static final String FIXED_DELAY_TIME = "fixedDelayTime";

    public static final String DISABLE_APP_COLLECTOR = "disableAppCollector";

    private static volatile Map<String, String> switches = new HashMap<String, String>();

    public static boolean getSwitchBoolean(String key, boolean defaultValue) {
        boolean rtn = defaultValue;
        try {
            String value = switches.get(key);
            rtn = value != null ? Boolean.parseBoolean(value) : defaultValue;
        } catch (Exception e) {
            rtn = defaultValue;
            LogUtil.fatalLog.error("corrupt switch value {}={}", key, switches.get(key));
        }
        return rtn;
    }

    public static int getSwitchInteger(String key, int defaultValue) {
        int rtn = defaultValue;
        try {
            String status = switches.get(key);
            rtn = status != null ? Integer.parseInt(status) : defaultValue;
        } catch (Exception e) {
            rtn = defaultValue;
            LogUtil.fatalLog.error("corrupt switch value {}={}", key, switches.get(key));
        }
        return rtn;
    }

    public static String getSwitchString(String key, String defaultValue) {
        String value = switches.get(key);
        return StringUtils.isBlank(value) ? defaultValue : value;
    }

    public static void load(String config) {
        if (StringUtils.isBlank(config)) {
            LogUtil.fatalLog.error("switch config is blank.");
            return;
        }
        LogUtil.fatalLog.warn("[switch-config] {}", config);

        Map<String, String> map = new HashMap<String, String>(30);
        try {
            for (String line : IoUtils.readLines(new StringReader(config))) {
                if (!StringUtils.isBlank(line) && !line.startsWith("#")) {
                    String[] array = line.split("=");

                    if (array == null || array.length != 2) {
                        LogUtil.fatalLog.error("corrupt switch record {}", line);
                        continue;
                    }

                    String key = array[0].trim();
                    String value = array[1].trim();

                    map.put(key, value);
                }
                switches = map;
                LogUtil.fatalLog.warn("[reload-switches] {}", getSwitches());
            }
        } catch (IOException e) {
            LogUtil.fatalLog.warn("[reload-switches] error! {}", config);
        }
    }

    public static String getSwitches() {
        StringBuilder sb = new StringBuilder();

        String split = "";
        for (Map.Entry<String, String> entry : switches.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(split);
            sb.append(key);
            sb.append("=");
            sb.append(value);
            split = "; ";
        }

        return sb.toString();
    }

}
