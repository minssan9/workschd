<template>
  <q-page padding>
    <div class="row q-col-gutter-md">
      <!-- Header Section -->
      <div class="col-12">
        <div class="row items-center justify-between q-mb-md">
          <h5 class="q-my-none">{{ t('assembly.title', '국회의원 목록') }}</h5>
          <div class="row q-gutter-sm">
            <q-input
              v-model="searchText"
              :placeholder="t('assembly.search.placeholder', '이름, 정당, 당선차수로 검색')"
              dense
              outlined
              class="col-auto"
            >
              <template v-slot:append>
                <q-icon name="search" />
              </template>
            </q-input>
            <q-btn-group outline>
              <q-btn
                :label="t('assembly.view.grid', '그리드')"
                icon="grid_view"
                :color="viewMode === 'grid' ? 'primary' : 'grey'"
                @click="viewMode = 'grid'"
              />
              <q-btn
                :label="t('assembly.view.card', '카드')"
                icon="view_module"
                :color="viewMode === 'card' ? 'primary' : 'grey'"
                @click="viewMode = 'card'"
              />
            </q-btn-group>
          </div>
        </div>
      </div>

      <!-- First Grid (Original) -->
      <template v-if="viewMode === 'grid'">
        <div class="col-12 q-mb-lg">
          <h6>국회의원 목록 (기존)</h6>
          <GridDefault
            :columnDefs="columnDefs"
            :rowData="filteredMembers"
            @grid-ready="onGridReady"
            class="ag-theme-alpine"
            style="height: 50vh"
          />
        </div>

        <!-- New Grid (Current Members) -->
        <div class="col-12">
          <h6>국회의원 현황</h6>
          <GridDefault
            :columnDefs="memberColumnDefs"
            :rowData="currentMembers"
            @grid-ready="onMemberGridReady"
            class="ag-theme-alpine"
            style="height: 50vh"
          />
        </div>
      </template>
      
      <!-- Card View -->
      <template v-else>
        <div v-for="member in filteredMembers" :key="member.NAAS_CD" class="col-12 col-sm-6 col-md-4">
          <q-card class="member-card">
            <q-card-section horizontal>
              <q-img
                :src="member.NAAS_PIC"
                :alt="member.NAAS_NM"
                style="width: 120px"
                contain
              >
                <template v-slot:error>
                  <div class="absolute-full flex flex-center bg-grey-3">
                    <q-icon name="person" size="48px" color="grey-7" />
                  </div>
                </template>
              </q-img>

              <q-card-section>
                <div class="text-h6">{{ member.NAAS_NM }}</div>
                <div class="text-subtitle2">{{ member.PLPT_NM }}</div>
                <div class="text-caption">
                  {{ member.ELECD_NM || member.ELECD_DIV_NM }}
                </div>
                <div class="text-caption q-mt-sm">
                  {{ member.CMIT_NM || member.BLNG_CMIT_NM }}
                </div>
              </q-card-section>
            </q-card-section>

            <q-separator />

            <q-card-actions>
              <q-btn
                v-if="member.NAAS_HP_URL"
                flat
                round
                icon="language"
                :href="member.NAAS_HP_URL"
                target="_blank"
              >
                <q-tooltip>{{ t('assembly.member.website', '홈페이지') }}</q-tooltip>
              </q-btn>
              <q-btn
                v-if="member.NAAS_EMAIL_ADDR"
                flat
                round
                icon="email"
                :href="`mailto:${member.NAAS_EMAIL_ADDR}`"
              >
                <q-tooltip>{{ t('assembly.member.email', '이메일') }}</q-tooltip>
              </q-btn>
              <q-space />
              <q-btn
                flat
                color="primary"
                :label="t('assembly.member.details', '상세정보')"
                @click="showDetails(member)"
              />
            </q-card-actions>
          </q-card>
        </div>
      </template>

      <!-- Move pagination to bottom of page and enhance it -->
      <div class="pagination-container q-mt-xl">
        <div class="row items-center justify-between q-px-md">
          <div class="text-subtitle2">
            {{ t('assembly.pagination.total', '총 {total}명', { total: totalItems }) }}
          </div>
          
          <div class="row items-center q-gutter-x-md">
            <q-select
              v-model="itemsPerPage"
              :options="[10, 20, 50, 100]"
              :label="t('assembly.pagination.itemsPerPage', '페이지당 항목')"
              dense
              outlined
              style="width: 150px"
              @update:model-value="handleItemsPerPageChange"
            />
            
            <q-pagination
              v-model="currentPage"
              :max="totalPages"
              :max-pages="6"
              :disable="isLoading"
              boundary-numbers
              direction-links
              boundary-links
              @update:model-value="handlePageChange"
            >
              <template v-slot:prev="scope">
                <q-btn
                  :disable="scope.disable"
                  round
                  flat
                  size="10px"
                  icon="chevron_left"
                  @click="scope.onClick"
                />
              </template>
              
              <template v-slot:next="scope">
                <q-btn
                  :disable="scope.disable"
                  round
                  flat
                  size="10px"
                  icon="chevron_right"
                  @click="scope.onClick"
                />
              </template>
            </q-pagination>
          </div>
        </div>
      </div>

      <!-- Loading overlay -->
      <q-inner-loading :showing="isLoading">
        <q-spinner-dots size="50px" color="primary" />
      </q-inner-loading>
    </div>

    <!-- Details Dialog -->
    <q-dialog v-model="showDetailsDialog">
      <q-card style="min-width: 350px">
        <q-card-section v-if="selectedMember">
          <div class="text-h6">{{ selectedMember.NAAS_NM }}</div>
          <div class="text-subtitle2">{{ selectedMember.NAAS_CH_NM }}</div>
        </q-card-section>

        <q-card-section v-if="selectedMember" class="q-pt-none">
          <q-list>
            <q-item>
              <q-item-section>
                <q-item-label caption>{{ t('assembly.member.party', '소속정당') }}</q-item-label>
                <q-item-label>{{ selectedMember.PLPT_NM }}</q-item-label>
              </q-item-section>
            </q-item>
            <q-item>
              <q-item-section>
                <q-item-label caption>{{ t('assembly.member.district', '선거구') }}</q-item-label>
                <q-item-label>{{ selectedMember.ELECD_NM || selectedMember.ELECD_DIV_NM }}</q-item-label>
              </q-item-section>
            </q-item>
            <q-item v-if="selectedMember.BRF_HST">
              <q-item-section>
                <q-item-label caption>{{ t('assembly.member.history', '주요 경력') }}</q-item-label>
                <q-item-label>{{ selectedMember.BRF_HST }}</q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('common.close', '닫기')" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar } from 'quasar'
