<template>
  <div>
    <el-drawer v-model="open" title="发起流程" size="600px" append-to-body>
      <el-alert title="需要注意的事项" type="success" :closable="false">
        <template v-slot:title>
          <div>1.业务key businessKey的作用:可以提供businessKey来将流程实例与具有明确业务含义的某个标识符关联起来。例如，在订单流程中，业务键可以是订单id。然后可以使用该业务键轻松地查找流程实例.</div>
          <div>2.当然一个订单业务可以有多个流程实例,多个流程实例绑定一个businessKey,假设一个流程实例在某一个节点结束了,但是这个实例没有通过审核,那我们可以重新起一个流程实例,查询历史记录时可以根据businessKey来查找</div>
          <div>3.主表单 比如:张三要请5天假,那么张三就要填写:请假天数、请假类型、请假理由等信息,那么这里的主表单就要选择请假相关表单和流程,在流程定义中可以设置成流程条件</div>
        </template>
      </el-alert>
      <br />
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="业务key" prop="businessKey">
          <el-input v-model="form.businessKey" placeholder="businessKey就是业务key" />
        </el-form-item>

        <el-form-item label="选择流程" prop="definitionId" class="disabled-color">
          <el-input v-model="form.definitionName" disabled>
            <template #append>
              <el-button :icon="Search" @click="selectProcess.init()" />
            </template>
          </el-input>
        </el-form-item>
      </el-form>

      <el-card class="box-card" v-if="Object.keys(form.formJson).length !== 0">
        <template #header>
          <div class="card-header">
            <span>主表单</span>
          </div>
        </template>
        <!-- 节点动态表单 -->
        <VFormRender ref="preForm" :preview-state="true" />
      </el-card>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="open = false">取 消</el-button>
        </div>
      </template>
    </el-drawer>
    <SelectProcess ref="selectProcess" @ok="selectProcessOk" />
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, toRef, nextTick } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
import SelectProcess from "./SelectProcess.vue";
import VFormRender from "@/components/FormDesigner/form-render/index.vue";

import { Search } from "@element-plus/icons-vue";

// 是否打开弹出框
const open = ref(false);

// 选择流程
const selectProcess = ref();

// 动态表单实例
const preForm = ref();

// 表单实例
const formRef = ref();
// 提交表单数据
let form = toRef(
  reactive({
    businessKey: "",
    formId: "",
    formName: "",
    formJson: {},
    definitionId: "",
    definitionName: ""
  })
);
// 表单验证
const rules = ref({
  businessKey: [{ required: true, message: "业务key不能为空", trigger: "blur" }],
  definitionId: [{ required: true, message: "流程必须选择", trigger: "blur" }]
});

// 初始化
const init = () => {
  open.value = true;
  form.value = {
    businessKey: "",
    formId: "",
    formName: "",
    formJson: {},
    definitionId: "",
    definitionName: ""
  };
  nextTick(() => {
    preForm.value?.setFormJson({});
    formRef.value?.resetFields();
  });
};

/**
 * 选择流程返回数据
 * @param data 表单数据
 */
const selectProcessOk = (data: any) => {
  console.log(data);

  form.value.definitionId = data.id;
  form.value.definitionName = data.name;
  form.value.formJson = data.formJson || {};
  nextTick(() => {
    preForm.value?.setFormJson(form.value.formJson);
  });
};

/**
 * 提交按钮
 */
function submitForm() {
  // 验证表单
  formRef.value.validate(async (valid: boolean) => {
    // 获取动态表单数据
    const formData = (await preForm.value?.getFormData()) || {};
    if (!valid) return;

    // 真实要提交的数据
    const subForm = {
      businessKey: form.value.businessKey,
      definitionId: form.value.definitionId,
      variables: {}
    };
    // 设置流程变量
    subForm.variables = JSON.parse(JSON.stringify(formData));

    // 由于回显使用
    subForm.variables[`${form.value.businessKey}_formData`] = JSON.parse(JSON.stringify(formData));
    subForm.variables[`${form.value.businessKey}_formJson`] = form.value.formJson;
    baseService.post(`/processStart/start`, subForm).then((res) => {
      if (res.code === 200) {
        ElMessage.success(res.msg);
        open.value = false;
        emit("ok");
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
}

const emit = defineEmits<{
  (event: "ok"): void;
}>();

defineExpose({
  init
});
</script>

<style scoped>
.disabled-color :deep() .is-disabled > .el-input__wrapper {
  background-color: var(--el-input-bg-color) !important;
}
.disabled-color :deep() .is-disabled > .el-input__wrapper > input {
  -webkit-text-fill-color: var(--el-input-text-color) !important;
}
</style>
