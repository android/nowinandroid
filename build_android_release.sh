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

export ANDROID_HOME="$(cd $DIR/../../../prebuilts/fullsdk/linux && pwd )"

echo "ANDROID_HOME=$ANDROID_HOME"
cd $DIR

# Build
GRADLE_PARAMS=" --stacktrace"
$DIR/gradlew :app:clean :app:assemble ${GRADLE_PARAMS}
BUILD_RESULT=$?

# Demo debug
cp $APP_OUT/apk/demo/debug/app-demo-debug.apk $DIST_DIR

# Demo release
cp $APP_OUT/apk/demo/release/app-demo-release.apk $DIST_DIR

# Prod debug
cp $APP_OUT/apk/prod/debug/app-prod-debug.apk $DIST_DIR/app-prod-debug.apk

# Prod release
cp $APP_OUT/apk/prod/release/app-prod-release.apk $DIST_DIR/app-prod-release.apk
#cp $APP_OUT/mapping/release/mapping.txt $DIST_DIR/mobile-release-apk-mapping.txt

# Build App Bundles
# Don't clean here, otherwise all apks are gone.
$DIR/gradlew :app:bundle ${GRADLE_PARAMS}

# Demo debug
cp $APP_OUT/bundle/demoDebug/app-demo-debug.aab $DIST_DIR/app-demo-debug.aab

# Demo release
cp $APP_OUT/bundle/demoRelease/app-demo-release.aab $DIST_DIR/app-demo-release.aab

# Prod debug
cp $APP_OUT/bundle/prodDebug/app-prod-debug.aab $DIST_DIR/app-prod-debug.aab

# Prod release
cp $APP_OUT/bundle/prodRelease/app-prod-release.aab $DIST_DIR/app-prod-release.aab
#cp $APP_OUT/mapping/prodRelease/mapping.txt $DIST_DIR/mobile-release-aab-mapping.txt
BUILD_RESULT=$?

exit $BUILD_RESULT