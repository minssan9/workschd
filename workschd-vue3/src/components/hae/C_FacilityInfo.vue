<template>
  <el-card body-class="detail-item-ce common common-info">
    <el-row>
      <el-col :span="6">
        <el-text>{{ t('facility.info') }} </el-text>
      </el-col>
      <el-col :span="18">
        <el-row justify="end" class="button-row">
          <el-button @click="handleRegisterInterface">{{ t('event.edit') }}</el-button>
          <el-button @click="handleEquipmentHistory">{{ t('event.history') }}</el-button>
          <el-button @click="handleReport">{{ t('event.report') }}</el-button>
          <el-button @click="handleAddEdgeCmsDialog">Add CMS</el-button>
          <el-button @click="handleEditTagInfo">{{ t('event.tag') }}</el-button>
        </el-row>
      </el-col>
    </el-row>
    <el-divider border-style="dotted" />

    <el-table class="detail-view__table"
              header-cell-class-name="table-header"
              border
              :data="equipmentData">
      <el-table-column prop="item" label="항목" align="center" />
      <el-table-column prop="info" label="내용" align="center" />
    </el-table>

    <el-divider border-style="dotted" />

    <el-table class="detail-view__table" header-cell-class-name="table-header" border :table-layout="tableLayout"
      :data="monitorDeviceCmsList" @row-dblclick="handleEdit">
      <el-table-column prop="serialNumber" label="시리얼넘버" />
      <el-table-column prop="ip" label="IP" />
      <el-table-column prop="isConnected" label="연결 상태" />
      <el-table-column prop="lastConnectionTime" label="마지막 연결 시간" />
      <el-table-column fixed="right" label="Edit" width="120">
        <template #default="scope">
          <div style="text-align: center;">
          <el-icon>
            <Remove @click.prevent="handleRemove(scope.row)" />
          </el-icon>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <RegisterInterfaceDialog v-model:dialog-form-visible="registerDialogFormVisible"
                           v-if="equipmentDiag"
    :equipment-diag="equipmentDiag" />
  <AddMonitorDeviceCmsDialog
      v-model:addMonitorCmsDialogVisible="addMonitorCmsDialogVisible"
      v-model:equipmentDiagId="equipmentDiagId"
      ref="addMonitorDeviceCms" />
  <AddInfoDeviceCmsDialog
      v-model:add-info-device-cms-dialog-visible="addInfoDeviceCmsDialogVisible"
    ref="addInfoDeviceCms" />
  <ReportDialog
      v-model:report-dialog-visible="reportDialogVisible"
      v-if="equipmentDiag"
      :equipment-diag="equipmentDiag"
      :title="title"
      :equipment-no="equipmentNo"
      :image-src="imageSrc"
  />
  <EquipmentHistoryDialog
      v-model:equipment-history-dialog-visible="equipmentHistoryDialogVisible"
      v-if="equipmentNo"
    :equipment-no="equipmentNo" :title="title" />
  <EditTagInfoDialog
      v-model:edit-tag-info-dialog-visible="editTagInfoDialogVisible"
      v-if="equipmentDiag"
      :equipment-diag="equipmentDiag"
  />
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, computed, watch } from 'vue'
import api from '@/api/axios'
import { useI18n } from 'vue-i18n';   /* i18n */
const { t } = useI18n()

import RegisterInterfaceDialog from '@/views/detailView/common/RegisterInterfaceDialog.vue'
import AddMonitorDeviceCmsDialog from '@/views/detailView/robotVibration/dialog/AddMonitorDeviceCmsDialog.vue';
import AddInfoDeviceCmsDialog from "@/views/detailView/robotVibration/dialog/AddInfoDeviceCms.vue";
import EquipmentHistoryDialog from "@/views/detailView/common/EquipmentHistoryDialog.vue";

import type { TableInstance } from 'element-plus'
import ReportDialog from "@/views/detailView/common/ReportDialog.vue";
const tableLayout = ref<TableInstance['tableLayout']>('auto')


const addMonitorCmsDialogVisible = ref(false);
const equipmentDiag = defineModel('equipmentDiag')
const equipmentDiagId = defineModel('equipmentDiagId')
const equipmentData = defineModel('equipmentData')
const equipmentId = defineModel('equipmentId')

const imageSrc = ref("");
const monitorDeviceCmsList = defineModel('monitorDeviceCmsList');
const title = defineModel('title');
const addInfoDeviceCmsDialogVisible = ref(false);
const addInfoDeviceCms = ref(null);
const addMonitorDeviceCms = ref(null);

const registerDialogFormVisible = ref(false)
const handleRegisterInterface = () => {
  registerDialogFormVisible.value = true
}

/* 리포트 조회 팝업 부분 */
const reportDialogVisible = ref(false);

const handleReport = () => {
  reportDialogVisible.value = true;
};

const handleAddEdgeCmsDialog = () => {
  addMonitorCmsDialogVisible.value = true;
};


async function getEquipmentInfo() {
  await api
      .get("/info/equipmentDiag/" + equipmentDiagId.value)
      .then(
          (response: {
            data: {
              data: {
                functionLocation: string;
                equipmentNameKo: string;
                equipmentNo: string;
                diagTypeName: string;
                diagStatus: string;
              };
            };
          }) => {
            let idx = equipmentData.findIndex((obj) => obj.item === "Func Location");
            equipmentData[idx].info = response.data.data.functionLocation;
            idx = equipmentData.findIndex((obj) => obj.item === "Facility Name");
            equipmentData[idx].info = response.data.data.equipmentNameKo;
            idx = equipmentData.findIndex((obj) => obj.item === "Equipment No.");
            equipmentData[idx].info = response.data.data.equipmentNo;
            idx = equipmentData.findIndex((obj) => obj.item === "Diagnosis Type");
            equipmentData[idx].info = response.data.data.diagTypeName;

            equipmentDiag.value = response.data.data;

            title.value = equipmentDiag.value.equipmentNameKo;
            equipmentId.value = equipmentDiag.value.equipmentId;
            equipmentNo.value = equipmentDiag.value.equipmentNo;
          }
      )
      .catch((error: any) => {
        console.error(error);
      });
}
const handleEdit = async (row: any) => {
  const infoDeviceCmsList = await addInfoDeviceCms.value.fetchInfoDeviceCmsList();
  addInfoDeviceCmsDialogVisible.value = true;
  addInfoDeviceCms.value.setInfoDeviceCms(infoDeviceCmsList, row.serialNumber);
};

const handleRemove = async (row: any) => {
  monitorDeviceCmsList.value = await addMonitorDeviceCms.value.removeMonitorDeviceCmsList(monitorDeviceCmsList.value, row.serialNumber);
};

watch(addMonitorCmsDialogVisible, async (val) => {
  if (val === false) {
    monitorDeviceCmsList.value = await addMonitorDeviceCms.value.fetchMonitorDeviceCmsList();
  }
});

watch(registerDialogFormVisible, () => {
  if (registerDialogFormVisible.value == false) {
    getEquipmentInfo();
  }
});

const equipmentNo = ref(null);
const equipmentHistoryDialogVisible = ref(false);
const handleEquipmentHistory = () => {
  equipmentHistoryDialogVisible.value = true;
};

onMounted(async () => {
  monitorDeviceCmsList.value = await addMonitorDeviceCms.value.fetchMonitorDeviceCmsList();
  getEquipmentInfo()
})
</script>

<style lang="scss">
@import "@/style/views/_detailview.scss";
</style>