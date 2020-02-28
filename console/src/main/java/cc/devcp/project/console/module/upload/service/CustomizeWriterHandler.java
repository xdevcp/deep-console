package cc.devcp.project.console.module.upload.service;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/14 15:13
 */
public class CustomizeWriterHandler implements RowWriteHandler {


    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer integer, Boolean aBoolean) {
        int j = row.getLastCellNum() - 1;
        Cell cell = row.getCell(j);
        if("ErrorMsg".equals(cell.getStringCellValue())){
            return;
        }
        if(StringUtils.isEmpty(cell.getStringCellValue())){
            return;
        }
        for(int i=0; i<=j; i++){
            Cell item = row.getCell(i);
            CellStyle style = writeSheetHolder.getParentWriteWorkbookHolder().getWorkbook().createCellStyle();
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
            item.setCellStyle(style);
        }
    }
}