import GridDefault from '@/components/grid/GridDefault.vue'
import type { ColDef } from 'ag-grid-community'
import apiAssembly from '@/api/modules/api-assembly'

const { t } = useI18n()
const $q = useQuasar()

// State
const members = ref([])
const searchText = ref('')
const viewMode = ref('grid')
const showDetailsDialog = ref(false)
const selectedMember = ref(null)

// Pagination state
const currentPage = ref(1)
const itemsPerPage = ref(20)
const totalItems = ref(0)
const totalPages = computed(() => Math.ceil(totalItems.value / itemsPerPage.value))

// Add loading state
const isLoading = ref(false)

// Grid Configuration
const columnDefs = ref<ColDef[]>([
  {
    headerName: t('assembly.grid.id', '의원번호'),
    field: 'NAAS_CD',
    sortable: true,
    filter: true,
    width: 100
  },
  {
    headerName: t('assembly.grid.name', '이름'),
    field: 'NAAS_NM',
    sortable: true,
    filter: true
  },
  {
    headerName: t('assembly.grid.party', '정당'),
    field: 'PLPT_NM',
    sortable: true,
    filter: true
  },
  {
    headerName: t('assembly.grid.district', '선거구'),
    field: 'ELECD_NM',
    sortable: true,
    filter: true,
    valueGetter: (params) => params.data.ELECD_NM || params.data.ELECD_DIV_NM
  },
  {
    headerName: t('assembly.grid.divDistrict', '선거구분'),
    field: 'ELECD_DIV_NM',
    sortable: true,
    filter: true
  },
  {
    headerName: t('assembly.grid.committee', '소속위원회'),
    field: 'CMIT_NM',
    sortable: true,
    filter: true,
    valueGetter: (params) => params.data.CMIT_NM || params.data.BLNG_CMIT_NM
  },
  {
    headerName: t('assembly.grid.experience', '당선차수'),
    field: 'GTELT_ERACO',
    sortable: true,
    filter: true,
    width: 100
  }
])

// Computed
const filteredMembers = computed(() => {
  if (!searchText.value) return members.value

  const search = searchText.value.toLowerCase()
  return members.value.filter(member => 
    member.NAAS_NM?.toLowerCase().includes(search) ||
    member.PLPT_NM?.toLowerCase().includes(search) ||
    member.GTELT_ERACO?.toLowerCase().includes(search)
  )
})

