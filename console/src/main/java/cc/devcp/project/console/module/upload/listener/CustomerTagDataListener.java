package cc.devcp.project.console.module.upload.listener;

import cc.devcp.project.common.exception.CommRuntimeException;
import cc.devcp.project.common.module.entity.LoadTempCustomerEntity;
import cc.devcp.project.console.module.upload.data.CustomerTagData;
import cc.devcp.project.console.module.upload.enums.ResEnum;
import cc.devcp.project.console.module.upload.service.impl.LoadTempCustomerServiceImpl;
import com.alibaba.excel.context.AnalysisContext;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/3 14:14
 */
@Slf4j
@Builder
public class CustomerTagDataListener extends AbstractListener<CustomerTagData, LoadTempCustomerEntity> {

    private long batchId;
    private String dealerId;
    private LoadTempCustomerServiceImpl loadCustomerTempService;

    public CustomerTagDataListener(long batchId, String dealerId, LoadTempCustomerServiceImpl loadCustomerTempService) {
        this.batchId = batchId;
        this.dealerId = dealerId;
        this.loadCustomerTempService = loadCustomerTempService;
    }

    @Override
    public void invoke(CustomerTagData vehicleTagData, AnalysisContext analysisContext) {
        LoadTempCustomerEntity customerTempEntity = new LoadTempCustomerEntity();
        customerTempEntity.setLbId(batchId);
        customerTempEntity.setCellphone(vehicleTagData.getCellphone());
        customerTempEntity.setCustomerLabel(vehicleTagData.getCustomerTag().replace("，", ","));
        dataList.add(customerTempEntity);
        if (dataList.size() >= BATCH_SIZE) {
            loadCustomerTempService.createBatch(dataList);
            insertSize += dataList.size();
            log.info("已处理:{}", insertSize);
            dataList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (dataList.isEmpty()) {
            //do nothing
        } else {
            loadCustomerTempService.createBatch(dataList);
            insertSize += dataList.size();
            dataList.clear();
        }
        if (insertSize == 0) {
            throw new CommRuntimeException(ResEnum.EXCEL_CONTENT_ERROR);
        }
        log.info("所有数据处理完毕:{}", insertSize);
        insertSize = 0;
    }
}
