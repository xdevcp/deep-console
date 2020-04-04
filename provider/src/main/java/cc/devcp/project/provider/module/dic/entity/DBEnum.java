package cc.devcp.project.provider.module.dic.entity;


import cc.devcp.project.common.enums.CommEnum;

/**
 * @author deep.wu
 * @version 1.0 on 2020/3/14
 */
public enum DBEnum implements CommEnum<Integer> {
    /**
     * 保存在数据库的值,对应的文本描述
     */
    VALID(0, "有效的"),
    INVALID(1, "无效的");

    private Integer code;
    private String content;

    DBEnum(Integer code, String content) {
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
