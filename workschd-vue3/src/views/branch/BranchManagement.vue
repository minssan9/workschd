<template>
  <div class="branch-management">
    <h2>지점 관리</h2>
    
    <!-- 지점 등록 폼 -->
    <div class="branch-form">
      <q-form @submit="onSubmit" class="q-gutter-md">
        <q-input
          v-model="form.name"
          label="지점명"
          :rules="[val => !!val || '지점명은 필수입니다']"
        />
        
        <q-input
          v-model="form.region"
          label="지역"
          :rules="[val => !!val || '지역은 필수입니다']"
        />
        
        <q-input
          v-model="form.address"
          label="주소"
          type="textarea"
        />
        
        <div>
          <q-btn label="등록" type="submit" color="primary"/>
          <q-btn label="초기화" type="reset" flat class="q-ml-sm" @click="resetForm"/>
        </div>
      </q-form>
    </div>

    <!-- 지점 목록 -->
    <div class="branch-list q-mt-md">
      <q-table
        :rows="branches"
        :columns="columns"
        row-key="id"
      >
        <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn flat round color="primary" icon="edit" @click="editBranch(props.row)"/>
            <q-btn flat round color="negative" icon="delete" @click="deleteBranch(props.row.id)"/>
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useQuasar } from 'quasar';
import apiBranch, { BranchDTO } from '@/api/modules/api-branch';

const $q = useQuasar();

const form = ref<BranchDTO>({
  name: '',
  region: '',
  address: ''
});

const branches = ref<BranchDTO[]>([]);

const columns = [
  { name: 'name', label: '지점명', field: 'name', align: 'left' },
  { name: 'region', label: '지역', field: 'region' },
  { name: 'address', label: '주소', field: 'address' },
  { name: 'actions', label: '관리', field: 'actions' }
];

const loadBranches = async () => {
  try {
    const response = await apiBranch.getList();
    branches.value = response.data;
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: '지점 목록을 불러오는데 실패했습니다.'
    });
  }
};

const onSubmit = async () => {
  try {
    await apiBranch.create(form.value);
    $q.notify({
      color: 'positive',
      message: '지점이 등록되었습니다.'
    });
    resetForm();
    loadBranches();
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: '지점 등록에 실패했습니다.'
    });
  }
};

const resetForm = () => {
  form.value = {
    name: '',
    region: '',
    address: ''
  };
};

const editBranch = (branch: BranchDTO) => {
  form.value = { ...branch };
};

const deleteBranch = async (id: number) => {
  try {
    await apiBranch.delete(id);
    $q.notify({
      color: 'positive',
      message: '지점이 삭제되었습니다.'
    });
    loadBranches();
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: '지점 삭제에 실패했습니다.'
    });
  }
};

onMounted(() => {
  loadBranches();
});
</script>

<style scoped>
.branch-management {
  padding: 20px;
}

.branch-form {
  max-width: 500px;
  margin: 20px 0;
}
</style> 