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

import java.util.List;

/**
 * 测试折扣Activiti
 *
 * @author liuguofeng
 * @date 2023/10/16 16:03
 **/
@SpringBootTest
public class ActivitiDiscountTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 部署流程测试
     */
    @Test
    public void testDeploy() {
        //进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/leave_demo.bpmn")
                .addClasspathResource("bpmn/leave_demo.png")
                .name("折扣流程申请")
                .deploy();
        //输出部署的一些信息
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署名称:" + deployment.getName());
    }

    /**
     * 启动流程
     */
    @Test
    public void testStartProcess() {
        //根据流程定义的key启动流程实例,这个key是在定义bpmn的时候设置的
        ProcessInstance instance = runtimeService.
                startProcessInstanceByKey("leave_demo");
        //获取流程实例的相关信息
        System.out.println("流程定义的id = " + instance.getProcessDefinitionId());
        System.out.println("流程实例的id = " + instance.getId());
    }

    /**
     * 任务查询
     * 流程启动后，各个任务的负责人就可以查询自己当前需要处理的任务，查询出来的任务都是该用户的待办任务。
     */
    @Test
    public void testSelectTodoTaskList() {
        //任务负责人
        String assignee = "lisi";
        //获取任务集合
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("leave_demo")

//                .taskAssignee(assignee)
                .list();
        //遍历任务列表
        for (Task task : taskList) {
            System.out.println("流程定义id = " + task.getProcessDefinitionId());
            System.out.println("流程实例id = " + task.getProcessInstanceId());
            System.out.println("任务id = " + task.getId());
            System.out.println("任务名称 = " + task.getName());
            System.out.println("执行人 = " + task.getAssignee());
        }
    }

    /**
     * 审批任务节点 并 添加审批意见
     */
    @Test
    public void testCompleteTask() {
        //任务负责人
        String assignee = "wangwu";
        //获取任务集合
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("leave_demo")
//                .processInstanceId()
                .taskAssignee(assignee)
                .list();
        //遍历任务列表
        for (Task task : taskList) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), "审批通过--测试xx");
            taskService.complete(task.getId());
        }
    }

    /**
     * 查询审批记录
     */
    @Test
    public void testSelectHistoryTask() {
        //流程实例ID
        String processInstanceId = "f8c05d75-6c94-11ee-87d0-30c9aba6c580";
        //任务审核人
        String taskAssignee = "王五";
        //获取历史审核信息
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .activityType("userTask")//只获取用户任务
//                .processInstanceId(processInstanceId)
//                .taskAssignee(taskAssignee)
                .finished()
                .list();
        for (HistoricActivityInstance instance : list) {
            System.out.println("任务名称:" + instance.getActivityName());
            System.out.println("任务开始时间:" + instance.getStartTime());
            System.out.println("任务结束时间:" + instance.getEndTime());
            System.out.println("审批人:" + instance.getAssignee());
            System.out.println("processInstanceId:" + instance.getProcessInstanceId());
            //获取审核批注信息
            List<Comment> taskComments = taskService.getTaskComments(instance.getTaskId());
            if (taskComments.size() > 0) {
                System.out.println("审批批注:" + taskComments.get(0).getFullMessage());
            }
        }
    }

}
