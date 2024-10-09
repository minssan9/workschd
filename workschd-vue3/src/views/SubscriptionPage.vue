<template>
  <q-page class="q-pa-md">
    <q-card class="my-card">
      <q-card-section>
        <div class="text-h6">Choose Your Subscription Plan</div>
        <q-list bordered separator>
          <q-item clickable @click="subscribe('1-month')">
            <q-item-section>1 Month Plan - $10</q-item-section>
          </q-item>
          <q-item clickable @click="subscribe('6-month')">
            <q-item-section>6 Month Plan - $50</q-item-section>
          </q-item>
          <q-item clickable @click="subscribe('12-month')">
            <q-item-section>12 Month Plan - $90</q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script>
export default {
  methods: {
    subscribe(plan) {
      this.service.post(`/api/payments/create`, { plan })
          .then(response => {
            // PayPal 결제 페이지로 리디렉트
            window.location.href = response.data;
          })
          .catch(() => {
            this.$q.notify('Error during payment.');
          });
    }
  }
}
</script>

<style>
.my-card {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}
</style>