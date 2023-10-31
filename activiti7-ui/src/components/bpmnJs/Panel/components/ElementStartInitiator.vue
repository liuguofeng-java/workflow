<template>
  <n-collapse-item name="element-start-initiator">
    <template #header>
      <collapse-title :title="$t('panel.startInitiator')">
        <lucide-icon name="PlayCircle" />
      </collapse-title>
    </template>
    <div class="element-start-initiator">
      <edit-item :label="$t('panel.initiator')">
        <n-input v-model:value="initiator" @change="setElementInitiator" />
      </edit-item>
    </div>
  </n-collapse-item>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { getInitiatorValue, setInitiatorValue } from "@/components/bpmnJs/bo-utils/initiatorUtil";
import modeler from "@/components/bpmnJs/store/modeler";
import { Element } from "diagram-js/lib/model/Types";
import EventEmitter from "@/components/bpmnJs/utils/EventEmitter";

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

      EventEmitter.on("element-update", getElementInitiator);
    });

    return {
      initiator,
      setElementInitiator
    };
  }
});
</script>
