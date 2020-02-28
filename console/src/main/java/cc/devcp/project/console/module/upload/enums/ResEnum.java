package cc.devcp.project.console.module.upload.enums;

import cc.devcp.project.common.enums.CommEnum;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/9 15:55
 */
public enum ResEnum implements CommEnum<Integer> {

    EXCEL_TYPE_ERROR(40001, "非法的文件名称"),
    EXCEL_CONTENT_ERROR(40002, "文件内容为空"),
    ACCOUNT_SERVER_ERROR(50000, "用户服务不可用"),
    ;


    private Integer code;

    private String content;

    ResEnum(Integer code, String content){
        this.code = code;
        this.content = content;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getContent() {
        return content;
    }
}
