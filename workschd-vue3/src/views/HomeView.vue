<script setup>
</script>

<template>
  <div class="background-pattern"></div>
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
import {useCounterStore} from '@/stores/modules/counter.js';

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
<style lang="scss">
// Variables
$primary-color: #3498db;
$secondary-color: #2980b9;
$background-color: #f9f9f9;
$text-color: #333;
$font-family: 'Roboto', Arial, sans-serif;

body {
  font-family: $font-family;
  line-height: 1.6;
  background-color: $background-color;
  color: $text-color;
}

.container {
  max-width: 1200px;
  margin: auto;
  padding: 0 20px;
}

.header {
  background: linear-gradient(135deg, $primary-color, $secondary-color);
  padding: 60px 0;
  text-align: center;
  color: white;

  h1 {
    font-size: 2.5em;
    margin-bottom: 20px;
  }

  p {
    font-size: 1.2em;
    margin-bottom: 30px;
  }
}

.content {
  margin-top: 40px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.section {
  flex-basis: calc(33.333% - 20px);
  margin-bottom: 40px;
  background-color: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

  h2 {
    color: $primary-color;
    margin-bottom: 20px;
  }

  ul, ol {
    padding-left: 20px;
  }
}

.footer {
  text-align: center;
  margin-top: 60px;
  padding: 40px 0;
  background-color: #f0f0f0;
}

.button {
  background-color: $primary-color;
  border: none;
  color: white;
  padding: 12px 24px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  &:hover {
    background-color: $secondary-color;
  }
}

// Add a subtle background pattern
.background-pattern {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%239C92AC' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  z-index: -1;
  opacity: 0.6;
}

// Responsive design
@media (max-width: 768px) {
  .section {
    flex-basis: 100%;
  }

  .header {
    padding: 40px 0;

    h1 {
      font-size: 2em;
    }

    p {
      font-size: 1em;
    }
  }
}
</style>
