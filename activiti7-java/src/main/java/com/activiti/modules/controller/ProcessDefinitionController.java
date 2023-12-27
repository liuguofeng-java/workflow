package com.activiti.modules.controller;

import com.activiti.modules.entity.dto.workflow.DefinitionListDto;
import com.activiti.modules.entity.dto.workflow.DeployProcessDto;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.utils.R;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
     * 获取流程定义xml
     *
     * @param deploymentId 部署id
     */
    @GetMapping("getDefinitionXml")
    public R<String> getDefinitionXml(String deploymentId) {
        String xml = processDefinitionService.getDefinitionXml(deploymentId);
        return R.ok(xml);
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
     * 更新流程定义状态 激活或者挂起
     *
     * @param deploymentId 部署id
     */
    @GetMapping("updateState")
    public R<String> updateState(String deploymentId) {
        processDefinitionService.updateState(deploymentId);
        return R.ok();
    }

    /**
     * 部署流程
     *
     * @param dto 参数
     */
    @PostMapping("deployProcess")
    public R<String> deployProcess(@Valid @RequestBody DeployProcessDto dto) {
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
