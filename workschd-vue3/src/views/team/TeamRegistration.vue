<template>
  <q-page class="q-pa-md">
    <h5>Team Registration</h5>
    <q-card class="q-mt-md">
      <q-card-section>
        <q-form @submit="onSubmit" class="q-gutter-md">
          <q-input
              v-model="teamId"
              label="Team ID"
              required
              :rules="[val => !!val || 'Team ID is required']"
          />
          <q-input
              v-model="name"
              label="Your Name"
              required
              :rules="[val => !!val || 'Name is required']"
          />
          <q-input
              v-model="location"
              label="Your Location"
              required
              :rules="[val => !!val || 'Location is required']"
          />
          <q-select
              v-model="preferredPlaces"
              label="Preferred Places (1-3)"
              multiple
              :options="placeOptions"
              use-chips
              stack-label
              :rules="[
              val => val.length > 0 || 'Select at least one place',
              val => val.length <= 3 || 'Select no more than 3 places'
            ]"
          />
          <q-btn label="Register" type="submit" color="primary" />
        </q-form>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script>
// register into team
// - worker
// - input
// team id
// my name
// my location
// my prefer place (1~3)
// request
import { ref } from 'vue'
import { useQuasar } from 'quasar'

export default {
  name: 'TeamRegistration',
  setup() {
    const $q = useQuasar()

    const teamId = ref('')
    const name = ref('')
    const location = ref('')
    const preferredPlaces = ref([])
    const placeOptions = [
      'Office A', 'Office B', 'Office C',
      'Remote', 'Hybrid', 'Flexible'
    ]

    const onSubmit = () => {
      if (preferredPlaces.value.length < 1 || preferredPlaces.value.length > 3) {
        $q.notify({
          color: 'negative',
          message: 'Please select 1 to 3 preferred places'
        })
        return
      }

      // Here you would typically send this data to your backend
      const registrationData = {
        teamId: teamId.value,
        name: name.value,
        location: location.value,
        preferredPlaces: preferredPlaces.value
      }

      console.log('Registration submitted:', registrationData)

      // Simulating an API call
      setTimeout(() => {
        $q.notify({
          color: 'positive',
          message: 'Registration successful!'
        })
        // Reset form after successful submission
        teamId.value = ''
        name.value = ''
        location.value = ''
        preferredPlaces.value = []
      }, 1000)
    }

    return {
      teamId,
      name,
      location,
      preferredPlaces,
      placeOptions,
      onSubmit
    }
  }
}
</script>