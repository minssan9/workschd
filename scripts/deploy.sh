#!/bin/bash

##############################################################################
# Workschd Deployment Script for Digital Ocean Droplet
#
# This script deploys the application to a Digital Ocean droplet using SSH
# and Docker Compose.
#
# Prerequisites:
#   - SSH access to the droplet
#   - Docker and Docker Compose installed on the droplet
#   - Environment variables configured (see .env.production)
#
# Usage:
#   ./scripts/deploy.sh [environment]
#
#   environment: production|staging (default: production)
##############################################################################

set -e  # Exit on error

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
ENVIRONMENT=${1:-production}
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Load environment-specific configuration
ENV_FILE="$PROJECT_ROOT/.env.$ENVIRONMENT"
if [ ! -f "$ENV_FILE" ]; then
    echo -e "${RED}Error: Environment file $ENV_FILE not found${NC}"
    exit 1
fi

# Source environment variables
source "$ENV_FILE"

# Validate required environment variables
REQUIRED_VARS=(
    "DROPLET_IP"
    "DROPLET_USER"
    "DROPLET_SSH_KEY"
    "DEPLOY_PATH"
)

for var in "${REQUIRED_VARS[@]}"; do
    if [ -z "${!var}" ]; then
        echo -e "${RED}Error: Required environment variable $var is not set${NC}"
        exit 1
    fi
done

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Starting deployment to $ENVIRONMENT${NC}"
echo -e "${GREEN}========================================${NC}"

# Step 1: Build frontend locally (optional - can also build on server)
echo -e "${YELLOW}[1/6] Building Vue3 frontend...${NC}"
cd "$PROJECT_ROOT/workschd-vue3"
npm ci
npm run build
cd "$PROJECT_ROOT"

# Step 2: Build backend locally (optional - can also build on server)
echo -e "${YELLOW}[2/6] Building Spring Boot backend...${NC}"
cd "$PROJECT_ROOT/workschd-api"
./gradlew clean build -x test
cd "$PROJECT_ROOT"

# Step 3: Create deployment package
echo -e "${YELLOW}[3/6] Creating deployment package...${NC}"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
DEPLOY_PACKAGE="workschd_${ENVIRONMENT}_${TIMESTAMP}.tar.gz"

# Files to include in deployment package
tar -czf "/tmp/$DEPLOY_PACKAGE" \
    --exclude='node_modules' \
    --exclude='.git' \
    --exclude='*.log' \
    --exclude='workschd-vue3/dist' \
    docker-compose.yml \
    nginx/ \
    workschd-api/ \
    workschd-vue3/Dockerfile \
    .env.${ENVIRONMENT}

# Step 4: Upload to droplet
echo -e "${YELLOW}[4/6] Uploading to droplet...${NC}"
ssh -i "$DROPLET_SSH_KEY" "${DROPLET_USER}@${DROPLET_IP}" "mkdir -p ${DEPLOY_PATH}/releases"
scp -i "$DROPLET_SSH_KEY" "/tmp/$DEPLOY_PACKAGE" "${DROPLET_USER}@${DROPLET_IP}:${DEPLOY_PATH}/releases/"

# Step 5: Extract and prepare on droplet
echo -e "${YELLOW}[5/6] Extracting and preparing on droplet...${NC}"
ssh -i "$DROPLET_SSH_KEY" "${DROPLET_USER}@${DROPLET_IP}" << EOF
    set -e
    cd ${DEPLOY_PATH}

    # Extract new release
    tar -xzf releases/${DEPLOY_PACKAGE} -C releases/

    # Stop existing containers
    if [ -f docker-compose.yml ]; then
        docker-compose down || true
    fi

    # Copy new files
    cp -r releases/docker-compose.yml .
    cp -r releases/nginx ./nginx_new
    rm -rf nginx
    mv nginx_new nginx

    # Copy environment file
    cp releases/.env.${ENVIRONMENT} .env

    # Create necessary directories
    mkdir -p logs/nginx logs/backend nginx/ssl

    # Set proper permissions
    chmod -R 755 nginx
EOF

# Step 6: Deploy with Docker Compose
echo -e "${YELLOW}[6/6] Deploying with Docker Compose...${NC}"
ssh -i "$DROPLET_SSH_KEY" "${DROPLET_USER}@${DROPLET_IP}" << 'EOF'
    set -e
    cd ${DEPLOY_PATH}

    # Pull latest images (if using registry)
    # docker-compose pull

    # Build and start services
    docker-compose build --no-cache
    docker-compose up -d

    # Wait for services to be healthy
    echo "Waiting for services to start..."
    sleep 10

    # Check service status
    docker-compose ps

    # Show logs
    echo -e "\n${GREEN}Recent logs:${NC}"
    docker-compose logs --tail=50
EOF

# Cleanup
rm -f "/tmp/$DEPLOY_PACKAGE"

# Health check
echo -e "${YELLOW}Performing health check...${NC}"
sleep 5

if curl -f -s "http://${DROPLET_IP}/api/health" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Backend health check passed${NC}"
else
    echo -e "${YELLOW}⚠ Backend health check failed (might still be starting)${NC}"
fi

if curl -f -s "http://${DROPLET_IP}/" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Frontend health check passed${NC}"
else
    echo -e "${YELLOW}⚠ Frontend health check failed (might still be starting)${NC}"
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Deployment completed successfully!${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "Application URL: http://${DROPLET_IP}"
echo -e "To view logs: ssh -i ${DROPLET_SSH_KEY} ${DROPLET_USER}@${DROPLET_IP} 'cd ${DEPLOY_PATH} && docker-compose logs -f'"
