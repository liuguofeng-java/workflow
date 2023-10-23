package com.activiti.modules.controller;

import com.activiti.modules.entity.dto.ProcessDefinitionListDto;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.utils.R;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 流程维护
 *
 * @author liuguofeng
 * @date 2023/10/21 11:28
 **/
@RequestMapping("processDefinition")
@RestController
public class ProcessDefinitionController {
    @Autowired
    private ProcessDefinitionService processDefinitionService;

    /**
     * 列表
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(ProcessDefinitionListDto dto) {
        return processDefinitionService.queryPage(dto);
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("delete")
    public R<String> delete(@RequestBody String id) {
        processDefinitionService.delete(id);
        return R.ok();
    }


}