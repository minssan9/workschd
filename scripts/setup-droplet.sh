#!/bin/bash

##############################################################################
# Digital Ocean Droplet Setup Script
#
# This script prepares a fresh Digital Ocean droplet for deploying Workschd
#
# Run this script ON THE DROPLET as root or with sudo
##############################################################################

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Setting up Digital Ocean Droplet${NC}"
echo -e "${GREEN}========================================${NC}"

# Update system
echo -e "${YELLOW}[1/7] Updating system packages...${NC}"
apt-get update
apt-get upgrade -y

# Install Docker
echo -e "${YELLOW}[2/7] Installing Docker...${NC}"
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com -o get-docker.sh
    sh get-docker.sh
    rm get-docker.sh
    systemctl enable docker
    systemctl start docker
else
    echo "Docker already installed"
fi

# Install Docker Compose
echo -e "${YELLOW}[3/7] Installing Docker Compose...${NC}"
if ! command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE_VERSION="v2.24.0"
    curl -L "https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
else
    echo "Docker Compose already installed"
fi

# Install other dependencies
echo -e "${YELLOW}[4/7] Installing dependencies...${NC}"
apt-get install -y \
    git \
    curl \
    wget \
    vim \
    ufw \
    certbot \
    python3-certbot-nginx

# Configure firewall
echo -e "${YELLOW}[5/7] Configuring firewall...${NC}"
ufw --force enable
ufw allow 22/tcp   # SSH
ufw allow 80/tcp   # HTTP
ufw allow 443/tcp  # HTTPS

# Create deployment directory
echo -e "${YELLOW}[6/7] Creating deployment directory...${NC}"
DEPLOY_USER=${1:-deploy}
DEPLOY_PATH="/home/${DEPLOY_USER}/workschd"

# Create user if not exists
if ! id "$DEPLOY_USER" &>/dev/null; then
    useradd -m -s /bin/bash "$DEPLOY_USER"
    usermod -aG docker "$DEPLOY_USER"
fi

# Create directories
mkdir -p "$DEPLOY_PATH"
mkdir -p "$DEPLOY_PATH/logs/nginx"
mkdir -p "$DEPLOY_PATH/logs/backend"
mkdir -p "$DEPLOY_PATH/nginx/ssl"
mkdir -p "$DEPLOY_PATH/releases"

chown -R "$DEPLOY_USER:$DEPLOY_USER" "$DEPLOY_PATH"

# Install Nginx (for SSL termination)
echo -e "${YELLOW}[7/7] Installing Nginx...${NC}"
if ! command -v nginx &> /dev/null; then
    apt-get install -y nginx
    systemctl enable nginx
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Droplet setup completed!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Next steps:"
echo "1. Set up SSH key authentication for $DEPLOY_USER"
echo "2. Configure .env.production with your droplet details"
echo "3. Run ./scripts/deploy.sh production from your local machine"
echo ""
echo "For SSL certificate (Let's Encrypt):"
echo "  certbot --nginx -d your-domain.com"
