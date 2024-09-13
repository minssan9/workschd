<template>
  <el-row>
    <DashBoard/>
  </el-row>
  <el-divider/>

  <el-tabs
      v-model="activeName"
      class="home__tabs"
      type="border-card"
      @tab-click="handleClick"
  >
    <el-tab-pane :label="t('home.summaryCard')" name="SummaryCard">
      <SummaryCard v-if="activeName === 'SummaryCard'" />
    </el-tab-pane>
    <el-tab-pane :label="t('home.equipmentDiagList')" name="DiagList">
      <DiagList v-if="activeName === 'DiagList'" />
    </el-tab-pane>
    <el-tab-pane :label="t('home.alarmList')" name="AlarmList">
      <AlarmList v-if="activeName === 'AlarmList'" />
    </el-tab-pane>
    <el-tab-pane :label="t('home.systemconnection')" name="InterfaceInfo">
      <InterfaceInfo v-if="activeName === 'InterfaceInfo'"/>
    </el-tab-pane>
  </el-tabs>
</template>


<script setup lang="ts">
import {onMounted, ref} from 'vue'
import axios from "axios";
import type {TabsPaneContext} from 'element-plus'

import DashBoard from '@/views/home/DashBoard_Top.vue';
import DiagList from '@/views/home/tab/DiagList.vue';
import AlarmList from '@/views/home/tab/AlarmList.vue';
import InterfaceInfo from '@/views/home/systemConnection/InterfaceInfo.vue';
import SummaryCard from "@/views/home/tab/SummaryCard.vue";

import {useI18n} from 'vue-i18n';/* i18n */
const { t } = useI18n()


const activeName = ref('SummaryCard')

onMounted(() => {
});

const handleClick = (tab: TabsPaneContext, event: Event) => {
  activeName.value = String(tab.props.name);
  console.log(tab, event)
  if(activeName.value === 'AlarmList') {
    sendAlarmTemp();
  }
}

const data = ref(
  { 
    COMPANY: "HMGMA",
    SENDER: "PHM",
    IFID: "PMHMGMA003",
    IT_DATA: [{
      ALARM_NO: "HMGMA00002",
      SWERK: "1401",
      EQUNR: "10000001",
      ALARM_TEXT: "Welding robot malfunction",
      ALARM_LTEXT: "Welding robot malfunction detail",
      ALARM_DATE: "2024-06-04",
      ALARM_TIME: "163200"
    }]
  }
);

async function sendAlarmTemp() {
  try {
    const response = await axios({
      method: "POST",
      url:
        "http://10.136.19.73:5510/rad/SapPMHMGMA.PHM.RESOURCE:PHM_RAD/ZFPMVP_PHM_NOTI",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic cGhtcmVzdDpwaG1yZXN0QDEyMw==",
      },
      data: data.value,
    });
    const responseData = response.data;
    console.log("알람 전송 결과:", responseData);
  } catch (error) {
    console.error("Failed to Send Alarm API :", error);
  }
}


</script>
<style lang="scss" >
@import "@/style/layout/_home.scss";
</style>