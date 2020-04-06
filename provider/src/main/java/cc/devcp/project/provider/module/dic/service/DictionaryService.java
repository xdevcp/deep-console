package cc.devcp.project.provider.module.dic.service;

import cc.devcp.project.common.exception.CommRuntimeException;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.result.ArrayResult;
import cc.devcp.project.common.model.result.ResEntity;
import cc.devcp.project.common.model.result.ResultData;
import cc.devcp.project.provider.module.dic.entity.DBEnum;
import cc.devcp.project.provider.module.dic.entity.DataDictionaryEntity;
import cc.devcp.project.provider.module.dic.entity.DicEnum;
import cc.devcp.project.provider.module.dic.mapper.DictionaryMapper;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     description: 数据字典
 * </pre>
 *
 * @author deep.pw
 * @version 2020.03.15
 */
@Service
public class DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    /**
     * <pre>
     *     description: 分页查询字典类型
     * </pre>
     *
     * @author deep.pw
     * @version 2020.03.15
     */
    public ResEntity queryDictionary(PageParam param, String dataType, String status) {
        ResultData<DataDictionaryEntity> resultData = new ResultData();
        QueryWrapper<DataDictionaryEntity> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isBlankOrUndefined(dataType)) {
            queryWrapper.eq("DIC_DATA_TYPE", dataType);
        }
        if (!StrUtil.isBlankOrUndefined(status)) {
            queryWrapper.eq("DIC_STATUS", status);
        }
        queryWrapper.ne("DIC_PARENT_ID", 0);
        queryWrapper.orderByAsc("DIC_DATA_TYPE");
        queryWrapper.orderByAsc("DIC_SORT_NO");
        queryWrapper.orderByDesc("DIC_UPDATE_TIME");

        PageInfo<DataDictionaryEntity> pageInfo = PageHelper.startPage(param.getCurrent(), param.getSize()).doSelectPageInfo(() -> {
            dictionaryMapper.selectList(queryWrapper);
        });

        resultData.setTotal(pageInfo.getTotal());
        resultData.setList(pageInfo.getList());
        return ResEntity.ok(resultData);
    }

    public ArrayResult queryDataType(String q) {

        QueryWrapper<DataDictionaryEntity> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isBlankOrUndefined(q)) {
            queryWrapper.like("DIC_DATA_TYPE", q);
        }
        queryWrapper.eq("DIC_PARENT_ID", 0);
        queryWrapper.orderByAsc("DIC_DATA_TYPE");
        List<DataDictionaryEntity> typeList = dictionaryMapper.selectList(queryWrapper);

        List list = new ArrayList();
        for (DataDictionaryEntity entity : typeList) {
            if (StringUtils.isNotEmpty(entity.getDataType())) {
                String[] args = new String[]{entity.getDataType(), entity.getDataValue()};
                list.add(args);
            }
        }
        return ArrayResult.ok(list);
    }

    public ResEntity details(String q, String w) {
        QueryWrapper<DataDictionaryEntity> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isBlankOrUndefined(q)) {
            queryWrapper.eq("DIC_DATA_TYPE", q);
        }
        if (!StrUtil.isBlankOrUndefined(w)) {
            queryWrapper.eq("DIC_DATA_CODE", w);
        }
        queryWrapper.last("LIMIT 1");
        DataDictionaryEntity record = dictionaryMapper.selectOne(queryWrapper);
        return ResEntity.ok(record);
    }

    /**
     * <pre>
     *     description: 创建数据字典
     * </pre>
     *
     * @author deep.pw
     * @version 2020.03.15
     */
    @Transactional(rollbackFor = CommRuntimeException.class)
    public ResEntity createDictionary(DataDictionaryEntity dictionaryEntity) {
        int res = 0;
        QueryWrapper<DataDictionaryEntity> queryWrapper = new QueryWrapper<>();
        if (dictionaryEntity.getParentId() == 0) {
            // 校验字典类型是否重复
            queryWrapper.eq("DIC_DATA_TYPE", dictionaryEntity.getDataType());
            queryWrapper.eq("DIC_PARENT_ID", dictionaryEntity.getParentId());
            if (dictionaryMapper.selectOne(queryWrapper) != null) {
                return ResEntity.fail(DicEnum.DIC_TYPE_EXITS);
            }
        } else {
            if (StringUtils.isBlank(dictionaryEntity.getDataCode())) {
                return ResEntity.fail(DicEnum.DIC_CODE_CANNOT_BE_NULL);
            }
            // 校验数据编码是否重复
            queryWrapper.eq("DIC_DATA_CODE", dictionaryEntity.getDataCode());
            queryWrapper.eq("DIC_PARENT_ID", dictionaryEntity.getParentId());
            if (dictionaryMapper.selectOne(queryWrapper) != null) {
                return ResEntity.fail(DicEnum.DIC_CODE_EXITS);
            }
        }
        dictionaryEntity.setStatus(DBEnum.VALID.getCode());
        dictionaryEntity.setUpdateTime(DateUtil.date());
        res = dictionaryMapper.insert(dictionaryEntity);
        if (res != 1) {
            throw new CommRuntimeException(DicEnum.DIC_CREATE_ERROR);
        }
        DataDictionaryEntity updateEntity = new DataDictionaryEntity();
        updateEntity.setId(dictionaryEntity.getId());
        updateEntity.setValueId(dictionaryEntity.getId());
        res = dictionaryMapper.updateById(updateEntity);
        if (res != 1) {
            throw new CommRuntimeException(DicEnum.DIC_CREATE_ERROR);
        }
        return ResEntity.ok();
    }

    /**
     * @param dictionaryEntity
     * @return ResEntity
     * @desc: 修改数据字典
     * @date 2019/12/18 14:05
     */
    @Transactional(rollbackFor = CommRuntimeException.class)
    public ResEntity modifyDictionary(DataDictionaryEntity dictionaryEntity) {
        int res = 0;
        QueryWrapper<DataDictionaryEntity> queryWrapper = new QueryWrapper<>();
        if (dictionaryEntity.getParentId() == 0) {
            if (StringUtils.isBlank(dictionaryEntity.getDataType())) {
                return ResEntity.fail(DicEnum.DIC_TYPE_CANNOT_BE_NULL);
            }
            // 校验字典类型是否存在不包含本身
            queryWrapper.eq("DIC_DATA_TYPE", dictionaryEntity.getDataType())
                .eq("DIC_PARENT_ID", dictionaryEntity.getParentId())
                .ne("DIC_ID", dictionaryEntity.getId());
            if (dictionaryMapper.selectOne(queryWrapper) != null) {
                throw new CommRuntimeException(DicEnum.DIC_TYPE_EXITS);
            }
            // 如果更改了数据类型 同步更新所有子集数据类型
            DataDictionaryEntity resEntity = dictionaryMapper.selectById(dictionaryEntity.getId());
            if (!resEntity.getDataType().equals(dictionaryEntity.getDataType())) {
                // 修改的值
                DataDictionaryEntity updateEntity = new DataDictionaryEntity();
                updateEntity.setDataType(dictionaryEntity.getDataType());
                // 修改条件
                UpdateWrapper<DataDictionaryEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("DIC_DATA_TYPE", resEntity.getDataType());
                // 批量更新
                dictionaryMapper.update(updateEntity, updateWrapper);
            }
            // 更新字典类型
            dictionaryEntity.setUpdateTime(DateUtil.date());
            res = dictionaryMapper.updateById(dictionaryEntity);
            if (res != 1) {
                throw new CommRuntimeException(DicEnum.DIC_MODIFY_ERROR);
            }
        } else {
            if (StringUtils.isBlank(dictionaryEntity.getDataCode())) {
                return ResEntity.fail(DicEnum.DIC_CODE_CANNOT_BE_NULL);
            }
            // 校验数据编码是否存在不包含本身
            queryWrapper.eq("DIC_DATA_CODE", dictionaryEntity.getDataCode())
                .eq("DIC_PARENT_ID", dictionaryEntity.getParentId())
                .ne("DIC_ID", dictionaryEntity.getId());
            if (dictionaryMapper.selectOne(queryWrapper) != null) {
                throw new CommRuntimeException(DicEnum.DIC_CODE_EXITS);
            }
            dictionaryEntity.setUpdateTime(DateUtil.date());
            res = dictionaryMapper.updateById(dictionaryEntity);
            if (res != 1) {
                throw new CommRuntimeException(DicEnum.DIC_MODIFY_ERROR);
            }
        }
        return ResEntity.ok();
    }

    /**
     * @param childId
     * @return ResEntity<Map < String, Object>>
     * @desc: 查找父级id以及父级的值
     * @date 2019/12/19 19:31
     */
    public ResEntity<Map<String, Object>> queryParentId(String childId) {
        Map<String, Object> map = new HashMap<>(16);

        QueryWrapper<DataDictionaryEntity> queryUpLevelWrapper = new QueryWrapper<>();
        queryUpLevelWrapper.eq("DIC_VALUE_ID", childId);

        DataDictionaryEntity upEntity = dictionaryMapper.selectOne(queryUpLevelWrapper);
        if (upEntity == null) {
            throw new CommRuntimeException(DicEnum.DIC_PARENT_NOT_EXITS);
        }
        if (upEntity.getParentId() == null) {
            throw new CommRuntimeException(DicEnum.DIC_PARENT_NOT_EXITS);
        }
        map.put("pid", upEntity.getParentId());

        // 如果不是字典类型 需要查找父级字典的值
        if (upEntity.getParentId() != 0) {
            QueryWrapper<DataDictionaryEntity> queryDicValueWrapper = new QueryWrapper<>();
            queryDicValueWrapper.eq("DIC_VALUE_ID", upEntity.getParentId());

            DataDictionaryEntity entity = dictionaryMapper.selectOne(queryDicValueWrapper);
            if (entity == null) {
                throw new CommRuntimeException(DicEnum.DIC_PARENT_NOT_EXITS);
            }
            map.put("dataValue", entity.getDataValue());
        }
        return ResEntity.ok(map);
    }

    /**
     * @param ids
     * @return ResEntity
     * @desc: 批量删除数据字典(逻辑删除)
     * @date 2019/12/23 15:04
     */
    public ResEntity removeDictionary(String ids) {
        if (StrUtil.isBlankOrUndefined(ids)) {
            return ResEntity.fail(DicEnum.DIC_REMOVE_ERROR);
        }
        List<Integer> idList = new ArrayList<>();
        for (String s : ids.split(",")) {
            // 子节点集合
            List<Integer> childList = new ArrayList<>();
            Integer id = Integer.parseInt(s);
            // 调用递归方法
            List<Integer> list = getChildList(childList, id);
            idList.add(id);
            idList.addAll(list);
        }
        int res = dictionaryMapper.batchUpdateStatus(idList, DBEnum.INVALID.getCode());
        if (res != idList.size()) {
            throw new CommRuntimeException(DicEnum.DIC_REMOVE_ERROR);
        }
        return ResEntity.ok();
    }

    /**
     * @param childList
     * @param id
     * @return List<Integer>
     * @desc: 递归查询子节点
     * @date 2019/12/21 14:54
     */
    private List<Integer> getChildList(List<Integer> childList, Integer id) {
        //遍历出父id等于参数的id，add进子节点集合
        QueryWrapper<DataDictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DIC_PARENT_ID", id);
        List<DataDictionaryEntity> list = dictionaryMapper.selectList(queryWrapper);
        if (list.size() > 0) {
            for (DataDictionaryEntity entity : list) {
                childList.add(entity.getValueId());
                // 递归遍历下一层
                getChildList(childList, entity.getValueId());
            }
        }
        return childList;
    }

    /**
     * @param dicType
     * @return ResEntity
     * @desc: 对外查询数据字典
     * @date 2019/12/17 19:16
     */
    public ResEntity<List<DataDictionaryEntity>> findDictionaryByType(String dicType) {

        QueryWrapper<DataDictionaryEntity> queryDicTypeWrapper = new QueryWrapper<>();
        queryDicTypeWrapper.eq("DIC_DATA_TYPE", dicType)
            .eq("DIC_PARENT_ID", 0)
            .eq("DIC_STATUS", DBEnum.VALID.getCode());
        DataDictionaryEntity dicTypeEntity = dictionaryMapper.selectOne(queryDicTypeWrapper);
        if (dicTypeEntity == null) {
            throw new CommRuntimeException(DicEnum.DIC_TYPE_NOT_EXITS);
        }

        QueryWrapper<DataDictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DIC_PARENT_ID", dicTypeEntity.getValueId())
            .eq("DIC_STATUS", DBEnum.VALID.getCode())
            .orderByAsc("DIC_SORT_NO")
            .orderByDesc("DIC_UPDATE_TIME");
        List<DataDictionaryEntity> list = dictionaryMapper.selectList(queryWrapper);

        return ResEntity.ok(list);
    }
}



