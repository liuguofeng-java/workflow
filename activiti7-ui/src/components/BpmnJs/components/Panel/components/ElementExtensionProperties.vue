<template>
  <div v-if="false">
    <el-divider content-position="left">扩展属性</el-divider>
    <el-table :data="extensions" style="width: 100%">
      <el-table-column type="index" label="序号" />
      <el-table-column prop="name" label="Name" />
      <el-table-column prop="value" label="Value" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button link type="primary" @click="removeProperty(scope.row.index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button type="primary" @click="openPropertyModel">添加</el-button>

    <el-dialog v-model="modelVisible" title="添加" width="500px" append-to-body>
      <el-form ref="formRef" :model="newProperty" :rules="rules" label-width="80px">
        <el-form-item label="name" prop="name">
          <el-input v-model="newProperty.name" @keydown.enter.prevent />
        </el-form-item>
        <el-form-item label="value" prop="value">
          <el-input v-model="newProperty.value" @keydown.enter.prevent />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="addProperty">确 定</el-button>
          <el-button @click="modelVisible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRaw, markRaw } from "vue";
import { mapState } from "pinia";
import modelerStore from "@/components/BpmnJs/store/modeler";
import { Element } from "diagram-js/lib/model/Types";
import { addExtensionProperty, getExtensionProperties, removeExtensionProperty } from "@/components/BpmnJs/bo-utils/extensionPropertiesUtil";
import EventBus from "@/utils/EventBus";

export default defineComponent({
  name: "ElementExtensionProperties",
  data() {
    return {
      extensions: [],
      extensionsRaw: [],
      newProperty: { name: "", value: "" },
      rules: {
        name: { required: true, message: "属性名称不能为空", trigger: ["blur", "change"] },
        value: { required: true, message: "属性值不能为空", trigger: ["blur", "change"] }
      },
      modelVisible: false
    };
  },
  computed: {
    ...mapState(modelerStore, ["getActive", "getActiveId"])
  },
  watch: {
    getActiveId: {
      immediate: true,
      handler() {
        this.reloadExtensionProperties();
      }
    }
  },
  mounted() {
    this.reloadExtensionProperties();
    EventBus.on("element-update", this.reloadExtensionProperties);
  },
  methods: {
    async reloadExtensionProperties() {
      this.modelVisible = false;
      await this.$nextTick();
      this.newProperty = { name: "", value: "" };
      (this.extensionsRaw as any[]) = markRaw(getExtensionProperties(this.getActive as Element));
      this.extensions = JSON.parse(JSON.stringify(this.extensionsRaw));
    },
    removeProperty(propIndex: number) {
      removeExtensionProperty(this.getActive as Element, this.extensionsRaw[propIndex]);
      this.reloadExtensionProperties();
    },
    addProperty() {
      (this.$refs.formRef as any).validate((valid: boolean) => {
        if (!valid) return;
        addExtensionProperty(this.getActive as Element, toRaw(this.newProperty));
        this.reloadExtensionProperties();
      });
    },
    async openPropertyModel() {
      this.modelVisible = true;
      await this.$nextTick();
      (this.$refs.formRef as any).resetFields();
    }
  }
});
</script>
<style scoped></style>
