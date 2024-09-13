<template>
  <div class="diagdashboard">
    <DetailHeader
        :title="title"
        v-model:equipmentDiag="equipmentDiag"
        v-model:equipmentDiagId="equipmentDiagId"
    />

    <div class="detail-view" id="detail-view">
          <div class="detail-view__item">
              <div class="add-system" @click="handleAddSystem">
                  <el-icon><Plus />
                  </el-icon>
                  <el-text style="float:right;">대상 로봇 등록</el-text>
              </div>
              <el-table empty-text="No Registered Robot" class="detail-view__table" header-cell-class-name="table-header" border :data="robotList">
                  <el-table-column label="상태" align="center" width="80">
                      <template #default="scope">
                          <el-icon :style="{ color: getStatusClass(scope.row.status)}">
                              <CircleCheckFilled />
                          </el-icon>
                      </template>
                  </el-table-column>
                  <el-table-column prop="robotId" align="center" label="ID" width="120"/>
                  <el-table-column prop="robotLine" show-overflow-tooltip="true" align="center" label="라인" width="180"/>
                  <el-table-column prop="robotCell" show-overflow-tooltip="true" align="center" label="공정" width="180"/>
                  <el-table-column prop="robotName" show-overflow-tooltip="true" align="center" label="로봇이름" width="180"/>
                  <el-table-column prop="robotCode" show-overflow-tooltip="true" align="center" label="로봇코드" width="120"/>
                  <el-table-column prop="status" align="center" label="상태" width="120"/>
                  <el-table-column prop="errWarnCode" align="center" label="알람코드" width="120"/>
                  <el-table-column prop="AlarmMsg" align="center" show-overflow-tooltip="true" label="알람 내용" width="180">
                      <template #default="scope">
                          <span v-if="scope.row.AlarmMsg">{{  scope.row.AlarmMsg }}</span>
                          <span v-else-if="scope.row.errWarnCode == 0">Normal</span>
                          <span v-else>Parsing Error</span>
                      </template>
                  </el-table-column>
                  <el-table-column prop="interfaceCode" align="center" label="연결시스템" width="120"/>
                  <el-table-column prop="issueTime" align="center" show-overflow-tooltip="true" label="이슈 발생 일시" width="220">
                      <template #default="scope">
                          <span v-if="scope.row.issueTime">{{  scope.row.issueTime }}</span>
                          <span v-else-if="scope.row.errWarnCode == 0">None</span>
                          <span v-else>No Data</span>
                      </template>
                  </el-table-column>
                  <el-table-column label="설정" align="center" width="80">
                      <template #default="scope">
                          <el-icon><Setting @click="handleEdit(scope.row)"/></el-icon>
                      </template>
                  </el-table-column>
              </el-table>
          </div>
      </div>
    <RegisterDeviceDialogVue
        v-model:register-device-visible="registerDeviceVisible"
        v-model:register-device-info="registerDeviceInfo"
        v-model:register-dialog-type="registerDialogType"
        v-model:equipment-diag-id="equipmentDiagId"
        v-model:diag-type-code="diagTypeCode"/>
    <AlarmListVue v-model:alarm-list-visible="alarmListVisible" v-model:robot-list="robotList" v-model:diag-type-code="diagTypeCode"/>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user';
import api from '@/api/axios';
import { useI18n } from 'vue-i18n';   /* i18n */
const { t } = useI18n()

import RegisterDeviceDialogVue from '@/views/detailView/robotMonitoring/RegisterDeviceDialog.vue';
import AlarmListVue from '@/views/detailView/robotMonitoring/AlarmList.vue';
import DetailHeader from "@/components/detailView/DetailHeader.vue";

const userStore = useUserStore();
const userId = userStore.userId;

// Start: Favorite Variables
const isFavoriteChecked = ref(false)
const handleFavoriteCheckChange = async () => {
  try {
    const requestData = {
      equipmentDiagId: equipmentDiagId.value,
      userId: userId,
      bookmarkYn: isFavoriteChecked.value
    };
    await api.post("/management/favorite", requestData);
  } catch (error) {
    console.error("Error updating bookmark flag:", error);
  }
}
// End: Favorite Variables

