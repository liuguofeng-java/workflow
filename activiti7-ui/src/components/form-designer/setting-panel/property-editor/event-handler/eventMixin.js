import emitter from '@/components/form-designer/utils/emitter'
import eventBus from "@/components/form-designer/utils/event-bus"

export default {
  mixins: [emitter],
  created() {},
  methods: {
    editEventHandler(eventName, eventParams) {
      this.dispatch('SettingPanel', 'editEventHandler', [eventName, [...eventParams]])
    },

  }
}
