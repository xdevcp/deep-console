package cc.devcp.project.config.server.service;

import cc.devcp.project.config.server.utils.event.EventDispatcher.Event;

import java.util.List;

/**
 * 本地数据发生变更的事件。
 *
 * @author Nacos
 */
public class LocalDataChangeEvent implements Event {
    final public String groupKey;
    final public boolean isBeta;
    final public List<String> betaIps;
    final public String tag;

    public LocalDataChangeEvent(String groupKey) {
        this.groupKey = groupKey;
        this.isBeta = false;
        this.betaIps = null;
        this.tag = null;
    }

    public LocalDataChangeEvent(String groupKey, boolean isBeta, List<String> betaIps) {
        this.groupKey = groupKey;
        this.isBeta = isBeta;
        this.betaIps = betaIps;
        this.tag = null;
    }

    public LocalDataChangeEvent(String groupKey, boolean isBeta, List<String> betaIps, String tag) {
        this.groupKey = groupKey;
        this.isBeta = isBeta;
        this.betaIps = betaIps;
        this.tag = tag;
    }
}
