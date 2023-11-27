<template>
  <div>
    <el-divider content-position="left">常规信息</el-divider>
    <el-form label-width="80px">
      <el-form-item label="ID">
        <el-input v-model="elementId" maxlength="32" @change="updateElementId" />
      </el-form-item>
      <el-form-item label="流程名称">
        <el-input v-model="elementName" maxlength="20" @change="updateElementName" />
      </el-form-item>
      <el-form-item label="介绍">
        <el-input v-model="elementDoc" type="textarea" @change="updateElementDoc" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapState } from "pinia";
import modelerStore from "@/components/BpmnJs/store/modeler";
import { Element } from "diagram-js/lib/model/Types";
import { getNameValue, setNameValue } from "@/components/BpmnJs/bo-utils/nameUtil";
import { setIdValue } from "@/components/BpmnJs/bo-utils/idUtil";
import { getDocumentValue, setDocumentValue } from "@/components/BpmnJs/bo-utils/documentationUtil";
import EventBus from "@/components/BpmnJs/utils/EventBus";

export default defineComponent({
  name: "ElementGenerations",
  data() {
    return {
      elementId: "",
      elementName: "",
      elementDoc: ""
    };
  },
  watch: {
    getActiveId: {
      immediate: true,
      handler() {
        this.elementDoc = getDocumentValue(this.getActive as Element) || "";
      }
    }
  },
  computed: {
    ...mapState(modelerStore, ["getActive", "getActiveId"])
  },
  mounted() {
    this.reloadGenerationData();
    EventBus.on("element-update", this.reloadGenerationData);
  },
  methods: {
    reloadGenerationData() {
      this.elementId = this.getActiveId as string;
      this.elementName = getNameValue(this.getActive as Element) || "";
      this.elementDoc = getDocumentValue(this.getActive as Element) || "";
    },
    updateElementName(value: string) {
      setNameValue(this.getActive as Element, value);
    },
    updateElementId(value: string) {
      setIdValue(this.getActive as Element, value);
    },
    updateElementDoc(value) {
      setDocumentValue(this.getActive as Element, value);
    }
  }
});
</script>
