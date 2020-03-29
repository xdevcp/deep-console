package cc.devcp.project.provider.model.app;

import cc.devcp.project.provider.utils.GroupKey2;

/**
 * config key util
 *
 * @author Nacos
 */
public class GroupKey extends GroupKey2 {

    private String dataId;
    private String group;

    public GroupKey(String dataId, String group) {
        this.dataId = dataId;
        this.group = group;
    }

    public GroupKey(String groupKeyString) {
        String[] groupKeys = parseKey(groupKeyString);
        this.dataId = groupKeys[0];
        this.group = groupKeys[1];
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return dataId + "+" + group;
    }

    public String getGroupkeyString() {
        return getKey(dataId, group);
    }

    //TODO : equal as we use Set

}
