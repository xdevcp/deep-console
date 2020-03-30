package cc.devcp.project.console.module.upload.service;

import cc.devcp.project.common.model.page.PageHelperUtils;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.page.PageResult;
import cc.devcp.project.common.model.result.ResEntity;
import cc.devcp.project.console.module.upload.data.CustomerTagData;
import cc.devcp.project.console.module.upload.entity.LoadDataBatchEntity;
import cc.devcp.project.console.module.upload.enums.StatusEnum;
import cc.devcp.project.console.module.upload.enums.TypeEnum;
import cc.devcp.project.console.module.upload.listener.CustomerTagDataListener;
import cc.devcp.project.console.module.upload.service.impl.LoadBatchServiceImpl;
import cc.devcp.project.console.module.upload.service.impl.LoadTempCustomerServiceImpl;
import cc.devcp.project.console.module.upload.utils.UploadProps;
import cc.devcp.project.console.module.upload.utils.UploadUtils;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/2 10:17
 */
@Slf4j
@Service
public class UploadService {

    private static final String FORMAT_FINISHED_RESULT = "导入数据：%d；成功：%d；失败：%d";

    @Autowired
    private LoadTempCustomerServiceImpl loadTempCustomerService;

    @Autowired
    private LoadBatchServiceImpl loadBatchService;

    @Autowired
    private UploadProps uploadProps;

    /**
     * 查询自定义任务
     *
     * @param dealerId
     * @param pageParam
     * @return
     */
    public ResEntity<PageResult<LoadDataBatchEntity>> queryDataBatch(String dealerId, PageParam pageParam) {
        QueryWrapper<LoadDataBatchEntity> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(dealerId)) {
            queryWrapper.eq(LoadDataBatchEntity.LB_CREATE_USER, dealerId);
        }
        queryWrapper.orderByDesc(LoadDataBatchEntity.LB_ID);
        PageInfo<LoadDataBatchEntity> pageInfo = PageHelper.startPage(pageParam.getCurrent(), pageParam.getSize()).doSelectPageInfo(
            () -> loadBatchService.list(queryWrapper));
        pageInfo.getList().forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUploadFileName())) {
                item.setUploadUrl(uploadProps.getUrlPrefix() + item.getUploadFileName());
            }
            if (StringUtils.isNotEmpty(item.getDownloadFileName())) {
                item.setDownloadUrl(uploadProps.getUrlPrefix() + item.getDownloadFileName());
            }
            if (StatusEnum.Processing.name().equals(item.getStatus())) {
                item.setResult("正在导入，请耐心等待");
            }
            if (StatusEnum.Finished.name().equals(item.getStatus())) {
                item.setResult(String.format(FORMAT_FINISHED_RESULT, item.getNumberAll(), item.getNumberValid(), item.getNumberInvalid()));
            }
            TypeEnum typeEnum = TypeEnum.valueOf(item.getType());
            item.setType(typeEnum.getContent());
            StatusEnum statusEnum = StatusEnum.valueOf(item.getStatus());
            item.setStatus(statusEnum.getContent());
        });
        return ResEntity.ok(PageHelperUtils.to(pageInfo));
    }

    /**
     * 上传客户标签
     *
     * @param excel
     * @throws IOException
     */
    public void uploadCustomerTag(String dealerId, MultipartFile excel) throws IOException, SftpException {
        Long id = IdWorker.getId();

        String fileName = excel.getOriginalFilename().toLowerCase().replace(".xlsx", "." + id + ".xlsx");
        String upload = UploadUtils.upload(excel, fileName, TypeEnum.CustomerTag.getUploadPath(), uploadProps);
        log.info("任务编号：{}，上传路径: {}，访问地址：{}", id, upload, uploadProps.getUrlPrefix() + upload);

        try {
            EasyExcel.read(excel.getInputStream(),
                CustomerTagData.class,
                CustomerTagDataListener.builder()
                    .batchId(id)
                    .dealerId(dealerId)
                    .loadCustomerTempService(loadTempCustomerService)
                    .build())
                .sheet()
                .headRowNumber(2)
                .doRead();
        } catch (Exception e) {
            log.error("EasyExcel Error:{}", e.getMessage());
        }

        LoadDataBatchEntity loadDataBatchEntity = createBatch(id, dealerId, TypeEnum.CustomerTag.name(), upload);

        loadTempCustomerService.doValidate(loadDataBatchEntity);
    }

    /**
     * 创建批次表
     *
     * @param id
     * @param dealerId
     * @param type
     * @param fileName
     * @return
     */
    private LoadDataBatchEntity createBatch(Long id, String dealerId, String type, String fileName) {

        LoadDataBatchEntity loadDataBatchEntity = new LoadDataBatchEntity();
        loadDataBatchEntity.setId(id);
        loadDataBatchEntity.setCreateUser(dealerId);
        loadDataBatchEntity.setCreateDate(LocalDateTime.now());
        loadDataBatchEntity.setType(type);
        loadDataBatchEntity.setUploadFileName(fileName);
        loadDataBatchEntity.setStatus(StatusEnum.Processing.name());
        loadBatchService.save(loadDataBatchEntity);
        return loadDataBatchEntity;
    }

}
