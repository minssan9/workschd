<template>
  <el-card body-class="detail-item-ce common common-image" style="height: 100%;"><!--class="detail-view__item"-->
    <el-row justify="end" >
      <el-col :span="8" >
        <el-row justify="start">
          <el-text >{{ t('facility.image') }} </el-text>
        </el-row>
      </el-col>
      <el-col :span="16" >
        <el-row justify="end">
          <el-button @click="handleSaveSensorPosition">센서 저장</el-button>
          <el-button @click="handleEditEquipmentImage">{{ t('event.save_image') }}</el-button>
        </el-row>
      </el-col>
    </el-row>
    <el-divider class="divider--up"/>
    <el-row class="equipment-image">
      <el-image
        class="equipment-image__contents"
        :src="imageSrc"
        fit="fill"
        v-if="imageSrc"
        @load="onImageLoad"/>

      <SensorPointDrawing
        v-if="imageLoaded"
        :equipmentDiagId="equipmentDiagId as string"
        :imageWidth="imageWidth"
        :imageHeight="imageHeight"
        :renderedWidth="renderedWidth"
        :renderedHeight="renderedHeight"
        ref="sensorPointDrawing"/>
    </el-row>

    <EditEquipmentImageDialog v-model:edit-equipment-image-dialog-visible="editEquipmentImageDialogVisible"
                              :equipmentId="equipmentId as string"
    />
  </el-card>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';   /* i18n */
const { t } = useI18n()
import { ref, watch, onMounted } from 'vue'
import api from '@/api/axios'

import EditEquipmentImageDialog from '@/views/detailView/common/EditEquipmentImageDialog.vue'
import SensorPointDrawing from "@/views/detailView/common/SensorPointDrawing.vue";

const sensorPointDrawing = ref(null)
const equipmentDiagId = defineModel('diagId')
const monitorInfoSensorCmsList = defineModel('monitorInfoSensorCmsList')

// Start: Dialog Variables
const editEquipmentImageDialogVisible = ref(false)
const handleSaveSensorPosition = () => {
  sensorPointDrawing.value.submitChanges()
}
const handleEditEquipmentImage = () => {
  editEquipmentImageDialogVisible.value = true
}
watch(editEquipmentImageDialogVisible, () => {
  if(editEquipmentImageDialogVisible.value === false) {
    // Clear the file list
   fetchImage()
  }
})

/* 이미지 위 센서 포인팅 */
const imageSrc = ref(null);
const imageLoaded = ref(false);
const imageWidth = ref(0);
const imageHeight = ref(0);
const renderedWidth = ref(0);
const renderedHeight = ref(0);

const onImageLoad = (event) => {
  imageWidth.value = event.target.naturalWidth;
  imageHeight.value = event.target.naturalHeight;
  const containerWidth = event.target.clientWidth;
  const containerHeight = event.target.clientHeight;
  renderedWidth.value = containerWidth;
  renderedHeight.value = containerHeight;
  imageLoaded.value = true;
};

const equipmentId = defineModel('equipmentId')

const fetchImage = async () => {
  imageSrc.value = null
  try {
    const response = await api.get("/map/plantMap/image/equipment/" + equipmentId.value, {
      responseType: "blob", 
    });

    const reader = new FileReader();
    reader.onload = () => {
      imageSrc.value = reader.result;
    };
    reader.readAsDataURL(response.data);
  } catch (error) {
    console.error("Failed to fetch image:", error);
  }
};

watch([equipmentId, monitorInfoSensorCmsList], async () => {
  console.debug("monitorInfoSensorCmsList in frame1", monitorInfoSensorCmsList.value);
  await fetchImage()
  await sensorPointDrawing.value.fetchSensorGroupList();
  sensorPointDrawing.value.initializeStage();
})

watch(equipmentId, async () => {
  if(equipmentId.value) {
    //await fetchImage()
  }
})

// onMounted(async () => {
//   await fetchImage()
// })

</script>

<style lang="scss">
@import "@/style/views/_detailview_rv.scss";
</style>

<style scoped lang="scss">
.equipment-image {
    //max-width: 100%;
    //max-height: 100%;
    //overflow: hidden;
    //justify-content: center;
    //align-items: center;
    //height: 290px;
    //position: relative;

    &__contents {
      //width: auto;
      //height: 100%;
      //display: block;
      //margin: 0 auto;
    }
  }

</style>