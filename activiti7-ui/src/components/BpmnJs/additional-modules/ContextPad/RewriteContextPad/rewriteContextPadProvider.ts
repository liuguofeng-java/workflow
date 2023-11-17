import ContextPadProvider from "bpmn-js/lib/features/context-pad/ContextPadProvider";
import { Injector } from "didi";
import EventBus from "diagram-js/lib/core/EventBus";
import ContextPad from "diagram-js/lib/features/context-pad/ContextPad";
import Modeling from "bpmn-js/lib/features/modeling/Modeling.js";
import ElementFactory from "bpmn-js/lib/features/modeling/ElementFactory";
import Connect from "diagram-js/lib/features/connect/Connect";
import Create from "diagram-js/lib/features/create/Create";
import PopupMenu from "diagram-js/lib/features/popup-menu/PopupMenu";
import Canvas from "diagram-js/lib/core/Canvas";
import Rules from "diagram-js/lib/features/rules/Rules";
import { Translate } from "diagram-js/lib/i18n/translate";
import { Element } from "diagram-js/lib/model/Types";
import { assign, forEach, isArray, every } from "min-dash";
import { is, isAny } from "bpmn-js/lib/util/ModelUtil";
import { isEventSubProcess, isExpanded } from "bpmn-js/lib/util/DiUtil";
import { getChildLanes } from "bpmn-js/lib/features/modeling/util/LaneUtil";

class RewriteContextPadProvider extends ContextPadProvider {
  private _contextPad: ContextPad;
  private _modeling: Modeling;
  private _elementFactory: ElementFactory;
  private _autoPlace: any;
  private _connect: Connect;
  private _create: Create;
  private _popupMenu: PopupMenu;
  private _canvas: Canvas;
  private _rules: Rules;
  constructor(
    config: any,
    injector: Injector,
    eventBus: EventBus,
    contextPad: ContextPad,
    modeling: Modeling,
    elementFactory: ElementFactory,
    connect: Connect,
    create: Create,
    popupMenu: PopupMenu,
    canvas: Canvas,
    rules: Rules,
    translate: Translate
  ) {
    super(
      config,
      injector,
      eventBus,
      contextPad,
      modeling,
      elementFactory,
      connect,
      create,
      popupMenu,
      canvas,
      rules,
      translate,
      2000
    );

    this._contextPad = contextPad;
    this._modeling = modeling;
    this._elementFactory = elementFactory;
    this._connect = connect;
    this._create = create;
    this._popupMenu = popupMenu;
    this._canvas = canvas;
    this._rules = rules;

    this._autoPlace = injector.get("autoPlace", false);
  }

