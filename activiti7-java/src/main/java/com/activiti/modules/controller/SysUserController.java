package com.activiti.modules.controller;

import com.activiti.modules.entity.SysDeptEntity;
import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.SysUserListDto;
import com.activiti.modules.service.SysDeptService;
import com.activiti.modules.service.SysUserService;
import com.activiti.utils.R;
import com.activiti.utils.exception.AException;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 用户信息维护
 *
 * @author liuguofeng
 * @date 2023/10/18 11:03
 **/
@RequestMapping("sysUser")
@RestController
public class SysUserController {
    @Autowired
    private SysUserService userService;

    @Autowired
    private SysDeptService deptService;

    /**
     * 部门列表
     */
    @GetMapping("deptList")
    public R<List<SysDeptEntity>> list() {
        List<SysDeptEntity> list = deptService.list();
        return R.ok(list);
    }

    /**
     * 列表
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(SysUserListDto dto) {
        PageUtils.startPage();
        List<SysUserEntity> list = userService.queryPage(dto);
        return PageUtils.getDataTable(list);
    }

    /**
     * 详情
     *
     * @param userId 用户id
     * @return 用户详情
     */
    @GetMapping("info/{userId}")
    public R<SysUserEntity> info(@PathVariable String userId) {
        SysUserEntity model = userService.getById(userId);
        return R.ok(model);
    }

    /**
     * 保存
     *
     * @param model 数据
     */
    @PostMapping("save")
    public R<String> info(@RequestBody SysUserEntity model) {
        if (StringUtils.isNotEmpty(model.getUserId())) {
            SysUserEntity user = userService.getById(model.getUserId());
            if (user == null) throw new AException("未知数据");
            if (user.getIsSys() == 1) throw new AException("系统内置不能修改!");
        }

        Date date = new Date();
        if (StringUtils.isEmpty(model.getUserId())) {
            model.setCreateTime(date);
        }
        model.setUpdateTime(date);
        userService.saveOrUpdate(model);
        return R.ok();
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("delete")
    public R<String> delete(@RequestBody String id) {

        SysUserEntity user = userService.getById(id);
        if (user == null) throw new AException("未知数据");
        if (user.getIsSys() == 1) throw new AException("系统内置不能删除!");

        userService.removeById(id);
        return R.ok();
    }
}
