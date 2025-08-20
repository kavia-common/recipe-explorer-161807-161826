Android build notes:

This repository contains the Android Gradle wrapper under: android_frontend/gradlew

Build and install from that directory:
  cd android_frontend
  ./gradlew :app:assembleDebug
  ./gradlew :app:installDebug

Alternatively, from the repository root use the provided proxy script:
  ./gradlew_proxy.sh :app:assembleDebug
  ./gradlew_proxy.sh :app:installDebug
