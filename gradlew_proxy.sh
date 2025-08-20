#!/usr/bin/env bash
# Simple proxy to call the Gradle wrapper located under android_frontend/
# Usage: ./gradlew_proxy.sh <gradle-args>
set -euo pipefail
cd "$(dirname "$0")/android_frontend"
exec ./gradlew "$@"
