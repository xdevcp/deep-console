package cc.devcp.project.upload.module.excel.controller;

import cc.devcp.project.common.constant.GlobalRouter;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.page.PageResult;
import cc.devcp.project.common.model.result.ResEntity;
import cc.devcp.project.upload.module.excel.entity.LoadDataBatchEntity;
import cc.devcp.project.upload.module.excel.enums.ResEnum;
import cc.devcp.project.upload.module.excel.service.UploadService;
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
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-04-04
 */
@Slf4j
@RestController
@RequestMapping(value = GlobalRouter.VER_AUTH + "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "自定义上传")
public class UploadController {

    public static final String PATTERN = "^.*(xlsx|XLSX)$";

    @Autowired
    private UploadService uploadService;

    @ApiOperation("查询上传任务")
    @GetMapping(value = "/list")
    public ResEntity<PageResult<LoadDataBatchEntity>> queryList(@RequestParam(required = false) int pageNo,
                                                                @RequestParam(required = false) int pageSize,
                                                                String dealerId) {
        PageParam pageParam = PageParam.of(pageNo, pageSize);
        return uploadService.queryDataBatch(dealerId, pageParam);
    }

    @ApiOperation("上传客户标签")
    @PostMapping(value = "/customer/tag", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResEntity uploadCustomerTag(String dealerId,
                                       @RequestPart("excel") MultipartFile excel) throws IOException, SftpException {
        if (!excel.getOriginalFilename().matches(PATTERN)) {
            return ResEntity.fail(ResEnum.EXCEL_TYPE_ERROR);
        }
        uploadService.uploadCustomerTag(dealerId, excel);
        return ResEntity.ok();
    }

}