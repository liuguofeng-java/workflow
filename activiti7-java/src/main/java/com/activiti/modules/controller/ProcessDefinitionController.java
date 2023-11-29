package com.activiti.modules.controller;

import com.activiti.modules.entity.dto.workflow.DefinitionListDto;
import com.activiti.modules.entity.dto.workflow.DeployProcessDto;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.utils.R;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

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
    public TableDataInfo list(DefinitionListDto dto) {
        return processDefinitionService.queryPage(dto);
    }

    /**
     * 获取流程定义详情
     *
     * @param deploymentId 部署id
     */
    @GetMapping("getDefinitionInfo")
    public R<Map<String, Object>> getDefinitionInfo(String deploymentId) {
        Map<String, Object> result = processDefinitionService.getDefinitionInfo(deploymentId);
        return R.ok(result);
    }

    /**
     * 部署流程
     *
     * @param dto 参数
     */
    @PostMapping("deployProcess")
    public R<String> deployProcess(@RequestBody DeployProcessDto dto) {
        processDefinitionService.deployProcess(dto);
        return R.ok();
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
