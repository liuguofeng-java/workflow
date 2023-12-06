package com.activiti.modules.controller;

import java.util.Date;
import java.util.List;

import com.activiti.modules.entity.SysListenerEntity;
import com.activiti.modules.entity.dto.SysListenerListDto;
import com.activiti.modules.service.SysListenerService;
import com.activiti.utils.R;
import com.activiti.utils.exception.AException;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 执行监听器
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-12-06 22:18:27
 */
@RestController
@RequestMapping("listener")
public class SysListenerController {
    @Autowired
    private SysListenerService listenerService;

    /**
     * 列表
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(SysListenerListDto dto) {
        PageUtils.startPage();
        List<SysListenerEntity> list = listenerService.queryPage(dto);
        return PageUtils.getDataTable(list);
    }

    /**
     * 详情
     *
     * @param userId 用户id
     * @return 用户详情
     */
    @GetMapping("info/{userId}")
    public R<SysListenerEntity> info(@PathVariable String userId) {
        SysListenerEntity model = listenerService.getById(userId);
        return R.ok(model);
    }

    /**
     * 保存
     *
     * @param model 数据
     */
    @PostMapping("save")
    public R<String> info(@RequestBody SysListenerEntity model) {
        if (StringUtils.isNotEmpty(model.getListenerId())) {
            SysListenerEntity listener = listenerService.getById(model.getListenerId());
            if (listener == null) throw new AException("未知数据");
            if (listener.getIsSys() == 1) throw new AException("系统内置不能修改!");
        }

        try {
            Class.forName(model.getJavaClass());
        } catch (ClassNotFoundException e) {
            throw new AException("保存失败,类不存在");
        }

        Date date = new Date();
        if (StringUtils.isEmpty(model.getListenerId())) {
            model.setCreateTime(date);
        }
        model.setUpdateTime(date);
        listenerService.saveOrUpdate(model);
        return R.ok();
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("delete")
    public R<String> delete(@RequestBody String id) {
        SysListenerEntity listener = listenerService.getById(id);
        if (listener == null) throw new AException("未知数据");
        if (listener.getIsSys() == 1) throw new AException("系统内置不能删除!");

        listenerService.removeById(id);
        return R.ok();
    }

}