// Start: For Robot
const robotList = ref([])
// End: For Robot 

const title = ref(null)
const equipmentDiagId = ref(null)
const equipmentDiag = ref(null)
const route = useRoute()
const registerDeviceVisible = ref(false)
const registerDialogType = ref('')
const registerDeviceInfo = ref(null)

watch(() => route.params.id, async () => {
    await getEquipmentInfo()
    await getRobotList()
    await isEquipmentFavoriteChecked()
})

const getStatusClass = (status: string) => {
    switch(status) {
        case 'ERROR':
            return 'red'
        case 'WARNING':
            return 'orange'
        case 'NORMAL':
            return 'green'
        default:
            return 'coral'
    }
}

const diagTypeCode = ref(null)
const alarmListVisible = ref(false)
const handleAlarm = () => {
    alarmListVisible.value = true
}

const handleEdit = (row: any) => {
    console.debug(row)
    registerDeviceVisible.value = true
    registerDialogType.value = 'edit'
    registerDeviceInfo.value = row

}
const handleAddSystem = () => {
    registerDeviceVisible.value = true
    registerDialogType.value = 'add'
}

watch(() => registerDeviceVisible.value, async (value: any) => {
    if(!value) {
        await getRobotList()
    }
})

async function getEquipmentInfo() {
    equipmentDiagId.value = route.params.id;  
    await api.get('/info/equipmentDiag/' + equipmentDiagId.value)
    .then((response: { data: { data: { functionLocation: string; equipmentNameKo: string; equipmentNo: string; diagTypeName: string; diagStatus: string } } }) => {
        equipmentDiag.value = response.data.data
        console.debug(equipmentDiag.value)
        // Set title to equipmentDiag's equipmentNameKo
        title.value = equipmentDiag.value.equipmentNameKo
        diagTypeCode.value = equipmentDiag.value.diagTypeCode
  })
  .catch((error: any) => {
    console.error(error)
  })
}

interface Item {
    id: string;
    location: string;
    robotId: string;
    robotName: string;
    status: string;
    errWarnCdoe: number;
    updateTime: string;
    issueTime: string;
    interfaceCode: string;
}

async function getRobotList() {
    await api.get('/collection/devices/' + diagTypeCode.value + '/' + equipmentDiagId.value)
    .then((response) => {
        console.debug(response.data.data)
        robotList.value = response.data.data
        for(const [key, item] of Object.entries(robotList.value)) {
            const typedItem = item as Item
            // Set Robot ID Format
            typedItem.robotId = 'RB-' + String(typedItem.robotId).padStart(3, '0')
            typedItem.updateTime = new Date(typedItem.updateTime).toLocaleString()
            if(typedItem.issueTime !== null)
                typedItem.issueTime = new Date(typedItem.issueTime).toLocaleString()
            typedItem.interfaceCode = diagTypeCode.value + '-' + String(typedItem.interfaceCode).padStart(3, '0')
        }
    })
}

async function isEquipmentFavoriteChecked() {
  try {
    const response = await api.get("/management/favorite/" + userId + "/" + equipmentDiagId.value)
    isFavoriteChecked.value = response.data.data
  } catch (error) {
    console.error("Error checking bookmark flag:", error);
  }
}

onMounted(async () => {
    await getEquipmentInfo()
    await isEquipmentFavoriteChecked()
    await getRobotList()
})
</script>

<style lang="scss">
@import "@/style/views/_detailview.scss";
</style>

<style lang="scss" scoped>

.diagdashboard > .detail-view {
    grid-template-columns: none;
    grid-template-rows: auto;
}

.add-system {
    padding: 10px 0;
    display: flex;
    justify-content: flex-end;
    font-size: 12px;
    cursor: pointer
}

.add-system .el-text {
    font-size: 14px;
    font-weight: bold;
}

</style>