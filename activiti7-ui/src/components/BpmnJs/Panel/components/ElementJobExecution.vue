<template>
  <el-collapse-item title="执行作业" name="ExecuteJob">
    <el-form label-width="100px">
      <div class="element-external-task">
        <el-form-item label="任务优先级" v-if="tpVisible">
          <el-input v-model="taskPriority" maxlength="32" @change="setExternalTaskPriority" />
        </el-form-item>
        <el-form-item label="重试周期" v-if="rtVisible">
          <el-input v-model="retryTimeCycle" maxlength="32" @change="setRetryTimeCycle" />
        </el-form-item>
      </div>
    </el-form>
  </el-collapse-item>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import modeler from "@/components/BpmnJs/store/modeler";
import { getExternalTaskValue, getRetryTimeCycleValue, retryTimeCycleVisible, setExternalTaskValue, setRetryTimeCycleValue, taskPriorityVisible } from "@/components/BpmnJs/bo-utils/jobExecutionUtil";
import { Element } from "diagram-js/lib/model/Types";
import EventBus from "@/components/bpmnJs/utils/EventBus";

export default defineComponent({
  name: "ElementJobExecution",
  setup() {
    const modelerStore = modeler();
    const getActive = computed<Element | null>(() => modelerStore.getActive!);
    const getActiveId = computed<string>(() => modelerStore.getActiveId!);

    const retryTimeCycle = ref<string | undefined>(undefined);
    const rtVisible = ref<boolean>(false);
    const getRetryTimeCycle = () => {
      rtVisible.value = retryTimeCycleVisible(getActive.value!);
      retryTimeCycle.value = getRetryTimeCycleValue(getActive.value!) || "";
    };
    const setRetryTimeCycle = (value: string | undefined) => {
      setRetryTimeCycleValue(getActive.value!, value);
    };

    const taskPriority = ref<string | undefined>(undefined);
    const tpVisible = ref<boolean>(false);
    const getExternalTaskPriority = () => {
      tpVisible.value = taskPriorityVisible(getActive.value!);
      taskPriority.value = getExternalTaskValue(getActive.value!) || "";
    };
    const setExternalTaskPriority = (value: string | undefined) => {
      setExternalTaskValue(getActive.value!, value);
    };

    watch(
      () => getActiveId.value,
      () => {
        getRetryTimeCycle();
        getExternalTaskPriority();
      },
      { immediate: true }
    );

    onMounted(() => {
      getRetryTimeCycle();
      getExternalTaskPriority();

      EventBus.on("element-update", () => {
        getRetryTimeCycle();
        getExternalTaskPriority();
      });
    });

    return {
      retryTimeCycle,
      rtVisible,
      setRetryTimeCycle,
      taskPriority,
      tpVisible,
      setExternalTaskPriority
    };
  }
});
</script>
