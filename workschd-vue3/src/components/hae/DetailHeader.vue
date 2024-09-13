

<template>
  <el-col :span="12"  class="head detail-header">
    <el-row justify="start" class="head__title">
      <el-col :span="2">
        <input class="head__input" type="checkbox" v-model="isFavoriteChecked" id="favorite" name="favorite" @change="handleFavoriteCheckChange">
        <label for="favorite" class="head__button">
          <el-icon v-if="!isFavoriteChecked"><Star/></el-icon>
          <el-icon v-else><StarFilled class="head__button--checked" /></el-icon>
        </label>
      </el-col>
      <el-col :span="22">
        <el-text class="head__title_text" > {{ title }} </el-text>
      </el-col>
    </el-row>
  </el-col>
  <el-col :span="4" :offset="8">
    <el-row justify="end">
      <el-button style="float: right;" @click="handleIndividualAlarmList">알림</el-button>
    </el-row>
  </el-col>
  <el-divider class="divider--up"/>


  <IndividualAlarmList
      v-model:individual-alarm-list-visible="individualAlarmListVisible"
      v-if="equipmentDiag"
      :equipment-diag="equipmentDiag"
  />
</template>
<script setup lang="ts">
import {onMounted, ref, watch} from 'vue';
import { Star, StarFilled } from '@element-plus/icons-vue';

import EditThresholdDialog from "@/views/detailView/common/EditThresholdDialog.vue";
import IndividualAlarmList from "@/views/detailView/common/IndividualAlarmList.vue";
import api from "@/api/axios";
import {useUserStore} from "@/stores/user";

// const equipmentDiag = defineModel('equipmentDiag')
const props = defineProps({
  title: { type: String, required: true },
  equipmentDiag: { type: String, required: false },
  equipmentDiagId: { type: String, required: false }
});

const userId = useUserStore().userId
const emit = defineEmits(['update:isFavoriteChecked', 'individualAlarmList']);

// watch(() => props.isFavoriteChecked,
//     (newValue) => isFavoriteChecked.value = newValue
// )
onMounted(async () => {
  await isEquipmentFavoriteChecked()
})
/* 개별 알림 리스트 팝업 부분 */
const individualAlarmListVisible = ref(false)
const equipmentDiagId = ref(props.equipmentDiagId)
const handleIndividualAlarmList = () => individualAlarmListVisible.value = true


/* 설비 즐겨찾기 부분 */
const isFavoriteChecked = ref(false);
async function isEquipmentFavoriteChecked() {
  await api.get("/management/favorite/" + userId + "/" + equipmentDiagId.value)
      .then(response => isFavoriteChecked.value = response.data.data)
      .catch(error => console.error("Error checking bookmark flag:", error))
}
const handleFavoriteCheckChange = async () => {
    await api.post("/management/favorite", {
      equipmentDiagId: equipmentDiagId.value,
      userId: userId,
      bookmarkYn: isFavoriteChecked.value
    })
    .catch(error => console.error("Error updating bookmark flag:", error))
}

// const handleFavoriteCheckChange = () => emit('update:isFavoriteChecked', localIsFavoriteChecked.value)
// const handleIndividualAlarmList = () => emit('individualAlarmList')
</script>

<style lang="scss">
//@import "@/style/views/_detailview.scss";
@import "@/style/views/detailHeader";
</style>