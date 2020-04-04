package cc.devcp.project.upload.module.excel.data;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/3 14:10
 */
@Data
public class CustomerTagData {

    @ExcelProperty(value = "Cellphone", index = 0)
    private String cellphone;

    @ExcelProperty(value = "CustomerTag", index = 1)
    private String customerTag;

    @ExcelProperty(value = "ErrorMsg", index = 2)
    private String errorMsg;

}
