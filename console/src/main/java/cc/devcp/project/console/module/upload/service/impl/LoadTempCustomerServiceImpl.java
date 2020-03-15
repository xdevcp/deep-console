package cc.devcp.project.console.module.upload.service.impl;

import cc.devcp.project.common.enums.ResultCodeEnum;
import cc.devcp.project.common.exception.CommRuntimeException;
import cc.devcp.project.console.module.upload.entity.LoadDataBatchEntity;
import cc.devcp.project.common.module.entity.LoadTempCustomerEntity;
import cc.devcp.project.common.module.mapper.LoadTempCustomerMapper;
import cc.devcp.project.console.module.upload.data.CustomerTagData;
import cc.devcp.project.console.module.upload.enums.StatusEnum;
import cc.devcp.project.console.module.upload.enums.TypeEnum;
import cc.devcp.project.console.module.upload.service.CustomizeWriterHandler;
import cc.devcp.project.console.module.upload.utils.LoadBatchTemp;
import cc.devcp.project.console.module.upload.utils.UploadProps;
import cc.devcp.project.console.module.upload.utils.UploadUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/3 14:19
 */
@Slf4j
@Service
public class LoadTempCustomerServiceImpl extends ServiceImpl<LoadTempCustomerMapper, LoadTempCustomerEntity> {

    @Autowired
    UploadProps uploadProps;

    @Autowired
    LoadBatchServiceImpl loadDataBatchService;

    public boolean createBatch(List<LoadTempCustomerEntity> list) {
        return this.saveBatch(list);
    }

    public PageInfo<LoadTempCustomerEntity> page(int pageNum, int pageSize, Long ldbId, String validFlag) {
        QueryWrapper<LoadTempCustomerEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq(LoadTempCustomerEntity.LTC_LB_ID, ldbId);
        if (StrUtil.isNotEmpty(validFlag)) {
            queryWrapper.eq(LoadTempCustomerEntity.LTC_VALID_FLAG, validFlag);
        }
        queryWrapper.orderByAsc(LoadTempCustomerEntity.LTC_ID);
        PageInfo<LoadTempCustomerEntity> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(
            () -> this.list(queryWrapper)
        );
        return pageInfo;
    }

    public void doValidate(LoadDataBatchEntity loadBatchEntity) throws IOException, SftpException {
        int pageNum = 1;
        int pageSize = 1000;
        long total = 0;
        long invalid = 0;
        PageInfo<LoadTempCustomerEntity> pageInfo = this.page(pageNum, pageSize, loadBatchEntity.getId(), null);
        if (pageInfo.getList().size() == 0) {
            LoadBatchTemp.set(loadBatchEntity, total, invalid, (total - invalid), StatusEnum.Finished.name());
            loadDataBatchService.updateById(loadBatchEntity);
            throw new CommRuntimeException(ResultCodeEnum.EXCEL_EMPTY);
        }

        String fileName = loadBatchEntity.getId() + ".xlsx";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ExcelWriter excelWriter = EasyExcel.write(outputStream, CustomerTagData.class)
            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
            .registerWriteHandler(new CustomizeWriterHandler())
            .build();

        WriteSheet writeSheet = EasyExcel.writerSheet(loadBatchEntity.getId().toString()).build();

        log.info("current batchId :{}, current pageNum :{}", loadBatchEntity.getId(), pageNum);
        List<CustomerTagData> resultDataList = new ArrayList<>();
        for (LoadTempCustomerEntity tempEntity : pageInfo.getList()) {
            StringBuffer errorMsgBuffer = new StringBuffer();
            int current = 0;
            String cellphone = StrUtil.trim(tempEntity.getCellphone());

            // 手机号 是否合法
            if (this.isInvalidPhone(cellphone)) {
                log.info(String.format("手机号:%s {内容是无效的}", cellphone));
                errorMsgBuffer.append("无效的手机号;");
                current++;
            } else {
                String tagText = StrUtil.trim(tempEntity.getCustomerLabel());
                // 标签内容是否合法
                if (this.isInvalidTagText(tagText)) {
                    log.info(String.format("标签:%s {内容是无效的};", tagText));
                    errorMsgBuffer.append("无效的标签;");
                    current++;
                } else {
                    LoadTempCustomerEntity tempEntityAdd = new LoadTempCustomerEntity();
                    BeanUtil.copyProperties(tempEntity, tempEntityAdd);
                    tempEntityAdd.setValidFlag("Y");
                    this.save(tempEntityAdd);
                    log.info("客户标签导入新增日志：{}", tempEntityAdd.toString());
                    this.removeById(tempEntity.getId());
                    log.info("客户标签导入删除日志：{}", tempEntity.toString());
                }
            }
            if (current > 0) {
                // 有错误
                tempEntity.setValidFlag("N");
                tempEntity.setErrorMsg(errorMsgBuffer.toString());
                this.updateById(tempEntity);
                log.info("客户标签导入更新日志：{}", tempEntity.toString());
                invalid++;
            }
            resultDataList.add(this.toExcelData(tempEntity));
        }
        excelWriter.write(resultDataList, writeSheet);
        if (pageInfo.isHasNextPage()) {
            pageNum++;
        } else {
            total = pageInfo.getTotal();
        }
        log.info("上传总数:{}", total);
        log.info("current batchId :{}, finished", loadBatchEntity.getId());

        excelWriter.finish();
        String downloadFileName = UploadUtils.upload(outputStream, fileName, TypeEnum.CustomerTag.getDownloadPath(), uploadProps);
        loadBatchEntity.setDownloadFileName(downloadFileName);
        LoadBatchTemp.set(loadBatchEntity, total, invalid, (total - invalid), StatusEnum.Finished.name());
        loadDataBatchService.updateById(loadBatchEntity);
        // 校验结束，执行入库
        this.doProcess(loadBatchEntity);
    }

