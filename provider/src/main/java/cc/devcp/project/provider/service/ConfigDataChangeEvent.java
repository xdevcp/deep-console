package cc.devcp.project.provider.service;

import cc.devcp.project.provider.utils.event.EventDispatcher;
import org.apache.commons.lang3.StringUtils;

/**
 * 指数据发布事件。
 *
 * @author Nacos
 */
public class ConfigDataChangeEvent implements EventDispatcher.Event {

    final public boolean isBeta;
    final public String dataId;
    final public String group;
    final public String tenant;
    final public String tag;
    final public long lastModifiedTs;

    public ConfigDataChangeEvent(String dataId, String group, long gmtModified) {
        this(false, dataId, group, gmtModified);
    }

    public ConfigDataChangeEvent(boolean isBeta, String dataId, String group, String tenant, long gmtModified) {
        if (null == dataId || null == group) {
            throw new IllegalArgumentException();
        }
        this.isBeta = isBeta;
        this.dataId = dataId;
        this.group = group;
        this.tenant = tenant;
        this.tag = null;
        this.lastModifiedTs = gmtModified;
    }

    public ConfigDataChangeEvent(boolean isBeta, String dataId, String group, long gmtModified) {
        this(isBeta, dataId, group, StringUtils.EMPTY, gmtModified);
    }

    public ConfigDataChangeEvent(boolean isBeta, String dataId, String group, String tenant, String tag,
                                 long gmtModified) {
        if (null == dataId || null == group) {
            throw new IllegalArgumentException();
        }
        this.isBeta = isBeta;
        this.dataId = dataId;
        this.group = group;
        this.tenant = tenant;
        this.tag = tag;
        this.lastModifiedTs = gmtModified;
    }

}
