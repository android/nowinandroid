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

RED='\033[0;1;31m'
NC='\033[0m' # No Color

GIT_DIR=$(git rev-parse --git-dir 2> /dev/null)
GIT_ROOT=$(git rev-parse --show-toplevel 2> /dev/null)

echo "Installing git commit-message hook"
echo
curl -sSLo "${GIT_DIR}/hooks/commit-msg" \
    "https://gerrit-review.googlesource.com/tools/hooks/commit-msg" \
  && chmod +x "${GIT_DIR}/hooks/commit-msg"

echo "Installing git pre-push hook"
echo
mkdir -p "${GIT_DIR}/hooks/"
cp "${GIT_ROOT}/tools/pre-push" "${GIT_DIR}/hooks/pre-push" \
  && chmod +x "${GIT_DIR}/hooks/pre-push"

cat <<-EOF
Checking the following settings helps avoid miscellaneous issues:
  * Settings -> Editor -> General -> Remove trailing spaces on: Modified lines
  * Settings -> Editor -> General -> Ensure every saved file ends with a line break
  * Settings -> Editor -> General -> Auto Import -> Optimize imports on the fly (for both Kotlin\
 and Java)
EOF
