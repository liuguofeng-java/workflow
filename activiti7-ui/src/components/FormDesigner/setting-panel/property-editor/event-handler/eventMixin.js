import emitter from "@/components/FormDesigner/utils/emitter";
import eventBus from "@/components/FormDesigner/utils/event-bus";

export default {
  mixins: [emitter],
  created() {},
  methods: {
    editEventHandler(eventName, eventParams) {
      this.dispatch("SettingPanel", "editEventHandler", [eventName, [...eventParams]]);
    }
  }
};
