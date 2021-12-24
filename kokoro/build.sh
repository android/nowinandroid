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

exit $?
