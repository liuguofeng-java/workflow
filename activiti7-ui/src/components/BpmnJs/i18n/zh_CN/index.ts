import tasks from "./elements/tasks";
import events from "./elements/events";
import gateway from "./elements/gateway";
import other from "./elements/other";
import lint from "./lint";

export default {
  elements: {
    ...other,
    ...events,
    ...gateway,
    ...tasks
  },
  lint
};
