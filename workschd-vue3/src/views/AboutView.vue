<template>

  <header class="bg-gray-800 text-white p-6">
    <div class="container mx-auto flex items-center justify-between">
      <h1 class="text-3xl font-bold">근무 스케줄표 자동 생성 서비스</h1>
      <div>
        <button class="bg-green-500 text-white px-4 py-2 rounded">지금 시작하기</button>
        <button class="bg-transparent border border-white text-white px-4 py-2 rounded ml-2">더 알아보기</button>
      </div>
    </div>
  </header>
  <main class="container mx-auto my-8">
    <section class="bg-white rounded shadow p-6">
      <h2 class="text-2xl font-bold mb-4">서비스 개요</h2>
      <div class="flex space-x-4">
        <div class="flex-1">
          <div class="flex items-center mb-2">
            <div class="bg-green-500 text-white w-8 h-8 flex items-center justify-center rounded-full">1</div>
            <p class="ml-4">직관적인 인터페이스</p>
          </div>
          <div class="flex items-center mb-2">
            <div class="bg-green-500 text-white w-8 h-8 flex items-center justify-center rounded-full">2</div>
            <p class="ml-4">실시간 동기화</p>
          </div>
          <div class="flex items-center">
            <div class="bg-green-500 text-white w-8 h-8 flex items-center justify-center rounded-full">3</div>
            <p class="ml-4">맞춤형 옵션</p>
          </div>
        </div>
        <div class="flex-1">
          <img src="https://placehold.co/400x300" alt="Service Overview Image" class="rounded shadow">
        </div>
      </div>
    </section>

    <section class="mt-8">
      <h2 class="text-2xl font-bold mb-4">주요 기능</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="bg-white p-4 rounded shadow">
          <h3 class="text-xl font-semibold mb-2">스케줄 자동화</h3>
          <p>직원 근무 패턴을 학습하여 최적의 스케줄을 자동으로 생성합니다.</p>
        </div>
        <div class="bg-white p-4 rounded shadow">
          <h3 class="text-xl font-semibold mb-2">실시간 알림</h3>
          <p>근무 시간 변경, 승인 요청 등의 알림을 실시간으로 전송합니다.</p>
        </div>
        <div class="bg-white p-4 rounded shadow">
          <h3 class="text-xl font-semibold mb-2">보고서 생성</h3>
          <p>근태 관리, 시간 외 근무 등의 보고서를 손쉽게 생성할 수 있습니다.</p>
        </div>
      </div>
    </section>

    <section class="mt-8 bg-white p-6 rounded shadow">
      <h2 class="text-2xl font-bold mb-4">사용 방법</h2>
      <div class="flex items-center space-x-4">
        <div class="flex-1">
          <ol class="list-decimal ml-5">
            <li class="mb-2">로그인</li>
            <li class="mb-2">스케줄 설정</li>
            <li class="mb-2">실시간 확인</li>
          </ol>
        </div>
        <div class="flex-1">
          <img src="https://placehold.co/400x300" alt="Usage Method Image" class="rounded shadow">
        </div>
      </div>
    </section>

    <section class="mt-8">
      <h2 class="text-2xl font-bold mb-4">장점 및 혜택</h2>
      <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
        <div class="bg-white p-4 rounded shadow">
          <h3 class="text-xl font-semibold mb-2">시각 환경</h3>
          <p>스케줄 관리에 소요되는 시간을 크게 줄일 수 있습니다.</p>
        </div>
        <div class="bg-white p-4 rounded shadow">
          <h3 class="text-xl font-semibold mb-2">팀워크 향상</h3>
          <p>팀원 간 신속한 소통으로 업무 효율이 향상됩니다.</p>
        </div>
        <div class="bg-white p-4 rounded shadow">
          <h3 class="text-xl font-semibold mb-2">유연한 보고</h3>
          <p>근태 및 근무 현황 보고가 자동화되어 편리합니다.</p>
        </div>
        <div class="bg-white p-4 rounded shadow">
          <h3 class="text-xl font-semibold mb-2">직원 만족도 UP</h3>
          <p>직원의 불만사항이 줄어들어 만족도가 향상됩니다.</p>
        </div>
      </div>
    </section>

    <section class="mt-8 bg-white p-6 rounded shadow">
      <h2 class="text-2xl font-bold mb-4">요금 정책</h2>
      <table class="w-full text-left table-auto">
        <thead>
        <tr>
          <th class="px-4 py-2">기준</th>
          <th class="px-4 py-2">Standard</th>
          <th class="px-4 py-2">Premium</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="border px-4 py-2">10인 이하 팀</td>
          <td class="border px-4 py-2">$9/user</td>
          <td class="border px-4 py-2">$25/user</td>
        </tr>
        <tr>
          <td class="border px-4 py-2">10-50인 팀</td>
          <td class="border px-4 py-2">$15/user</td>
          <td class="border px-4 py-2">$25/user</td>
        </tr>
        <tr>
          <td class="border px-4 py-2">50인 이상 팀</td>
          <td class="border px-4 py-2">$25/user</td>
          <td class="border px-4 py-2">$25/user</td>
        </tr>
        </tbody>
      </table>
    </section>

    <section class="mt-8 bg-white p-6 rounded shadow">
      <h2 class="text-2xl font-bold mb-4">고객 후기</h2>
      <div class="flex items-center space-x-4">
        <div class="flex-1">
          <p>"이전에는 팀의 스케줄 관리를 하는 것이 너무 복잡했지만, 이 서비스를 사용한 후 정말 편리해졌어요. 보고서 생성이 너무 편리해졌어요. 이제 근태와 시간 외 근무 내역을 손쉽게 확인할 수 있습니다."</p>
        </div>
        <div class="flex-1">
          <img src="https://placehold.co/300x300" alt="Customer Feedback Image" class="rounded shadow">
        </div>
      </div>
    </section>

    <section class="mt-8 bg-white p-6 rounded shadow">
      <h2 class="text-2xl font-bold mb-4">문의 및 지원</h2>
      <div class="flex items-center space-x-4">
        <div class="flex-1">
          <ol class="list-decimal ml-5">
            <li class="mb-2">궁금한 점이 있으신가요?</li>
            <li class="mb-2">24/7 고객 지원</li>
            <li class="mb-2">맞춤형 솔루션</li>
          </ol>
        </div>
        <div class="flex-1">
          <img src="https://placehold.co/400x300" alt="Support and Inquiry Image" class="rounded shadow">
        </div>
      </div>
    </section>
  </main>
  <footer class="bg-gray-800 text-white p-6 mt-8">
    <div class="container mx-auto text-center">
      <p>&copy; 2024 Work Schedule Automation Service. All rights reserved.</p>
    </div>
  </footer>

</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
<script setup lang="ts">
</script>