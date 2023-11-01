<template>
  <el-card shadow="hover">
    <template #header> 异步控制 </template>
    <el-form label-width="100px">
      <el-form-item label="异步前">
        <el-switch v-model="acBefore" @change="updateElementACBefore" />
      </el-form-item>
      <el-form-item label="异步后">
        <el-switch v-model="acAfter" @change="updateElementACAfter" />
      </el-form-item>
      <el-form-item label="异步优先" v-if="showExclusive">
        <el-switch v-model="acExclusive" @change="updateElementACExclusive" />
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapState } from "pinia";
import { Element } from "diagram-js/lib/model/Types";
import modelerStore from "@/components/bpmnJs/store/modeler";
import { getACAfter, getACBefore, getACExclusive, setACAfter, setACBefore, setACExclusive } from "@/components/bpmnJs/bo-utils/asynchronousContinuationsUtil";
import EventEmitter from "@/components/bpmnJs/utils/EventEmitter";

export default defineComponent({
  name: "ElementAsyncContinuations",
  data() {
    return {
      acBefore: false,
      acAfter: false,
      acExclusive: false
    };
  },
  computed: {
    ...mapState(modelerStore, ["getActive", "getActiveId"]),
    showExclusive() {
      return this.acBefore || this.acAfter;
    }
  },
  mounted() {
    this.reloadACStatus();
    EventEmitter.on("element-update", this.reloadACStatus);
  },
  methods: {
    reloadACStatus() {
      this.acBefore = getACBefore(this.getActive as Element);
      this.acAfter = getACAfter(this.getActive as Element);
      this.acExclusive = getACExclusive(this.getActive as Element);
    },
    updateElementACBefore(value: boolean) {
      setACBefore(this.getActive as Element, value);
      this.reloadACStatus();
    },
    updateElementACAfter(value: boolean) {
      setACAfter(this.getActive as Element, value);
      this.reloadACStatus();
    },
    updateElementACExclusive(value: boolean) {
      setACExclusive(this.getActive as Element, value);
      this.reloadACStatus();
    }
  }
});
</script>
