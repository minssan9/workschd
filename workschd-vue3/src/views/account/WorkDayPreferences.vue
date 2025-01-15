<template>
  <div class="work-day-preferences">
    <h2>근무일 선호도 설정</h2>
    
    <!-- 선호/비선호 요일 추가 폼 -->
    <div class="preference-form q-pa-md">
      <q-form @submit="onSubmit" class="q-gutter-md">
        <div class="row q-col-gutter-md">
          <div class="col-12 col-md-6">
            <q-select
              v-model="form.dayOfWeek"
              :options="daysOfWeek"
              label="요일"
              :rules="[val => !!val || '요일을 선택해주세요']"
            />
          </div>
          
          <div class="col-12 col-md-6">
            <q-select
              v-model="form.preferenceType"
              :options="preferenceTypes"
              label="선호도 유형"
              :rules="[val => !!val || '유형을 선택해주세요']"
            />
          </div>
        </div>

        <div class="row q-col-gutter-md" v-if="form.preferenceType === 'PREFERRED'">
          <div class="col-12 col-md-6">
            <q-input
              v-model="form.preferredStartTime"
              type="time"
              label="선호 시작 시간"
            />
          </div>
          
          <div class="col-12 col-md-6">
            <q-input
              v-model="form.preferredEndTime"
              type="time"
              label="선호 종료 시간"
            />
          </div>
        </div>

        <div class="row justify-end">
          <q-btn label="추가" type="submit" color="primary"/>
        </div>
      </q-form>
    </div>

    <!-- 선호 요일 목록 -->
    <div class="preferences-list q-pa-md">
      <h3>선호 요일</h3>
      <q-list bordered separator>
        <q-item v-for="pref in preferredDays" :key="pref.id">
          <q-item-section>
            <q-item-label>{{ getDayLabel(pref.dayOfWeek) }}</q-item-label>
            <q-item-label caption v-if="pref.preferredStartTime">
              {{ pref.preferredStartTime }} - {{ pref.preferredEndTime }}
            </q-item-label>
          </q-item-section>
          <q-item-section side>
            <q-btn flat round color="negative" icon="delete" @click="deletePreference(pref.id)"/>
          </q-item-section>
        </q-item>
      </q-list>

      <h3 class="q-mt-lg">근무 불가능 요일</h3>
      <q-list bordered separator>
        <q-item v-for="pref in unavailableDays" :key="pref.id">
          <q-item-section>
            <q-item-label>{{ getDayLabel(pref.dayOfWeek) }}</q-item-label>
          </q-item-section>
          <q-item-section side>
            <q-btn flat round color="negative" icon="delete" @click="deletePreference(pref.id)"/>
          </q-item-section>
        </q-item>
      </q-list>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useQuasar } from 'quasar';
import apiPreferredWorkDay, { PreferredWorkDayDTO } from '@/api/modules/api-preferred-work-day';

const $q = useQuasar();

const daysOfWeek = [
  { label: '월요일', value: 1 },
  { label: '화요일', value: 2 },
  { label: '수요일', value: 3 },
  { label: '목요일', value: 4 },
  { label: '금요일', value: 5 },
  { label: '토요일', value: 6 },
  { label: '일요일', value: 0 }
];

const preferenceTypes = [
  { label: '선호 요일', value: 'PREFERRED' },
  { label: '근무 불가능 요일', value: 'UNAVAILABLE' }
];

const form = ref<PreferredWorkDayDTO>({
  accountInfoId: 0, // TODO: 실제 로그인한 사용자의 AccountInfo ID로 설정
  dayOfWeek: null,
  preferenceType: null,
  preferredStartTime: '',
  preferredEndTime: ''
});

const preferences = ref<PreferredWorkDayDTO[]>([]);

const preferredDays = computed(() => 
  preferences.value.filter(p => p.preferenceType === 'PREFERRED')
);

const unavailableDays = computed(() => 
  preferences.value.filter(p => p.preferenceType === 'UNAVAILABLE')
);

const getDayLabel = (dayOfWeek: number) => {
  return daysOfWeek.find(d => d.value === dayOfWeek)?.label || '';
};

const loadPreferences = async () => {
  try {
    const response = await apiPreferredWorkDay.getByAccountInfoId(form.value.accountInfoId);
    preferences.value = response.data;
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: '선호도 정보를 불러오는데 실패했습니다.'
    });
  }
};

const onSubmit = async () => {
  try {
    await apiPreferredWorkDay.create(form.value);
    $q.notify({
      color: 'positive',
      message: '선호도가 추가되었습니다.'
    });
    await loadPreferences();
    resetForm();
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: '선호도 추가에 실패했습니다.'
    });
  }
};

const deletePreference = async (id: number) => {
  try {
    await apiPreferredWorkDay.delete(id);
    $q.notify({
      color: 'positive',
      message: '선호도가 삭제되었습니다.'
    });
    await loadPreferences();
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: '선호도 삭제에 실패했습니다.'
    });
  }
};

const resetForm = () => {
  form.value = {
    accountInfoId: form.value.accountInfoId,
    dayOfWeek: null,
    preferenceType: null,
    preferredStartTime: '',
    preferredEndTime: ''
  };
};

onMounted(() => {
  // TODO: 로그인한 사용자의 AccountInfo ID 설정
  form.value.accountInfoId = 1; // 임시값
  loadPreferences();
});
</script>

<style scoped>
.work-day-preferences {
  max-width: 800px;
  margin: 0 auto;
}

.preference-form {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
}

.preferences-list {
  margin-top: 20px;
}
</style> 