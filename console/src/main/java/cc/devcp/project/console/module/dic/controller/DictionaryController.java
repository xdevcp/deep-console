package cc.devcp.project.console.module.dic.controller;

import cc.devcp.project.common.constant.GlobalRouter;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.result.ArrayResult;
import cc.devcp.project.common.model.result.ResEntity;
import cc.devcp.project.common.validator.Create;
import cc.devcp.project.common.validator.Modify;
import cc.devcp.project.console.module.dic.entity.DictionaryEntity;
import cc.devcp.project.console.module.dic.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping(value = GlobalRouter.OPEN, produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "数据字典管理")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation(value = "查询数据字典")
    @GetMapping("/dict")
    public ResEntity queryDictionary(@RequestParam(required = false) Integer pageNo,
                                     @RequestParam(required = false) Integer pageSize,
                                     @RequestParam(required = false) String dataType,
                                     @RequestParam(required = false) String status) {
        return dictionaryService.queryDictionary(PageParam.of(pageNo, pageSize), dataType, status);
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

    @ApiOperation(value = "createDictionary", notes = "创建数据字典")
    @PostMapping("/dict")
    public ResEntity createDictionary(@RequestBody @Validated(Create.class) DictionaryEntity dictionaryEntity) {
        return dictionaryService.createDictionary(dictionaryEntity);
    }

    @ApiOperation(value = "modifyDictionary", notes = "修改数据字典")
    @PutMapping("/dict")
    public ResEntity modifyDictionary(@RequestBody @Validated(Modify.class) DictionaryEntity dictionaryEntity) {
        return dictionaryService.modifyDictionary(dictionaryEntity);
    }

    @ApiOperation(value = "queryParentId", notes = "根据子级Id查找父级Id")
    @GetMapping("/dict/getPid")
    public ResEntity<Map<String, Object>> queryParentId(@RequestParam String childId) {
        return dictionaryService.queryParentId(childId);
    }

    @ApiOperation(value = "removeDictionary", notes = "逻辑删除数据字典")
    @PatchMapping("/dictionary/batch/remove")
    public ResEntity removeDictionary(@RequestParam String[] ids) {
        return dictionaryService.removeDictionary(ids);
    }

    @ApiOperation(value = "findDictionaryByType", notes = "根据字典类型查询下一级数据")
    @GetMapping("/dictionary/type")
    public ResEntity<List<DictionaryEntity>> findDictionaryByType(@RequestParam String dicType) {
        return dictionaryService.findDictionaryByType(dicType);
    }
}
