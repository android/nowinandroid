#!/usr/bin/env bash

#
# Copyright 2022 The Android Open Source Project
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       https://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#

# IGNORE this file, it's only used in the internal Google release process

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
APP_OUT=$DIR/app/build/outputs

export JAVA_HOME="$(cd $DIR/../nowinandroid-prebuilts/jdk17/linux && pwd )"
echo "JAVA_HOME=$JAVA_HOME"

export ANDROID_HOME="$(cd $DIR/../../../prebuilts/fullsdk/linux && pwd )"
echo "ANDROID_HOME=$ANDROID_HOME"

echo "Copying google-services.json"
cp $DIR/../nowinandroid-prebuilts/google-services.json $DIR/app

echo "Copying local.properties"
cp $DIR/../nowinandroid-prebuilts/local.properties $DIR

cd $DIR

# Build the prodRelease variant
GRADLE_PARAMS=" --stacktrace -Puse-google-services"
$DIR/gradlew :app:clean :app:assembleProdRelease :app:bundleProdRelease ${GRADLE_PARAMS}
BUILD_RESULT=$?

# Prod release apk
cp $APP_OUT/apk/prod/release/app-prod-release.apk $DIST_DIR/app-prod-release.apk
# Prod release bundle
cp $APP_OUT/bundle/prodRelease/app-prod-release.aab $DIST_DIR/app-prod-release.aab
# Prod release bundle mapping
cp $APP_OUT/mapping/prodRelease/mapping.txt $DIST_DIR/mobile-release-aab-mapping.txt

exit $BUILD_RESULT
