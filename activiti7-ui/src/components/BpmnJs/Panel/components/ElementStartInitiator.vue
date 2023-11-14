<template>
  <el-collapse-item title="启动器配置" name="Initiator">
    <el-form label-width="100px">
      <el-form-item label="异步前">
        <el-input v-model="initiator" @change="setElementInitiator" />
      </el-form-item>
    </el-form>
  </el-collapse-item>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { getInitiatorValue, setInitiatorValue } from "@/components/BpmnJs/bo-utils/initiatorUtil";
import modeler from "@/components/BpmnJs/store/modeler";
import { Element } from "diagram-js/lib/model/Types";
import EventBus from "@/components/bpmnJs/utils/EventBus";

export default defineComponent({
  name: "ElementStartInitiator",
  setup() {
    const modelerStore = modeler();
    const getActive = computed<Element | null>(() => modelerStore.getActive!);
    const initiator = ref<string | undefined>("");

    const getElementInitiator = () => {
      initiator.value = getInitiatorValue(getActive.value!);
    };
    const setElementInitiator = (value: string | undefined) => {
      setInitiatorValue(getActive.value!, value);
    };

    onMounted(() => {
      getElementInitiator();
      EventBus.on("element-update", getElementInitiator);
    });

    return {
      initiator,
      setElementInitiator
    };
  }
});
</script>
