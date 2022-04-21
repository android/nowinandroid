#!/bin/bash

# Copyright 2021 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Fail on any error.
set -e
# Display commands to stderr.
set -x

deviceIds=${1:-'Pixel2,Pixel3'}
osVersionIds=${2:-'23,27,30'}

GRADLE_FLAGS=()
if [[ -n "$GRADLE_DEBUG" ]]; then
  GRADLE_FLAGS=( --debug --stacktrace )
fi

# Install the build tools and accept all licenses
export ANDROID_HOME=/opt/android-sdk/current
echo "Installing build-tools..."
echo y | ${ANDROID_HOME}/tools/bin/sdkmanager "build-tools;30.0.3" > /dev/null
echo y | ${ANDROID_HOME}/tools/bin/sdkmanager --licenses

cd $KOKORO_ARTIFACTS_DIR/git/nowinandroid

# The build needs Java 11, set it as the default Java version.
sudo update-java-alternatives --set java-1.11.0-openjdk-amd64

# Also clear JAVA_HOME variable so java -version is used instead
export JAVA_HOME=

./gradlew "${GRADLE_FLAGS[@]}" build

# For Firebase Test Lab
./gradlew assembleAndroidTest
./gradlew assembleDebug

MAX_RETRY=3
run_firebase_test_lab() {
  ## Retry can be done by passing the --num-flaky-test-attempts to gcloud, but gcloud SDK in the
  ## kokoro server doesn't support it yet.

  ## FTL requires a normal apk, even though we don't need or have any for library modules
  ## For now, just pass in the main app apk

  set +e # To not exit on an error to retry flaky tests
  local counter=0
  local result=1
  local module=$1
  while [ $result != 0 -a $counter -lt $MAX_RETRY ]; do
    gcloud firebase test android run \
      --type instrumentation \
      --app  "app/build/outputs/apk/debug/app-debug.apk" \
      --test "$module/build/outputs/apk/androidTest/debug/$module-debug-androidTest.apk" \
      --device-ids $deviceIds \
      --os-version-ids $osVersionIds \
      --locales en \
      --timeout 300
    result=$? ;
    let counter=counter+1
  done
  return $result
}


# All modules with androidTest to run tests on.
# This command will create a list like ["app", "sync"] based on which subdirectories
# (assumed to be modules) have an androidTest source directory.
# The sed regex pulls out the module name from the matched directory
modules=($(find . -regex ".*/src/androidTest" | sed -E 's|\./([^/]*)/.*|\1|g'))

# Run all modules in parallel with Firebase Test Lab, and fail if any fail
pids=""
result=0

for module in ${modules[@]}; do
  run_firebase_test_lab $module &
  pids="$pids $!"
done

for pid in ${pids[@]}; do
  wait $pid || let "result=1"
done

exit $result