// Methods
const fetchMembers = async (page = 1, size = itemsPerPage.value) => {
  isLoading.value = true
  try {
    const data = await apiAssembly.getMembers({
      pIndex: page,
      pSize: size
    })
    
    if (data.ALLNAMEMBER[0].head[1].RESULT.CODE !== 'INFO-000') {
      throw new Error(data.ALLNAMEMBER[0].head[1].RESULT.MESSAGE)
    }
    
    members.value = data.ALLNAMEMBER[1].row
    totalItems.value = data.ALLNAMEMBER[0].head[0].list_total_count
  } catch (error) {
    console.error('Failed to fetch assembly members:', error)
    $q.notify({
      type: 'negative',
      message: t('assembly.error.fetch', '의원 정보를 불러오는데 실패했습니다')
    })
  } finally {
    isLoading.value = false
  }
}

const showDetails = (member) => {
  selectedMember.value = member
  showDetailsDialog.value = true
}

const onGridReady = () => {
  // Additional grid initialization if needed
}

// Pagination handlers
const handlePageChange = (page: number) => {
  if (isLoading.value) return
  currentPage.value = page
  fetchMembers(page)
}

const handleItemsPerPageChange = (size: number) => {
  if (isLoading.value) return
  itemsPerPage.value = size
  currentPage.value = 1
  fetchMembers(1, size)
}

// Watch for search text changes
watch(searchText, (newValue) => {
  if (newValue === '') {
    // Reset to first page when clearing search
    currentPage.value = 1
    fetchMembers()
  }
})

// New refs for current members
const currentMembers = ref([])
const pageSize = ref(10)
const totalCurrentItems = ref(0)

// Column definitions for current members grid
const memberColumnDefs = ref<ColDef[]>([
  {
    headerName: t('assembly.grid.number', '의원번호'),
    field: 'num',
    sortable: true,
    filter: true,
    width: 100
  },
  {
    headerName: t('assembly.grid.name', '이름'),
    field: 'empNm',
    sortable: true,
    filter: true
  },
  {
    headerName: t('assembly.grid.englishName', '영문이름'),
    field: 'engNm',
    sortable: true,
    filter: true
  },
  {
    headerName: t('assembly.grid.chineseName', '한자이름'),
    field: 'hjNm',
    sortable: true,
    filter: true
  },
  {
    headerName: t('assembly.grid.district', '선거구'),
    field: 'origNm',
    sortable: true,
    filter: true
  },
  {
    headerName: t('assembly.grid.term', '당선횟수'),
    field: 'reeleGbnNm',
    sortable: true,
    filter: true
  },
  {
    headerName: t('assembly.grid.photo', '사진'),
    field: 'jpgLink',
    sortable: false,
    filter: false,
    cellRenderer: (params: any) => {
      return params.value ? `<img src="${params.value}" style="width: 40px; height: 50px; object-fit: cover;">` : ''
    }
  }
])

// Function to fetch current members
const fetchCurrentMembers = async () => {
  try {
    const response = await apiAssembly.getMemberList({
      pIndex: currentPage.value,
      pSize: pageSize.value
    })
    
    currentMembers.value = response.items
    totalCurrentItems.value = response.pageInfo.totalCount

    // Update pagination if needed
    if (response.pageInfo.pageNo !== currentPage.value) {
      currentPage.value = response.pageInfo.pageNo
    }
  } catch (error) {
    console.error('Error fetching current members:', error)
    $q.notify({
      type: 'negative',
      message: t('assembly.error.fetch', '의원 목록을 불러오는데 실패했습니다.')
    })
  }
}

// Grid ready handler for current members
const onMemberGridReady = (params: any) => {
  params.api.sizeColumnsToFit()
}

// Lifecycle
onMounted(() => {
  fetchMembers()
  fetchCurrentMembers()
})
</script>

<style lang="scss" scoped>
.member-card {
  height: 100%;
  
  .q-img {
    background-color: #f5f5f5;
  }
}

.text-caption {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.pagination-container {
  position: sticky;
  bottom: 0;
  background: white;
  padding: 16px 0;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  z-index: 2;

  .q-pagination {
    .q-btn {
      padding: 0 10px;
      min-height: 32px;
      
      &--round {
        padding: 0;
        min-height: 24px;
      }
    }
  }

  .q-select {
    min-width: 120px;
  }
}

// Add responsive styles
@media (max-width: 600px) {
  .pagination-container {
    .row {
      flex-direction: column;
      gap: 16px;
    }
    
    .text-subtitle2 {
      text-align: center;
    }
  }
}

h6 {
  margin-top: 0;
  margin-bottom: 1rem;
}
</style> 