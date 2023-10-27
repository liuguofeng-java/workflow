package com.activiti.modules.controller;

import java.util.Date;
import java.util.List;

import com.activiti.modules.entity.SysFormEntity;
import com.activiti.modules.service.SysFormService;
import com.activiti.utils.R;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * activiti相关->流程表单
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-10-27 09:44:39
 */
@RestController
@RequestMapping("sysForm")
public class SysFormController {
    @Autowired
    private SysFormService formService;

    /**
     * 列表
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(SysFormEntity dto) {
        PageUtils.startPage();
        List<SysFormEntity> list = formService.queryPage(dto);
        return PageUtils.getDataTable(list);
    }

    /**
     * 详情
     *
     * @param deptId 部门id
     * @return 表单详情
     */
    @GetMapping("info/{deptId}")
    public R<SysFormEntity> info(@PathVariable String deptId) {
        SysFormEntity model = formService.getById(deptId);
        return R.ok(model);
    }

    /**
     * 保存
     *
     * @param model 数据
     */
    @PostMapping("save")
    public R<String> info(@RequestBody SysFormEntity model) {
        Date date = new Date();
        if (StringUtils.isEmpty(model.getFormId())) {
            model.setCreateTime(date);
        }
        model.setUpdateTime(date);
        formService.saveOrUpdate(model);
        return R.ok();
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("delete")
    public R<String> delete(@RequestBody String id) {
        formService.removeById(id);
        return R.ok();
    }

}
