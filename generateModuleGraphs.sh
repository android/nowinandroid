#!/bin/bash
#
# Copyright 2024 The Android Open Source Project
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
#

# Script to generate dependency graphs for each of the modules
# Usage: generateModuleGraphs.sh --exclude-module :benchmarks --exclude-module :lint

# Echo each command so the caller knows what's going on
set -e

# Check if the dot command is available
if ! command -v dot &> /dev/null
then
    echo "The 'dot' command is not found. This is required to generate SVGs from the Graphviz files."
    echo "On macOS, you can install it using Homebrew: 'brew install graphviz'"
    exit 1
fi

# Initialize an array to store excluded modules
excluded_modules=()

# Parse command-line arguments for excluded modules
while [[ $# -gt 0 ]]; do
    case "$1" in
        --exclude-module)
            excluded_modules+=("$2")
            shift # Past argument
            shift # Past value
            ;;
        *)
            echo "Unknown parameter passed: $1"
            exit 1
            ;;
    esac
done

# Function to check and create README.md
check_and_create_readme() {
    local module_path="$1"
    local file_name="$2"

    local readme_path="${module_path:1}" # Remove leading colon
    readme_path=$(echo "$readme_path" | sed 's/:/\//g') # Replace colons with slashes using sed
    readme_path="${readme_path}/README.md"

    # Check if README.md exists and create it if not
    if [[ ! -f "$readme_path" ]]; then
        echo "Creating README.md for ${module_path}"
        # Calculate the correct relative path to the image
        local relative_image_path="../../docs/images/graphs/${file_name}.svg"
        echo "# ${module_path} module" > "$readme_path"
        echo "" >> "$readme_path"
        echo "![Dependency graph](${relative_image_path})" >> "$readme_path"
    fi
}

# Get the module paths
module_paths=$(./gradlew -q printModulePaths --no-configuration-cache)

# Loop through each module path
echo "$module_paths" | while read -r module_path; do
    # Check if the module is in the excluded list
    if [[ ! " ${excluded_modules[@]} " =~ " ${module_path} " ]]; then
        # Derive the filename from the module path
        file_name="dep_graph${module_path//:/_}" # Replace colons with underscores
        file_name="${file_name//-/_}" # Replace dashes with underscores

        check_and_create_readme "$module_path" "$file_name"

        # Generate the .gv file in a temporary location
        # </dev/null is used to stop ./gradlew from consuming input which prematurely ends the while loop
        ./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv="/tmp/${file_name}.gv" -Pmodules.graph.of.module="${module_path}" </dev/null
        # Convert to SVG using dot
        dot -Tsvg "/tmp/${file_name}.gv" > "docs/images/graphs/${file_name}.svg"
        # Remove the temporary .gv file
        rm "/tmp/${file_name}.gv"
    fi
done