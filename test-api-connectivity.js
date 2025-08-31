const axios = require('axios');

// Configuration
const API_BASE_URL = process.env.VITE_API_URL || 'http://localhost:8080';
const TEST_TIMEOUT = 5000;

// Create axios instance with similar config to frontend
const testClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: TEST_TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Test cases for API connectivity
const testCases = [
  {
    name: 'Fetch all tasks',
    method: 'GET',
    url: '/task',
    expectedStatus: [200, 401] // 401 is expected without auth
  },
  {
    name: 'Get task employees',
    method: 'GET', 
    url: '/task-employee/1/employees',
    expectedStatus: [200, 401, 404]
  },
  {
    name: 'Get user task requests',
    method: 'GET',
    url: '/account/1/task-requests',
    expectedStatus: [200, 401, 404]
  }
];

// Test runner
async function runConnectivityTests() {
  console.log(`🔗 Testing API connectivity to: ${API_BASE_URL}`);
  console.log('=' .repeat(60));
  
  const results = [];
  
  for (const test of testCases) {
    try {
      console.log(`🧪 Testing: ${test.name}`);
      
      const response = await testClient({
        method: test.method,
        url: test.url,
        data: test.data
      });
      
      const success = test.expectedStatus.includes(response.status);
      console.log(`  ✅ Status: ${response.status} ${success ? '(Expected)' : '(Unexpected)'}`);
      
      results.push({
        test: test.name,
        status: response.status,
        success: success,
        error: null
      });
      
    } catch (error) {
      const status = error.response?.status || 'No Response';
      const success = test.expectedStatus.includes(status);
      
      console.log(`  ${success ? '✅' : '❌'} Status: ${status} ${success ? '(Expected)' : '(Error)'}`);
      if (!success) {
        console.log(`     Error: ${error.message}`);
      }
      
      results.push({
        test: test.name,
        status: status,
        success: success,
        error: error.message
      });
    }
    
    console.log('');
  }
  
  // Summary
  console.log('📊 Test Summary:');
  console.log('=' .repeat(40));
  
  const passed = results.filter(r => r.success).length;
  const total = results.length;
  
  results.forEach(result => {
    const icon = result.success ? '✅' : '❌';
    console.log(`  ${icon} ${result.test}: ${result.status}`);
  });
  
  console.log('');
  console.log(`🎯 Results: ${passed}/${total} tests passed`);
  
  if (passed === total) {
    console.log('🎉 All API endpoints are accessible!');
  } else {
    console.log('⚠️  Some endpoints may need investigation.');
    console.log('💡 Note: 401 (Unauthorized) responses are expected for protected endpoints.');
  }
  
  return results;
}

// Self-executing test runner
if (require.main === module) {
  runConnectivityTests()
    .catch(error => {
      console.error('💥 Test runner failed:', error.message);
      process.exit(1);
    });
}

module.exports = { runConnectivityTests, testClient };