#!/bin/bash
set -e

AUTH_APP="jywon1128/jjubull-auth"
RESOURCE_APP="jywon1128/jjubull-resource"
TAG=$1
COLOR_FILE=./color

if [ -z "$TAG" ]; then
  echo "Usage: ./deploy.sh <TAG>"
  exit 1
fi

if [ ! -f "$COLOR_FILE" ]; then
  echo "blue" > "$COLOR_FILE"
fi

CURRENT=$(cat "$COLOR_FILE")

if [ "$CURRENT" = "blue" ]; then
  NEW="green"
else
  NEW="blue"
fi

docker pull "$AUTH_APP:$TAG"
docker pull "$RESOURCE_APP:$TAG"

export AUTH_APP RESOURCE_APP TAG

# 새 색 스택 올리기
docker compose up -d auth-"$NEW" resource-"$NEW"

# nginx 라우팅 전환
sudo ln -sf "/etc/nginx/sites-available/auth-${NEW}.conf" /etc/nginx/sites-enabled/auth.conf
sudo ln -sf "/etc/nginx/sites-available/resource-${NEW}.conf"  /etc/nginx/sites-enabled/resource.conf
sudo nginx -t
sudo systemctl reload nginx

# 이전 색 내리기
docker compose stop auth-"$CURRENT" resource-"$CURRENT"

echo "$NEW" > "$COLOR_FILE"

echo "[CLEANUP] cleaning unused docker resources..."
docker container prune -f
docker image prune -af
docker volume prune -f

