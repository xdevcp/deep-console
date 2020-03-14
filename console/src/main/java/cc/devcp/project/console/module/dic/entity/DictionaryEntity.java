package cc.devcp.project.console.module.dic.entity;

import cc.devcp.project.common.validator.Create;
import cc.devcp.project.common.validator.Modify;
import cc.devcp.project.common.validator.Query;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 数据字典实体
 * @Author DING DONG
 * @Date 2019/12/13 10:57
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("data_dictionary")
public class DictionaryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "DIC_ID", type= IdType.AUTO)
    private Integer id;

    @TableField(value = "DIC_VALUE_ID")
    private Integer valueId;

    @TableField(value = "DIC_PARENT_ID")
    @NotNull(groups = {Query.class, Create.class, Modify.class}, message = "父ID不能为空")
    private Integer parentId;

    @TableField(value = "DIC_DATA_TYPE")
    @NotBlank(groups = {Create.class}, message = "数据类型不能为空")
    private String dataType;

    @TableField(value = "DIC_DATA_CODE")
    private String dataCode;

    @TableField(value = "DIC_DATA_VALUE")
    private String dataValue;

    @TableField(value = "DIC_SORT_NO")
    @Max(value = 9999)
    private Integer sortNo;

    @TableField(value = "DIC_STATUS")
    private Integer status;

    @TableField(value = "DIC_DATA_DESC")
    private String dataDesc;

    @TableField(value = "DIC_UPDATE_TIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}



