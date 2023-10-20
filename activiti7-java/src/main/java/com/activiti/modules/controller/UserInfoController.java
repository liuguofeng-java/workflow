package com.activiti.modules.controller;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.UserInfoListDto;
import com.activiti.modules.service.SysUserService;
import com.activiti.utils.R;
import com.activiti.utils.exception.AException;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@RequestMapping("userInfo")
@RestController
public class UserInfoController {
    @Autowired
    private SysUserService userService;

    /**
     * 列表
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(UserInfoListDto dto) {
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
        userService.removeById(id);
        return R.ok();
    }
}
