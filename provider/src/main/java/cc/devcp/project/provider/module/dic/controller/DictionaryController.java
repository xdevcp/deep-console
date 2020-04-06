package cc.devcp.project.provider.module.dic.controller;

import cc.devcp.project.common.constant.CxtRouter;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.result.ArrayResult;
import cc.devcp.project.common.model.result.ResEntity;
import cc.devcp.project.common.validator.Create;
import cc.devcp.project.common.validator.Modify;
import cc.devcp.project.provider.module.dic.entity.DataDictionaryEntity;
import cc.devcp.project.provider.module.dic.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-03-23
 */
@Slf4j
@RestController
@RequestMapping(value = CxtRouter.gateway_ver_open + "/dict")
@Api(tags = "数据字典管理")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation(value = "查询数据字典")
    @GetMapping
    public ResEntity queryDictionary(@RequestParam(required = false) Integer pageNo,
                                     @RequestParam(required = false) Integer pageSize,
                                     @RequestParam(required = false) String dataType,
                                     @RequestParam(required = false) String status) {
        return dictionaryService.queryDictionary(PageParam.of(pageNo, pageSize), dataType, status);
    }

    @ApiOperation(value = "创建数据字典", notes = "")
    @PostMapping
    public ResEntity createDictionary(@RequestBody @Validated(Create.class) DataDictionaryEntity dictionaryEntity) {
        return dictionaryService.createDictionary(dictionaryEntity);
    }

    @ApiOperation(value = "修改数据字典", notes = "")
    @PutMapping
    public ResEntity modifyDictionary(@RequestBody @Validated(Modify.class) DataDictionaryEntity dictionaryEntity) {
        return dictionaryService.modifyDictionary(dictionaryEntity);
    }

    @ApiOperation(value = "逻辑删除数据字典", notes = "")
    @DeleteMapping
    public ResEntity removeDictionary(
        @RequestParam(name = "param", defaultValue = StringUtils.EMPTY) String param) {
        System.out.println(param + "=====");
        return dictionaryService.removeDictionary(param);
    }

    @ApiOperation(value = "查询数据类型")
    @GetMapping("/dataType")
    public ArrayResult queryDataType(@RequestParam(required = false) String q) {
        return dictionaryService.queryDataType(q);
    }

    @ApiOperation(value = "查询详情")
    @GetMapping("/details")
    public ResEntity details(@RequestParam(required = false) String q,
                             @RequestParam(required = false) String w) {
        return dictionaryService.details(q, w);
    }

    @ApiOperation(value = "根据子级Id查找父级Id")
    @GetMapping("/getPid")
    public ResEntity<Map<String, Object>> queryParentId(@RequestParam String childId) {
        return dictionaryService.queryParentId(childId);
    }

    @ApiOperation(value = "根据字典类型查询下一级数据")
    @GetMapping("/type")
    public ResEntity<List<DataDictionaryEntity>> findDictionaryByType(@RequestParam String dicType) {
        return dictionaryService.findDictionaryByType(dicType);
    }
}
