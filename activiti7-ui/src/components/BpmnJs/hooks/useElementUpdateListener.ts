import { onBeforeUnmount, onMounted } from "vue";
import EventBus from "@/components/bpmnJs/utils/EventBus";

export default function (listener: Function) {
  const thisListener = listener;

  const removeListener = () => {
    EventBus.off("element-update", thisListener);
  };

  onMounted(() => {
    thisListener();
    // if (EventEmitter.hasListener("element-update", thisListener)) {
    //   return;
    // }
    EventBus.on("element-update", thisListener);
  });
  onBeforeUnmount(() => removeListener());

  return [thisListener, removeListener];
}
