<template>
  <el-collapse-item title="常规信息" name="NormalInfo">
    <el-form label-width="100px">
      <el-form-item label="ID">
        <el-input v-model="elementId" maxlength="32" @change="updateElementId" />
      </el-form-item>
      <el-form-item label="流程名称">
        <el-input v-model="elementName" maxlength="20" @change="updateElementName" />
      </el-form-item>
      <!-- <template v-if="isProcess">
        <el-form-item label="版本号" key="version">
          <el-input v-model="elementVersion" maxlength="20" @change="updateElementVersion" />
        </el-form-item>
        <el-form-item label="可执行" key="executable">
          <el-switch v-model="elementExecutable" @change="updateElementExecutable" />
        </el-form-item>
      </template> -->
    </el-form>
  </el-collapse-item>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapState } from "pinia";
import modelerStore from "@/components/BpmnJs/store/modeler";
import { Element } from "diagram-js/lib/model/Types";
import { getNameValue, setNameValue } from "@/components/BpmnJs/bo-utils/nameUtil";
import { setIdValue } from "@/components/BpmnJs/bo-utils/idUtil";
import { getProcessExecutable, getProcessVersionTag, setProcessExecutable, setProcessVersionTag } from "@/components/BpmnJs/bo-utils/processUtil";
import EventEmitter from "@/components/BpmnJs/utils/EventEmitter";
import { ElMessage } from "element-plus";

export default defineComponent({
  name: "ElementGenerations",
  data() {
    return {
      elementId: "",
      elementName: "",
      elementVersion: "",
      elementExecutable: true,
      isProcess: false
    };
  },
  computed: {
    ...mapState(modelerStore, ["getActive", "getActiveId"])
  },
  mounted() {
    this.reloadGenerationData();
    EventEmitter.on("element-update", this.reloadGenerationData);
  },
  methods: {
    reloadGenerationData() {
      this.isProcess = !!this.getActive && this.getActive.type === "bpmn:Process";
      this.elementId = this.getActiveId as string;
      this.elementName = getNameValue(this.getActive as Element) || "";
      if (this.isProcess) {
        this.elementExecutable = getProcessExecutable(this.getActive as Element);
        this.elementVersion = getProcessVersionTag(this.getActive as Element) || "";
      }
    },
    updateElementName(value: string) {
      setNameValue(this.getActive as Element, value);
    },
    updateElementId(value: string) {
      setIdValue(this.getActive as Element, value);
    },
    updateElementVersion(value: string) {
      const reg = /((\d|([1-9](\d*))).){2}(\d|([1-9](\d*)))/;
      if (reg.test(value)) {
        setProcessVersionTag(this.getActive as Element, value);
      } else {
        ElMessage.error("版本号必须符合语义化版本2.0.0 要点");
      }
    },
    updateElementExecutable(value: boolean) {
      setProcessExecutable(this.getActive as Element, value);
    }
  }
});
</script>
