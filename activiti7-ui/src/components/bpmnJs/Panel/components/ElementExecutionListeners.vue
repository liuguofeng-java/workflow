<template>
  <el-collapse-item title="执行监听器" name="Listener">
    <el-table :data="listeners" style="width: 100%">
      <el-table-column type="index" label="序号" />
      <el-table-column prop="event" label="事件类型" />
      <el-table-column prop="type" label="监听器类型" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button link type="primary" @click="openListenerModel(scope.$index, scope.row)">修改</el-button>
          <el-button link type="primary" @click="removeListener(scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button type="primary" @click="openListenerModel(-1, null)">添加</el-button>
  </el-collapse-item>

  <el-dialog v-model="modelVisible" title="添加" width="500px">
    <el-form ref="formRef" :model="newListener" :rules="formRules" label-width="80px">
      <el-form-item label="事件类型" prop="event">
        <el-select v-model="newListener.event">
          <el-option v-for="item in listenerEventTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="监听器类型" prop="type">
        <el-input disabled v-model="newListener.type" value="class" />
      </el-form-item>

      <el-form-item label="java类" prop="class">
        <el-input v-model="newListener.class" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="saveExecutionListener">确 定</el-button>
        <el-button @click="modelVisible = false">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, markRaw, nextTick } from "vue";
import { mapState } from "pinia";
import modelerStore from "@/components/bpmnJs/store/modeler";
import { ModdleElement } from "bpmn-moddle";
import { Element } from "diagram-js/lib/model/Types";
import { addExecutionListener, getDefaultEvent, getExecutionListeners, getExecutionListenerType, removeExecutionListener, updateExecutionListener } from "@/components/bpmnJs/bo-utils/executionListenersUtil";
import { getScriptType } from "@/components/bpmnJs/bo-utils/scriptUtil";
import EventEmitter from "@/components/bpmnJs/utils/EventEmitter";

export default defineComponent({
  name: "ElementExecutionListeners",
  data() {
    return {
      activeIndex: -1,
      listeners: [],
      listenerEventTypeOptions: [
        { label: "Start", value: "start" },
        { label: "End", value: "end" }
      ],
      listenerTypeOptions: [{ label: "Java Class", value: "class" }],
      formRules: {
        event: { required: true, trigger: ["blur", "change"], message: "事件类型不能为空" },
        type: { required: true, trigger: ["blur", "change"], message: "监听器类型不能为空" },
        class: { required: true, trigger: ["blur", "change"], message: "java类不能为空" }
      },
      newListener: { event: "", type: "class", class: "" },
      modelVisible: false,
      listenersRaw: []
    };
  },
  computed: {
    ...mapState(modelerStore, ["getActive", "getActiveId"])
  },
  mounted() {
    this.reloadExtensionListeners();
    EventEmitter.on("element-update", this.reloadExtensionListeners);
  },
  methods: {
    reloadExtensionListeners() {
      this.modelVisible = false;
      this.newListener = { event: getDefaultEvent(this.getActive), type: "class", class: "" };
      (this.listenersRaw as ModdleElement[]) = markRaw(getExecutionListeners(this.getActive as Element));
      const list = this.listenersRaw.map(
        (item: ModdleElement & BpmnExecutionListener): ExecutionListenerForm => ({
          ...item,
          ...(item.script
            ? {
                script: {
                  ...item.script,
                  scriptType: getScriptType(item.script as ModdleElement & BpmnScript)
                }
              }
            : {}),
          type: getExecutionListenerType(item)
        })
      );
      this.listeners = JSON.parse(JSON.stringify(list));
    },
    removeListener(index: number) {
      const listener: ModdleElement = this.listenersRaw[index];
      removeExecutionListener(this.getActive, listener);
      this.reloadExtensionListeners();
    },
    async saveExecutionListener() {
      await (this.$refs.formRef as any).validate();
      console.log(this.newListener);
      this.activeIndex === -1 ? addExecutionListener(this.getActive, this.newListener) : updateExecutionListener(this.getActive, this.newListener, this.listenersRaw[this.activeIndex]);
      this.reloadExtensionListeners();
    },
    async openListenerModel(index: number, listenerData) {
      this.activeIndex = index;
      console.log(JSON.stringify(listenerData));
      listenerData && (this.newListener = JSON.parse(JSON.stringify(listenerData)));
      this.modelVisible = true;
      await nextTick();
      (this.$refs.formRef as any).formRef && (this.$refs.formRef as any).resetFields();
    }
  }
});
</script>
