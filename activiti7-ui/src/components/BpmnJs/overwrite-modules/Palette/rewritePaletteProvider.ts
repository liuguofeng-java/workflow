import PaletteProvider from "bpmn-js/lib/features/palette/PaletteProvider";
import ElementFactory from "bpmn-js/lib/features/modeling/ElementFactory.js";
import { assign } from "min-dash";
import { createAction } from "./utils";

class RewritePaletteProvider extends PaletteProvider {
  private readonly _palette: PaletteProvider;
  private readonly _create: any;
  private readonly _elementFactory: ElementFactory;
  private readonly _spaceTool: any;
  private readonly _lassoTool: any;
  private readonly _handTool: any;
  private readonly _globalConnect: any;
  private readonly _translate: any;
  private readonly _moddle: any;
  constructor(palette, create, elementFactory, spaceTool, lassoTool, handTool, globalConnect) {
    super(palette, create, elementFactory, spaceTool, lassoTool, handTool, globalConnect, 2000);
    this._palette = palette;
    this._create = create;
    this._elementFactory = elementFactory;
    this._spaceTool = spaceTool;
    this._lassoTool = lassoTool;
    this._handTool = handTool;
    this._globalConnect = globalConnect;
  }
  getPaletteEntries() {
    const actions = {},
      create = this._create,
      elementFactory = this._elementFactory,
      lassoTool = this._lassoTool,
      handTool = this._handTool,
      globalConnect = this._globalConnect;

    assign(actions, {
      "hand-tool": {
        group: "tools",
        className: "bpmn-icon-hand-tool",
        title: "激活手动工具",
        action: {
          click: function (event) {
            handTool.activateHand(event);
          }
        }
      },
      "lasso-tool": {
        group: "tools",
        className: "bpmn-icon-lasso-tool",
        title: "套索工具",
        action: {
          click: function (event) {
            lassoTool.activateSelection(event);
          }
        }
      },
      "global-connect-tool": {
        group: "tools",
        className: "bpmn-icon-connection-multi",
        title: "全局连线",
        action: {
          click: function (event) {
            globalConnect.toggle(event);
          }
        }
      },
      "create.start-event": createAction(
        elementFactory,
        create,
        "bpmn:StartEvent",
        "events",
        "bpmn-icon-start-event-none",
        "开始事件"
      ),
      "create.end-event": createAction(
        elementFactory,
        create,
        "bpmn:EndEvent",
        "events",
        "bpmn-icon-end-event-none",
        "结束事件"
      ),
      "create.exclusive-gateway": createAction(
        elementFactory,
        create,
        "bpmn:ExclusiveGateway",
        "gateway",
        "bpmn-icon-gateway-none",
        "网关"
      ),
      "create.parallel-gateway": createAction(
        elementFactory,
        create,
        "bpmn:ParallelGateway",
        "gateway",
        "bpmn-icon-gateway-parallel",
        "并行网关"
      ),
      "create.user-task": createAction(
        elementFactory,
        create,
        "bpmn:UserTask",
        "activity",
        "bpmn-icon-user-task",
        "用户任务"
      ),
      "create.service-task": createAction(
        elementFactory,
        create,
        "bpmn:ServiceTask",
        "activity",
        "bpmn-icon-service-task",
        "服务任务"
      )
    });

    return actions;
  }
}

RewritePaletteProvider.$inject = [
  "palette",
  "create",
  "elementFactory",
  "spaceTool",
  "lassoTool",
  "handTool",
  "globalConnect"
];

export default RewritePaletteProvider;
