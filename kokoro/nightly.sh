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

# Run the normal build, but replace the default virtual devices with physical ones.
# walleye     | Pixel 2       | API 27 | Phone
# gts4lltevzw | Galaxy Tab S4 | API 28 | Tablet
# a10         | Samsung A10   | API 29 | Phone
# redfin      | Pixel 5e      | API 30 | Phone
# oriole      | Pixel 6       | API 31 | Phone
bash $KOKORO_ARTIFACTS_DIR/git/nowinandroid/kokoro/build.sh "walleye,gts4lltevzw,a10,redfin,oriole" "27,28,29,30,31"

exit $?
