package com.activiti.modules.controller;

import com.activiti.modules.entity.dto.ProcessDefinitionListDto;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.utils.R;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
     * 获取流程定义xml
     *
     * @param deploymentId 部署id
     */
    @GetMapping("getDefinitionXml")
    public R<String> getDefinitionXml(String deploymentId) throws IOException {
        String xml = processDefinitionService.getDefinitionXml(deploymentId);
        return R.ok(xml);
    }

    /**
     * 部署流程
     *
     * @param xmlStr xml字符串
     */
    @PostMapping("deployment")
    public R<String> deployment(@RequestBody String xmlStr) {
        processDefinitionService.deploymentXmlByStr(xmlStr);
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
