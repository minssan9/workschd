<script setup>
</script>

<template>
  This is Home Page

  <p>Count: {{ count }}</p>

  <p>Double Count: {{ doubleCount }}</p>
  <button @click="increment">Increment</button>

</template>


<script>
import { defineComponent, computed } from 'vue';
import { useCounterStore } from '../stores/counter.js';

export default defineComponent({
  name: 'CounterComponent',
  setup() {
    const counterStore = useCounterStore();

    const count = computed(() => counterStore.count);
    const doubleCount = computed(() => counterStore.doubleCount);
    const increment = () => counterStore.increment();

    return {
      count,
      increment,
      doubleCount
    };
  }
});
</script>