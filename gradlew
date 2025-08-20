#!/bin/sh
# Proxy wrapper to the actual Gradle wrapper located in android_frontend/
set -e
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR/android_frontend"
exec ./gradlew "$@"
