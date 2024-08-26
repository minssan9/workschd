<script setup>
</script>

<template>
  <div class="header">
    <h1>근무 스케줄표 자동 생성 서비스</h1>
    <p>효율적인 근무 관리를 위한 스케줄 자동화 솔루션</p>
    <a href="https://gamma.app" class="button">지금 시작하기</a>
    <a href="https://gamma.app" class="button">더 알아보기</a>
  </div>

  <div class="content">
    <div class="section">
      <h2>서비스 개요</h2>
      <ul>
        <li>직관적인 인터페이스: 사용자 친화적인 디자인으로 누구나 쉽게 사용할 수 있습니다.</li>
        <li>실시간 동기화: 모든 팀원의 스케줄이 실시간으로 공유되어 효율적인 협업이 가능합니다.</li>
        <li>맞춤형 옵션: 개인과 팀의 니즈에 맞춰 다양한 설정 옵션을 제공합니다.</li>
      </ul>
    </div>

    <div class="section">
      <h2>주요 기능</h2>
      <p>스케줄 자동화: 직원 근무 패턴을 학습하여 최적의 스케줄을 자동으로 생성합니다.</p>
      <p>실시간 알림: 근무 시간 변경, 승인 요청 등의 알림을 실시간으로 전송합니다.</p>
      <p>보고서 생성: 근태 관리, 시간 외 근무 등의 보고서를 손쉽게 생성할 수 있습니다.</p>
    </div>

    <div class="section">
      <h2>사용 방법</h2>
      <ol>
        <li>웹사이트나 모바일 앱에 로그인하세요.</li>
        <li>개인 및 팀 근무 패턴을 입력하여 스케줄을 설정할 수 있습니다.</li>
        <li>언제 어디서든 실시간으로 근무 스케줄을 확인할 수 있습니다.</li>
      </ol>
    </div>
  </div>

  <div class="footer">
    <p>문의 및 지원: 언제든 연락주세요. 친절하게 상담해 드리겠습니다.</p>
    <a href="https://gamma.app" class="button">문의하기</a>
  </div>

  This is Home Page

  <p>Count: {{ count }}</p>

  <p>Double Count: {{ doubleCount }}</p>
  <button @click="increment">Increment</button>

</template>

<script>
import {defineComponent, computed} from 'vue';
import {useCounterStore} from '../stores/counter.js';

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
<style scoped>
body {
  font-family: Arial, sans-serif;
  line-height: 1.6;
}

.container {
  max-width: 800px;
  margin: auto;
}

.header {
  background: #f0f0f0;
  padding: 20px;
  text-align: center;
}

.content {
  margin-top: 20px;
}

.section {
  margin-bottom: 20px;
}

.section h2 {
  color: #333;
}

.footer {
  text-align: center;
  margin-top: 30px;
}

.button {
  background-color: #4CAF50;
  border: none;
  color: white;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
}
</style>