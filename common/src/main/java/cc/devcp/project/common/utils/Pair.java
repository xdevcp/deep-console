package cc.devcp.project.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author deep
 */
public class Pair {
    private String key;
    private String value;

    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Pair() {
        this(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
