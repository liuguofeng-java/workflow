<template>
  <div>
    <el-divider content-position="left" v-if="scopedElement?.type === 'bpmn:StartEvent'">主表单</el-divider>
    <el-divider content-position="left" v-if="scopedElement?.type === 'bpmn:UserTask'">动态表单</el-divider>

    <el-card shadow="hover">
      <el-tree ref="nodeTree" :data="nodeTreeData" node-key="id" default-expand-all highlight-current class="node-tree" icon-class="el-icon-arrow-right" :expand-on-click-node="false">
        <template #default="{ node, data }">
          <div>
            <svg-icon v-if="data.icon" :icon-class="data.icon" class-name="color-svg-icon" />
            {{ i18n.methods.i18n2t(`designer.widgetLabel.${data.type}`, `extension.widgetLabel.${data.type}`) }}
            <span> >>>> {{ node.label }}</span>
          </div>
        </template>
      </el-tree>
    </el-card>

    <div style="width: 100%">
      <el-button type="primary" plain style="width: 100%" @click="selectFormRef.handleOpen()">设计</el-button>
    </div>
    <SelectForm ref="selectFormRef" :formJson="formJson" @ok="selectFormOk" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import modelerStore from "@/components/BpmnJs/store/modeler";
import SelectForm from "./SubChild/SelectForm.vue";
import EventBus from "@/utils/EventBus";
import catchUndefElement from "@/components/BpmnJs/utils/CatchUndefElement";
import { type Element } from "bpmn-js/lib/util/ModelUtil";
import i18n from "@/components/FormDesigner/utils/i18n";
import SvgIcon from "@/components/FormDesigner/svg-icon/index.vue";

const modeler = modelerStore();

// 当前节点信息
let scopedElement: Element;

// 树节点
let nodeTreeData = ref<any[]>([]);

// 选择表单
const selectFormRef = ref();

// 当前表单结构数据
let formJson = ref<any>({});

// 编辑表单成功
const selectFormOk = (data: any) => {
  modeler.setFormJson({
    activityId: scopedElement.id,
    formJson: data,
    isMainFrom: scopedElement.type === "bpmn:StartEvent" ? 1 : 0
  });
  getElementData();
};

/**
 * 获取节点表单数据
 */
const getElementData = () => {
  formJson.value = modeler.getFormJsonList.find((t) => t.activityId === scopedElement.id)?.formJson || {};
  nodeTreeData.value.length = 0;
  formJson.value.widgetList?.forEach((wItem) => {
    buildTreeNodeOfWidget(wItem, nodeTreeData.value);
  });
};

/**
 * 生成树节点
 * @param widget 当前节点
 * @param treeNode 全部节点
 */
const buildTreeNodeOfWidget = (widget, treeNode) => {
  let curNode: any = {
    id: widget.id,
    type: widget.type,
    icon: widget.icon,
    label: widget.options.label || widget.type
  };
  treeNode.push(curNode);

  if (widget.category === undefined) {
    return;
  }

  curNode.children = [];
  if (widget.type === "grid") {
    widget.cols.map((col) => {
      let colNode = {
        id: col.id,
        type: col.type,
        icon: col.icon,
        label: col.options.name || widget.type,
        children: []
      };
      curNode.children.push(colNode);
      col.widgetList.map((wChild) => {
        buildTreeNodeOfWidget(wChild, colNode.children);
      });
    });
  } else if (widget.type === "table") {
    //TODO: 需要考虑合并单元格！！
    widget.rows.map((row) => {
      let rowNode = {
        id: row.id,
        type: row.type,
        icon: row.icon,
        label: "table-row",
        selectable: false,
        children: []
      };
      curNode.children.push(rowNode);

      row.cols.map((cell) => {
        if (cell.merged) {
          //跳过合并单元格！！
          return;
        }

        let rowChildren: any = rowNode.children;
        let cellNode: any = {
          id: cell.id,
          type: cell.type,
          icon: cell.icon,
          label: "table-cell",
          children: []
        };
        rowChildren.push(cellNode);

        cell.widgetList.map((wChild) => {
          buildTreeNodeOfWidget(wChild, cellNode.children);
        });
      });
    });
  } else if (widget.type === "tab") {
    widget.tabs.map((tab) => {
      let tabNode = {
        id: tab.id,
        type: tab.type,
        icon: tab.icon,
        label: tab.options.name || widget.type,
        selectable: false,
        children: []
      };
      curNode.children.push(tabNode);
      tab.widgetList.map((wChild) => {
        buildTreeNodeOfWidget(wChild, tabNode.children);
      });
    });
  } else if (widget.type === "sub-form") {
    widget.widgetList.map((wChild) => {
      buildTreeNodeOfWidget(wChild, curNode.children);
    });
  } else if (widget.category === "container") {
    //自定义容器
    widget.widgetList.map((wChild) => {
      buildTreeNodeOfWidget(wChild, curNode.children);
    });
  }
};

// 点击用户节点，初始化用
EventBus.on("element-init", function () {
  catchUndefElement((element) => {
    scopedElement = element;
    getElementData();
  });
});
</script>

<style scoped lang="scss">
:deep(.node-tree) {
  .el-tree > .el-tree-node:after {
    border-top: none;
  }
  .el-tree-node {
    position: relative;
    padding-left: 12px;
  }

  .el-tree-node__content {
    padding-left: 0 !important;
  }

  .el-tree-node__expand-icon.is-leaf {
    display: none;
  }

  .el-tree-node__children {
    padding-left: 12px;
    overflow: visible !important; /* 加入此行让el-tree宽度自动撑开，超出宽度el-draw自动出现水平滚动条！ */
  }

  .el-tree-node :last-child:before {
    height: 38px;
  }

  .el-tree > .el-tree-node:before {
    border-left: none;
  }

  .el-tree > .el-tree-node:after {
    border-top: none;
  }

  .el-tree-node:before {
    content: "";
    left: -4px;
    position: absolute;
    right: auto;
    border-width: 1px;
  }

  .el-tree-node:after {
    content: "";
    left: -4px;
    position: absolute;
    right: auto;
    border-width: 1px;
  }

  .el-tree-node:before {
    border-left: 1px dashed #4386c6;
    bottom: 0px;
    height: 100%;
    top: -10px;
    width: 1px;
  }

  .el-tree-node:after {
    border-top: 1px dashed #4386c6;
    height: 20px;
    top: 12px;
    width: 16px;
  }

  .el-tree-node.is-current > .el-tree-node__content {
    background: white !important;
  }

  .el-tree-node__expand-icon {
    margin-left: -3px;
    padding: 6px 6px 6px 0px;
    font-size: 16px;
  }
  .el-tree-node__content:hover {
    background-color: white;
  }
}
</style>
