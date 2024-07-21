import { defineStore } from 'pinia';

// const useCounterStore = defineStore('counter', () => {
//   const count = ref(0)
//   const doubleCount = computed(() => count.value * 2)
//
//   // actions
//   function increment() {
//     count.value++
//   }
//
//
//   return { count, doubleCount, increment }
// })


// stores/counterState.js
const state = () => ({
  count: 0
});

// stores/counterActions.js
const actions = {
  increment() {
    this.count++;
  },
  decrement() {
    this.count--;
  }
};


export const useCounterStore = defineStore('counter', {
  state,
  actions
});