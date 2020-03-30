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
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer integer, Boolean isHead) {
        if (isHead) {
            return;
        }
        int j = row.getLastCellNum() - 1;
        Cell cell = row.getCell(j);
        /*
         * 修改说明：edit by wupan on 2020.03.17
         * POI操作Excel中，导出的数据不是很大时，则不会有问题，而数据很多或者比较多时，就会报以下的错误，
         * java.lang.IllegalStateException: The maximum number of Cell Styles was exceeded. You can define up to 64000 style in a .xlsx Workbook
         * 是由于cell styles太多create造成，故一般可以把CellStyle设置放到循环外面
         * */
        CellStyle cellStyle = writeSheetHolder.getParentWriteWorkbookHolder().getWorkbook().createCellStyle();
        boolean isHighLight = StringUtils.isNotEmpty(cell.getStringCellValue());
        for (int i = 0; i <= j; i++) {
            Cell item = row.getCell(i);
            buildStyle(cellStyle, isHighLight);
            item.setCellStyle(cellStyle);
        }
        row.setHeightInPoints(17.5f);
    }

    public void buildStyle(CellStyle cellStyle, boolean isHighLight) {
        // 下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 水平对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直对齐方式
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        if (isHighLight) {
            //红色背景
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        }
    }
}
