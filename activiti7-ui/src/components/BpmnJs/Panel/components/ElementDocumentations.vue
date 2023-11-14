<template>
  <el-collapse-item title="文档设置" name="Document">
    <el-form label-width="100px">
      <el-form-item label="ID">
        <el-input v-model="elementDoc" type="textarea" @change="updateElementDoc" />
      </el-form-item>
    </el-form>
  </el-collapse-item>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapState } from "pinia";
import modelerStore from "@/components/BpmnJs/store/modeler";
import { Element } from "diagram-js/lib/model/Types";
import { getDocumentValue, setDocumentValue } from "@/components/BpmnJs/bo-utils/documentationUtil";
import EventBus from "@/components/BpmnJs/utils/EventBus";

export default defineComponent({
  name: "ElementDocumentations",
  data() {
    return {
      elementDoc: ""
    };
  },
  computed: {
    ...mapState(modelerStore, ["getActive", "getActiveId"])
  },
  watch: {
    getActiveId: {
      immediate: true,
      handler() {
        this.elementDoc = getDocumentValue(this.getActive as Element) || "";
      }
    }
  },
  mounted() {
    this.elementDoc = getDocumentValue(this.getActive as Element) || "";
    EventBus.on("element-update", () => {
      this.elementDoc = getDocumentValue(this.getActive as Element) || "";
    });
  },
  methods: {
    updateElementDoc(value) {
      setDocumentValue(this.getActive as Element, value);
    }
  }
});
</script>