  getContextPadEntries(element: Element) {
    const contextPad = this._contextPad,
      modeling = this._modeling,
      elementFactory = this._elementFactory,
      connect = this._connect,
      create = this._create,
      popupMenu = this._popupMenu,
      rules = this._rules,
      autoPlace = this._autoPlace,
      translate = this._translate;

    const actions = {};

    if (element.type === "label") {
      return actions;
    }

    const businessObject = element.businessObject;

    function startConnect(event, element) {
      connect.start(event, element);
    }

    function removeElement(e, element) {
      modeling.removeElements([element]);
    }

    /**
     * Create an append action.
     *
     * @param {string} type
     * @param {string} className
     * @param {string} [title]
     * @param {Object} [options]
     *
     * @return {ContextPadEntry}
     */
    function appendAction(type, className, title, options) {
      if (typeof title !== "string") {
        options = title;
        title = translate("Append {type}", { type: type.replace(/^bpmn:/, "") });
      }

      function appendStart(event, element) {
        const shape = elementFactory.createShape(assign({ type: type }, options));
        create.start(event, shape, {
          source: element
        });
      }

      const append = autoPlace
        ? function (event, element) {
            const shape = elementFactory.createShape(assign({ type: type }, options));

            autoPlace.append(element, shape);
          }
        : appendStart;

      return {
        group: "模型",
        className: className,
        title: title,
        action: {
          dragstart: appendStart,
          click: append
        }
      };
    }

    function splitLaneHandler(count) {
      return function (_, element) {
        // actual split
        modeling.splitLane(element, count);

        // refresh context pad after split to
        // get rid of split icons
        contextPad.open(element, true);
      };
    }

    if (isAny(businessObject, ["bpmn:Lane", "bpmn:Participant"]) && isExpanded(element)) {
      const childLanes = getChildLanes(element);

      assign(actions, {
        "lane-insert-above": {
          group: "lane-insert-above",
          className: "bpmn-icon-lane-insert-above",
          title: translate("Add Lane above"),
          action: {
            click: function (event, element) {
              modeling.addLane(element, "top");
            }
          }
        }
      });

      if (childLanes.length < 2) {
        if (element.height >= 120) {
          assign(actions, {
            "lane-divide-two": {
              group: "lane-divide",
              className: "bpmn-icon-lane-divide-two",
              title: translate("Divide into two Lanes"),
              action: {
                click: splitLaneHandler(2)
              }
            }
          });
        }

        if (element.height >= 180) {
          assign(actions, {
            "lane-divide-three": {
              group: "lane-divide",
              className: "bpmn-icon-lane-divide-three",
              title: translate("Divide into three Lanes"),
              action: {
                click: splitLaneHandler(3)
              }
            }
          });
        }
      }

      assign(actions, {
        "lane-insert-below": {
          group: "lane-insert-below",
          className: "bpmn-icon-lane-insert-below",
          title: translate("Add Lane below"),
          action: {
            click: function (event, element) {
              modeling.addLane(element, "bottom");
            }
          }
        }
      });
    }

    if (is(businessObject, "bpmn:FlowNode")) {
      if (is(businessObject, "bpmn:EventBasedGateway")) {
        assign(actions, {
          "append.receive-task": appendAction(
            "bpmn:ReceiveTask",
            "bpmn-icon-receive-task",
            translate("Append ReceiveTask"),
            undefined
          ),
          "append.message-intermediate-event": appendAction(
            "bpmn:IntermediateCatchEvent",
            "bpmn-icon-intermediate-event-catch-message",
            translate("Append MessageIntermediateCatchEvent"),
            { eventDefinitionType: "bpmn:MessageEventDefinition" }
          ),
          "append.timer-intermediate-event": appendAction(
            "bpmn:IntermediateCatchEvent",
            "bpmn-icon-intermediate-event-catch-timer",
            translate("Append TimerIntermediateCatchEvent"),
            { eventDefinitionType: "bpmn:TimerEventDefinition" }
          ),
          "append.condition-intermediate-event": appendAction(
            "bpmn:IntermediateCatchEvent",
            "bpmn-icon-intermediate-event-catch-condition",
            translate("Append ConditionIntermediateCatchEvent"),
            { eventDefinitionType: "bpmn:ConditionalEventDefinition" }
          ),
          "append.signal-intermediate-event": appendAction(
            "bpmn:IntermediateCatchEvent",
            "bpmn-icon-intermediate-event-catch-signal",
            translate("Append SignalIntermediateCatchEvent"),
            { eventDefinitionType: "bpmn:SignalEventDefinition" }
          )
        });
      } else if (
        isEventType(businessObject, "bpmn:BoundaryEvent", "bpmn:CompensateEventDefinition")
      ) {
        assign(actions, {
          "append.compensation-activity": appendAction(
            "bpmn:Task",
            "bpmn-icon-task",
            translate("Append compensation activity"),
            {
              isForCompensation: true
            }
          )
        });
      } else if (
        !is(businessObject, "bpmn:EndEvent") &&
        !businessObject.isForCompensation &&
        !isEventType(businessObject, "bpmn:IntermediateThrowEvent", "bpmn:LinkEventDefinition") &&
        !isEventSubProcess(businessObject)
      ) {
        assign(actions, {
          "append.append-user-task": appendAction(
            "bpmn:UserTask",
            "bpmn-icon-user-task",
            translate("Create User Task"),
            undefined
          ),
          "append.end-event": appendAction(
            "bpmn:EndEvent",
            "bpmn-icon-end-event-none",
            translate("Append EndEvent"),
            undefined
          ),
          "append.gateway": appendAction(
            "bpmn:ExclusiveGateway",
            "bpmn-icon-gateway-none",
            translate("Append Gateway"),
            undefined
          )
        });
      }
    }

    if (
      isAny(businessObject, [
        "bpmn:FlowNode",
        "bpmn:InteractionNode",
        "bpmn:DataObjectReference",
        "bpmn:DataStoreReference"
      ])
    ) {
      assign(actions, {
        connect: {
          group: "连接",
          className: "bpmn-icon-connection-multi",
          title: translate(
            "Connect using " +
              (businessObject.isForCompensation ? "" : "Sequence/MessageFlow or ") +
              "Association"
          ),
          action: {
            click: startConnect,
            dragstart: startConnect
          }
        }
      });
    }

    if (is(businessObject, "bpmn:TextAnnotation")) {
      assign(actions, {
        connect: {
          group: "连接",
          className: "bpmn-icon-connection-multi",
          title: translate("Connect using Association"),
          action: {
            click: startConnect,
            dragstart: startConnect
          }
        }
      });
    }

    if (isAny(businessObject, ["bpmn:DataObjectReference", "bpmn:DataStoreReference"])) {
      assign(actions, {
        connect: {
          group: "连接",
          className: "bpmn-icon-connection-multi",
          title: translate("Connect using DataInputAssociation"),
          action: {
            click: startConnect,
            dragstart: startConnect
          }
        }
      });
    }

    if (is(businessObject, "bpmn:Group")) {
      assign(actions, {
        "append.text-annotation": appendAction(
          "bpmn:TextAnnotation",
          "bpmn-icon-text-annotation",
          undefined,
          undefined
        )
      });
    }

    // delete element entry, only show if allowed by rules
    let deleteAllowed = rules.allowed("elements.delete", { elements: [element] });

    if (isArray(deleteAllowed)) {
      // was the element returned as a deletion candidate?
      deleteAllowed = deleteAllowed[0] === element;
    }

    if (deleteAllowed) {
      assign(actions, {
        delete: {
          group: "编辑",
          className: "bpmn-icon-trash",
          title: translate("Remove"),
          action: {
            click: removeElement
          }
        }
      });
    }

    return actions;
  }
}

// helpers /////////

function isEventType(eventBo, type, definition) {
  const isType = eventBo.$instanceOf(type);
  let isDefinition = false;

  const definitions = eventBo.eventDefinitions || [];
  forEach(definitions, function (def) {
    if (def.$type === definition) {
      isDefinition = true;
    }
  });

  return isType && isDefinition;
}

export default RewriteContextPadProvider;
