package cc.devcp.project.console.module.upload.controller;

import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.page.PageResult;
import cc.devcp.project.common.model.response.ResEntity;
import cc.devcp.project.console.module.upload.entity.LoadBatchEntity;
import cc.devcp.project.console.module.upload.enums.ResEnum;
import cc.devcp.project.console.module.upload.service.UploadService;
import com.jcraft.jsch.SftpException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/2 10:17
 */
@Slf4j
@RestController
@RequestMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags = "自定义上传")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @ApiOperation("查询上传任务")
    @GetMapping(value = "/query")
    public ResEntity<PageResult<LoadBatchEntity>> queryDataBatch(String dealerId, PageParam pageParam) {
        return uploadService.queryDataBatch(dealerId, pageParam);
    }

    @ApiOperation("上传客户标签")
    @PostMapping(value = "/customer/tag", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResEntity uploadCustomerTag(String dealerId,
                                       @RequestPart("excel") MultipartFile excel) throws IOException, SftpException {
        if (!excel.getOriginalFilename().matches("^.*(xlsx|XLSX)$")) {
            return ResEntity.fail(ResEnum.EXCEL_TYPE_ERROR);
        }
        uploadService.uploadCustomerTag(dealerId, excel);
        return ResEntity.ok();
    }

}
