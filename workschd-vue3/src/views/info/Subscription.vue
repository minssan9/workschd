<template>
  <q-page class="bg-white">
    <div class="q-pa-md">
      <div class="q-px-lg">
        <!-- Header -->
        <div class="text-h5 q-mb-lg">Subscription</div>

        <!-- Pricing Cards -->
        <div class="row q-col-gutter-md">
          <!-- Premium Plan -->
          <div class="col-3">
            <q-card 
              flat 
              bordered 
              class="subscription-card"
              :class="{ 'selected-plan': planType === 'premium' }"
            >
              <q-card-section>
                <div class="text-grey">PREMIUM</div>
                <div class="text-h4 q-mt-sm">
                  <span class="text-primary">$150</span>
                </div>
                <div class="text-grey-7 q-mt-md">
                  Premium features and support for advanced users
                </div>
              </q-card-section>
              <q-card-actions>
                <q-btn 
                  flat 
                  :color="planType === 'premium' ? 'grey' : 'primary'"
                  class="full-width"
                  :label="planType === 'premium' ? 'Current Plan' : 'Choose'" 
                  @click="subscribe('premium')"
                  :disable="planType === 'premium'"
                />
              </q-card-actions>
            </q-card>
          </div>

          <!-- Basic Plan -->
          <div class="col-3">
            <q-card 
              flat 
              bordered 
              class="subscription-card"
              :class="{ 'selected-plan': planType === 'basic' }"
            >
              <q-card-section>
                <div class="text-grey">BASIC</div>
                <div class="text-h4 q-mt-sm">
                  <span class="text-primary">$100</span>
                </div>
                <div class="text-grey-7 q-mt-md">
                  Standard features for regular users
                </div>
              </q-card-section>
              <q-card-actions>
                <q-btn 
                  flat 
                  :color="planType === 'basic' ? 'grey' : 'primary'"
                  class="full-width"
                  :label="planType === 'basic' ? 'Current Plan' : 'Choose'" 
                  @click="subscribe('basic')"
                  :disable="planType === 'basic'"
                />
              </q-card-actions>
            </q-card>
          </div>

          <!-- Starter Plan -->
          <div class="col-3">
            <q-card 
              flat 
              bordered 
              class="subscription-card"
              :class="{ 'selected-plan': planType === 'starter' }"
            >
              <q-card-section>
                <div class="text-grey">STARTER</div>
                <div class="text-h4 q-mt-sm">
                  <span class="text-primary">$50</span>
                </div>
                <div class="text-grey-7 q-mt-md">
                  Essential features for beginners
                </div>
              </q-card-section>
              <q-card-actions>
                <q-btn 
                  flat 
                  :color="planType === 'starter' ? 'grey' : 'primary'"
                  class="full-width"
                  :label="planType === 'starter' ? 'Current Plan' : 'Choose'" 
                  @click="subscribe('starter')"
                  :disable="planType === 'starter'"
                />
              </q-card-actions>
            </q-card>
          </div>

          <!-- Small Plan -->
          <div class="col-3">
            <q-card 
              flat 
              bordered 
              class="subscription-card"
              :class="{ 'selected-plan': planType === 'small' }"
            >
              <q-card-section>
                <div class="text-grey">SMALL</div>
                <div class="text-h4 q-mt-sm">
                  <span class="text-primary">$10</span>
                </div>
                <div class="text-grey-7 q-mt-md">
                  Basic features for small needs
                </div>
              </q-card-section>
              <q-card-actions>
                <q-btn 
                  flat 
                  :color="planType === 'small' ? 'grey' : 'primary'"
                  class="full-width"
                  :label="planType === 'small' ? 'Current Plan' : 'Choose'" 
                  @click="subscribe('small')"
                  :disable="planType === 'small'"
                />
              </q-card-actions>
            </q-card>
          </div>
        </div>

        <!-- Features Section -->
        <div class="q-mt-xl">
          <div class="text-subtitle1 q-mb-md">All accounts included:</div>
          <div class="row q-col-gutter-md">
            <div class="col-3" v-for="(feature, index) in features" :key="index">
              <div class="flex items-center q-mb-sm">
                <q-icon name="check" color="green" size="xs" class="q-mr-sm" />
                <span class="text-grey-7">{{ feature }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useQuasar } from 'quasar'
import apiSubscription from '@/api/modules/api-subscription'

const $q = useQuasar()
const planType = ref(null)

const features = [
  'One user included',
  'Basic support',
  'Access to main features',
  'Regular updates',
  'Community access',
  'Email support',
  'API access',
  'Developer tools'
]

onMounted(async () => {
  try {
    const response = await apiSubscription.getCurrentPlan()
    planType.value = response.data.planType
  } catch (error) {
    console.error('Error fetching current plan:', error)
  }
})

async function subscribe(plan) {
  try {
    await apiSubscription.subscribe(plan)
    planType.value = plan
    $q.notify({
      type: 'positive',
      message: 'Successfully subscribed to ' + plan + ' plan',
      position: 'top'
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Error during subscription process. Please try again.',
      position: 'top'
    })
  }
}
</script>

<style scoped>
.subscription-card {
  height: 100%;
  transition: all 0.3s ease;
}

.subscription-card:hover {
  border-color: var(--q-primary);
}

.selected-plan {
  border: 2px solid var(--q-primary);
  background-color: rgba(var(--q-primary), 0.05);
}

.subscription-card .q-card__section {
  padding: 24px;
}

.subscription-card .q-card__actions {
  padding: 16px 24px;
}
</style>