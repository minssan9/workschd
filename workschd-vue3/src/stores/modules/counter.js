import { defineStore } from 'pinia';

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