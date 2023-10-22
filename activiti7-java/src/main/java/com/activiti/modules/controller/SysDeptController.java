package com.activiti.modules.controller;

import com.activiti.modules.entity.SysDeptEntity;
import com.activiti.modules.service.SysDeptService;
import com.activiti.utils.R;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 部门信息维护
 *
 * @author liuguofeng
 * @date 2023/10/22 11:52
 **/
@RequestMapping("sysDept")
@RestController
public class SysDeptController {

    @Autowired
    private SysDeptService deptService;

    /**
     * 列表
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(SysDeptEntity dto) {
        PageUtils.startPage();
        List<SysDeptEntity> list = deptService.queryPage(dto);
        return PageUtils.getDataTable(list);
    }

    /**
     * 详情
     *
     * @param deptId 部门id
     * @return 部门详情
     */
    @GetMapping("info/{deptId}")
    public R<SysDeptEntity> info(@PathVariable String deptId) {
        SysDeptEntity model = deptService.getById(deptId);
        return R.ok(model);
    }

    /**
     * 保存
     *
     * @param model 数据
     */
    @PostMapping("save")
    public R<String> info(@RequestBody SysDeptEntity model) {
        Date date = new Date();
        if (StringUtils.isEmpty(model.getDeptId())) {
            model.setCreateTime(date);
        }
        model.setUpdateTime(date);
        deptService.saveOrUpdate(model);
        return R.ok();
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("delete")
    public R<String> delete(@RequestBody String id) {
        deptService.removeById(id);
        return R.ok();
    }
}