    private CustomerTagData toExcelData(LoadTempCustomerEntity bizTempEntity) {
        CustomerTagData resultData = new CustomerTagData();
        resultData.setCellphone(bizTempEntity.getCellphone());
        resultData.setCustomerTag(bizTempEntity.getCustomerLabel());
        resultData.setErrorMsg(bizTempEntity.getErrorMsg());
        return resultData;
    }

    @Async
    public void doProcess(LoadDataBatchEntity loadBatchEntity) {
        int pageNum = 1;
        int pageSize = 1000;
        long successTotal = 0;

        while (true) {
            log.info("current batchId :{}, current pageNum :{}", loadBatchEntity.getId(), pageNum);
            PageInfo<LoadTempCustomerEntity> pageInfo = this.page(pageNum, pageSize, loadBatchEntity.getId(), "Y");

            for (LoadTempCustomerEntity tempEntity : pageInfo.getList()) {
                this.saveValidData(loadBatchEntity, tempEntity);
                successTotal++;
            }

            if (pageInfo.isHasNextPage()) {
                pageNum++;
            } else {
                break;
            }
        }
        log.info("成功上传总数:{}", successTotal);
    }

    private void saveValidData(LoadDataBatchEntity loadBatchEntity, LoadTempCustomerEntity tempEntity) {
        log.info("saveValidData loadBatchEntity :{}, tempEntity :{}", loadBatchEntity.toString(), tempEntity.toString());
    }

    /**
     * 内容是无效的 字符串合法性校验
     *
     * @param context
     * @return
     */
    public boolean isInvalidPhone(String context) {
        // 非空校验
        if (context == null || "".equals(context)) {
            // 为空
            return true;
        }
        // 格式校验
        if (!Validator.isMobile(context)) {
            // 格式错误
            return true;
        }
        return false;
    }

    /**
     * 内容是无效的 字符串合法性校验
     *
     * @param context
     * @return
     */
    public boolean isInvalidTagText(String context) {
        // 非空校验
        if (context == null || "".equals(context)) {
            // 为空
            return true;
        }
        // 格式校验 (只允许为英文字母、数字、汉字)
        if (!Validator.isGeneralWithChinese(context)) {
            return true;
        }
        return false;
    }

}
