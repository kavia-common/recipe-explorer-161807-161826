CI notes:
- The Gradle wrapper is proxied at repo root via ./gradlew which delegates to android_frontend/gradlew.
- Ensure executable permission is set on both wrappers:
  chmod +x ./gradlew
  chmod +x ./android_frontend/gradlew
- If CI still fails to detect the build, run:
  ./gradlew :app:assembleDebug
