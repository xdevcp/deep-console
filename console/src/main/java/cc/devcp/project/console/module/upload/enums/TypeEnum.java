package cc.devcp.project.console.module.upload.enums;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/8 15:17
 */
public enum TypeEnum {
    /**
     * TypeEnum
     */
    Opportunities("自定义线索", "opportunities-upload", "opportunities-download"),
    CustomerTag("客户标签", "customer-tag-upload", "customer-tag-download"),
    VehicleTag("车辆标签", "vehicle-tag-upload", "vehicle-tag-download"),
    VehicleAgent("关联信息", "vehicle-agent-upload", "vehicle-agent-download"),
    ;

    private String content;

    private String uploadPath;

    private String downloadPath;

    TypeEnum(String content, String uploadPath, String downloadPath) {
        this.content = content;
        this.uploadPath = uploadPath;
        this.downloadPath = downloadPath;
    }

    public String getContent() {
        return content;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }
}
