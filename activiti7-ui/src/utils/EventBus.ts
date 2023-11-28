import mitt from "mitt";

const instance = mitt();
const eventBus: any = {};
eventBus.on = instance.on;
eventBus.off = instance.off;
eventBus.emit = instance.emit;

export default eventBus;
