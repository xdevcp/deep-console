package cc.devcp.project.console.module.dic.controller;

import cc.devcp.project.common.constant.GlobalRouter;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.page.PageResult;
import cc.devcp.project.common.model.response.ResEntity;
import cc.devcp.project.common.validator.Create;
import cc.devcp.project.common.validator.Modify;
import cc.devcp.project.common.validator.Query;
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
 * @author deep.wu
 * @version 1.0 on 2020/3/14
 */
@Slf4j
@RestController
@RequestMapping(value = GlobalRouter.OPEN, produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "数据字典管理")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * @param
     * @return ResEntity
     * @desc: 分页查询数据字典
     * @date 2019/12/17 19:16
     */
    @ApiOperation(value = "queryDictionary", notes = "分页查询数据字典")
    @GetMapping("/dictionary")
    public ResEntity<PageResult<DictionaryEntity>> queryDictionary(@RequestParam(required = false) int pageNo,
                                                                   @RequestParam(required = false) int pageSize,
                                                                   @RequestParam @Validated(Query.class) Integer parentId) {
        PageParam pageParam = PageParam.of(pageNo, pageSize);
        return dictionaryService.queryDictionary(pageParam, parentId);
    }

    /**
     * @param dictionaryEntity
     * @return ResEntity
     * @desc: 创建数据字典
     * @date 2019/12/17 19:18
     */
    @ApiOperation(value = "createDictionary", notes = "创建数据字典")
    @PostMapping("/dictionary")
    public ResEntity createDictionary(@RequestBody @Validated(Create.class) DictionaryEntity dictionaryEntity) {
        return dictionaryService.createDictionary(dictionaryEntity);
    }

    /**
     * @param dictionaryEntity
     * @return ResEntity
     * @desc: 修改数据字典
     * @date 2019/12/19 16:47
     */
    @ApiOperation(value = "modifyDictionary", notes = "修改数据字典")
    @PatchMapping("/dictionary")
    public ResEntity modifyDictionary(@RequestBody @Validated(Modify.class) DictionaryEntity dictionaryEntity) {
        return dictionaryService.modifyDictionary(dictionaryEntity);
    }

    /**
     * @param childId
     * @return ResEntity<Map < String, Object>>
     * @desc: 查找父级id以及父级的值
     * @date 2019/12/19 19:31
     */
    @ApiOperation(value = "queryParentId", notes = "根据子级Id查找父级Id")
    @GetMapping("/dictionary/getPid")
    public ResEntity<Map<String, Object>> queryParentId(@RequestParam String childId) {
        return dictionaryService.queryParentId(childId);
    }

    /**
     * @param ids
     * @return ResEntity
     * @desc: 批量删除数据字典(逻辑删除)
     * @date 2019/12/23 15:04
     */
    @ApiOperation(value = "removeDictionary", notes = "逻辑删除数据字典")
    @PatchMapping("/dictionary/batch/remove")
    public ResEntity removeDictionary(@RequestParam String[] ids) {
        return dictionaryService.removeDictionary(ids);
    }

    /**
     * @param dicType
     * @return ResEntity
     * @desc: 根据字典类型查询下一级数据
     * @date 2019/12/17 19:16
     */
    @ApiOperation(value = "findDictionaryByType", notes = "根据字典类型查询下一级数据")
    @GetMapping("/dictionary/type")
    public ResEntity<List<DictionaryEntity>> findDictionaryByType(@RequestParam String dicType) {
        return dictionaryService.findDictionaryByType(dicType);
    }
}
