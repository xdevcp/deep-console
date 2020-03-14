package cc.devcp.project.console.module.dic.entity;

import cc.devcp.project.common.enums.CommEnum;

/**
 * @author deep.wu
 * @version 1.0 on 2020/3/14
 */
public enum DicEnum implements CommEnum<Integer> {
    /**
     * 响应的code值，响应的文本
     */
    DIC_CREATE_ERROR(42001, "数据字典创建失败"),
    DIC_MODIFY_ERROR(42002, "数据字典修改失败"),
    DIC_REMOVE_ERROR(42003, "数据字典删除失败"),
    DIC_TYPE_CANNOT_BE_NULL(42004, "字典类型不能为空"),
    DIC_CODE_CANNOT_BE_NULL(42005, "数据编码不能为空"),
    DIC_PARENT_NOT_EXITS(42006, "父级ID不存在,数据有误"),
    DIC_TYPE_NOT_EXITS(42007, "字典类型不存在"),
    DIC_TYPE_EXITS(42008, "字典类型已存在"),
    DIC_CODE_EXITS(42009, "数据编码已存在"),
    DIC_VALUE_EXITS(42010, "数据名称已存在");

    private Integer code;
    private String content;

    DicEnum(Integer code, String content) {
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
