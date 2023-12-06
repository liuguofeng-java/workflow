package com.activiti.modules.listener;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * 执行监听器测试
 *
 * @author liuguofeng
 * @date 2023/12/06 15:07
 **/
public class MyExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("execution.getProcessInstanceId() = " + execution.getProcessInstanceId());
        System.out.println("execution.getEventName() = " + execution.getEventName());
        FlowElement currentFlowElement = execution.getCurrentFlowElement();
        System.out.println("currentFlowElement.getName() = " + currentFlowElement.getName());
        System.out.println("currentFlowElement.getDocumentation() = " + currentFlowElement.getDocumentation());
        System.out.println("currentFlowElement.getExecutionListeners() = " + currentFlowElement.getExecutionListeners());

        String currentActivityId = execution.getCurrentActivityId();
        System.out.println("currentActivityId = " + currentActivityId);
    }
}
