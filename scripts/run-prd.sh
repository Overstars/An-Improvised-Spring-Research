#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
APP_DIR="$ROOT_DIR/spring-research"

cd "$APP_DIR"
./gradlew clean bootJar --info

nohup java -jar ./build/libs/spring-research-0.0.1-SNAPSHOT.jar \
  -Dspring.profiles.active=prd \
  -Dserver.port=9003 \
  -Dfile.encoding=UTF-8 \
  -verbose:gc \
  -XX:+PrintGCDetails > springboot.log 2>&1 &

echo "started, check log: $APP_DIR/springboot.log"
