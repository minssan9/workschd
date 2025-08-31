#!/bin/bash

# API connectivity test script
API_BASE_URL=${VITE_API_URL:-"http://localhost:8080"}

echo "🔗 Testing API connectivity to: $API_BASE_URL"
echo "============================================="

# Function to test API endpoint
test_endpoint() {
    local name="$1"
    local method="$2" 
    local endpoint="$3"
    local expected_codes="$4"
    
    echo "🧪 Testing: $name"
    echo "   Method: $method $endpoint"
    
    # Make request and capture response code
    response_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -X "$method" \
        -H "Content-Type: application/json" \
        -H "Accept: application/json" \
        --max-time 10 \
        "$API_BASE_URL$endpoint" 2>/dev/null)
    
    # Check if response code is in expected codes
    if echo "$expected_codes" | grep -q "$response_code"; then
        echo "   ✅ Status: $response_code (Expected)"
    else
        echo "   ❌ Status: $response_code (Expected: $expected_codes)"
    fi
    
    echo ""
    return 0
}

# Test cases
test_endpoint "Fetch all tasks" "GET" "/task" "200 401"
test_endpoint "Get task employees (task ID 1)" "GET" "/task-employee/1/employees" "200 401 404"
test_endpoint "Get user task requests (account ID 1)" "GET" "/account/1/task-requests" "200 401 404"
test_endpoint "Create task request (task ID 1)" "POST" "/task/1/request?accountId=1" "200 401 404 409"
test_endpoint "Health check (if available)" "GET" "/actuator/health" "200 404"

echo "📊 Test Summary:"
echo "================"
echo "💡 Note: 401 (Unauthorized) responses are expected for protected endpoints"
echo "💡 Note: 404 (Not Found) responses may indicate server is not running"
echo ""
echo "🎯 If you see mostly 404 responses, the API server may not be running"
echo "🔧 To start the API server, run: cd workschd-api && ./gradlew bootRun"