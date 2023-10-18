package com.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试驳回Activiti
 *
 * @author liuguofeng
 * @date 2023/10/17 14:12
 **/
@SpringBootTest
public class ActivitiRejectTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    /**
     * 部署流程
     */
    @Test
    public void deployProcessDefinition() {
        // 获取流程定义与部署相关Service
        Deployment deployment = repositoryService.createDeployment()
                .name("驳回流程测试")
                // 加载资源文件
                .addClasspathResource("bpmn/rejectProcess.bpmn")
                .addClasspathResource("bpmn/rejectProcess.png")
                // 完成部署
                .deploy();
        //输出部署的一些信息
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署名称:" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("applySubmit", "张三");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("rejectProcess", "rejectProcess", varMap);
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());
        taskQuery();
    }

    /**
     * 申请人提交审批节点
     */
    @Test
    public void doApplySubmitTask() {
        //任务负责人
        String assignee = "李四";
//        //获取任务集合
//        List<Task> taskList = taskService.createTaskQuery()
//                .processDefinitionKey("rejectProcess")
////                .processInstanceId()
////                .taskAssignee(assignee)
//                .list();
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("applySubmitPass", true);
        varMap.put("check", assignee);
        taskService.complete("05ce2ea2-6cb6-11ee-b4c1-30c9aba6c580", varMap);
        taskQuery();
    }

    /**
     * 审批节点，驳回
     */
    @Test
    public void doCheckRejectTask() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("checkReject", true);
        varMap.put("checkPass", false);
        taskService.complete("7509", varMap);
        taskQuery();
    }

    /**
     * 查询待办任务
     */
    @Test
    public void taskQuery() {
        List<Task> list = taskService.createTaskQuery()
                // 筛选 multiMeetingApprove 流程
                .processDefinitionKey("rejectProcess")
                .list();
        list.forEach(task -> {
            System.out.println("流程实例id:" + task.getProcessInstanceId());
            System.out.println("任务处理人:" + task.getAssignee());
            System.out.println("任务名:" + task.getName());
            System.out.println("任务定义Key:" + task.getTaskDefinitionKey());
            System.out.println("任务id:" + task.getId());
        });
        if (list.isEmpty()) {
            System.out.println("没有代办任务了，流程结束");
        }
    }
}
