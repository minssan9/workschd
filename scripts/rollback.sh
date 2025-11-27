#!/bin/bash

##############################################################################
# Workschd Rollback Script
#
# This script rolls back to the previous deployment
##############################################################################

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

ENVIRONMENT=${1:-production}
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Load environment-specific configuration
ENV_FILE="$PROJECT_ROOT/.env.$ENVIRONMENT"
if [ ! -f "$ENV_FILE" ]; then
    echo -e "${RED}Error: Environment file $ENV_FILE not found${NC}"
    exit 1
fi

source "$ENV_FILE"

# Check if sshpass is installed
if ! command -v sshpass &> /dev/null; then
    echo -e "${RED}Error: sshpass is not installed${NC}"
    exit 1
fi

echo -e "${YELLOW}Rolling back to previous version...${NC}"

sshpass -p "$DROPLET_PASSWORD" ssh -o StrictHostKeyChecking=no "${DROPLET_USER}@${DROPLET_IP}" << EOF
    set -e
    cd ${DEPLOY_PATH}

    # Stop current containers
    docker-compose down

    # Restore previous version (if exists)
    if [ -d "previous" ]; then
        cp -r previous/* .
        docker-compose up -d
        echo -e "${GREEN}Rollback completed${NC}"
    else
        echo -e "${RED}No previous version found${NC}"
        exit 1
    fi
EOF
