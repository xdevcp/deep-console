package cc.devcp.project.provider.controller.parameters;

/**
 * @author klw(213539 @ qq.com)
 * @ClassName: SameNamespaceCloneConfigBean
 * @Description: 同namespace克隆接口的配制bean
 * @date 2019/12/13 16:10
 */
public class SameNamespaceCloneConfigBean {

    private Long cfgId;

    private String dataId;

    private String group;

    public Long getCfgId() {
        return cfgId;
    }

    public void setCfgId(Long cfgId) {
        this.cfgId = cfgId;
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
}
