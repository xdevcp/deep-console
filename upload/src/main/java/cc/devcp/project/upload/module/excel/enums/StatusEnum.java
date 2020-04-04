package cc.devcp.project.upload.module.excel.enums;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/8 15:24
 */
public enum StatusEnum {
    /**
     * StatusEnum
     */
    Started("已创建任务"),
    Processing("导入中"),
    Finished("已完成"),
    ;

    private String content;

    StatusEnum(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
