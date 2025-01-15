<template>
  <div class="account-preferences">
    <h2>근무 선호도 설정</h2>
    
    <q-form @submit="onSubmit" class="q-gutter-md">
      <q-select
        v-model="form.employeeType"
        :options="employeeTypes"
        label="고용 형태"
        :rules="[val => !!val || '고용 형태를 선택해주세요']"
      />
      
      <q-select
        v-model="form.preferredDay"
        :options="daysOfWeek"
        label="선호 요일"
      />
      
      <q-select
        v-model="form.unavailableDaysOfWeek"
        :options="daysOfWeek"
        label="근무 불가능 요일"
        multiple
      />
      
      <div class="row q-gutter-md">
        <q-input
          v-model="form.preferredStartTime"
          type="time"
          label="선호 시작 시간"
          class="col"
        />
        
        <q-input
          v-model="form.preferredEndTime"
          type="time"
          label="선호 종료 시간"
          class="col"
        />
      </div>
      
      <div>
        <q-btn label="저장" type="submit" color="primary"/>
        <q-btn label="초기화" type="reset" flat class="q-ml-sm" @click="resetForm"/>
      </div>
    </q-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useQuasar } from 'quasar';
import apiAccountInfo, { AccountInfoDTO } from '@/api/modules/api-account-info';

const $q = useQuasar();

const employeeTypes = [
  { label: '정규직', value: 'FULL_TIME' },
  { label: '파트타임', value: 'PART_TIME' },
  { label: '임시직', value: 'TEMPORARY' }
];

const daysOfWeek = [
  { label: '월요일', value: 1 },
  { label: '화요일', value: 2 },
  { label: '수요일', value: 3 },
  { label: '목요일', value: 4 },
  { label: '금요일', value: 5 },
  { label: '토요일', value: 6 },
  { label: '일요일', value: 0 }
];

const form = ref<AccountInfoDTO>({
  accountId: 0, // 실제로는 로그인한 사용자의 ID를 사용
  employeeType: 'FULL_TIME',
  preferredDay: '',
  unavailableDaysOfWeek: [],
  preferredStartTime: '',
  preferredEndTime: ''
});

const onSubmit = async () => {
  try {
    if (form.value.id) {
      await apiAccountInfo.update(form.value.id, form.value);
    } else {
      await apiAccountInfo.create(form.value);
    }
    
    $q.notify({
      color: 'positive',
      message: '선호도 설정이 저장되었습니다.'
    });
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: '선호도 설정 저장에 실패했습니다.'
    });
  }
};

const resetForm = () => {
  form.value = {
    accountId: 0,
    employeeType: 'FULL_TIME',
    preferredDay: '',
    unavailableDaysOfWeek: [],
    preferredStartTime: '',
    preferredEndTime: ''
  };
};

onMounted(async () => {
  // TODO: 로그인한 사용자의 AccountInfo 정보를 불러와서 form에 설정
});
</script>

<style scoped>
.account-preferences {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}
</style> 