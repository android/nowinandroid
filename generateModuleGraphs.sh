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
#
# Script to generate dependency graphs for each of the modules. The --exclude-module parameter can
# be used to exclude modules which are not part of the root dependency graph (and which, if included
# would cause the script to fail.
#
# Usage: generateModuleGraphs.sh --exclude-module :benchmarks --exclude-module :lint --exclude-module :ui-test-hilt-manifest

# Check if the dot command is available
if ! command -v dot &> /dev/null
then
    echo "The 'dot' command is not found. This is required to generate SVGs from the Graphviz files."
    echo "Installation instructions:"
    echo "  - On macOS: You can install Graphviz using Homebrew with the command: 'brew install graphviz'"
    echo "  - On Ubuntu: You can install Graphviz using APT with the command: 'sudo apt-get install graphviz'"
    exit 1
fi

# Check if the svgo command is available
if ! command -v svgo &> /dev/null
then
    echo "The 'svgo' command is not found. This is required to cleanup and compress SVGs."
    echo "Installation instructions available at https://github.com/svg/svgo."
    exit 1
fi

# Check for a version of grep which supports Perl regex.
# On MacOS the OS installed grep doesn't support Perl regex so check for the existence of the
# GNU version instead which is prefixed with 'g' to distinguish it from the OS installed version.
    if grep -P "" /dev/null > /dev/null 2>&1; then
    GREP_COMMAND=grep
elif command -v ggrep &> /dev/null; then
    GREP_COMMAND=ggrep
else
    echo "You don't have a version of 'grep' installed which supports Perl regular expressions."
    echo "On MacOS you can install one using Homebrew with the command: 'brew install grep'"
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

# Get the module paths
module_paths=$(${GREP_COMMAND} -oP 'include\("\K[^"]+' settings.gradle.kts)

# Ensure the output directory exists
mkdir -p docs/images/graphs/

# Function to check and create a README.md for modules which don't have one.
check_and_create_readme() {
    local module_path="$1"
    local file_name="$2"

    local readme_path="${module_path:1}" # Remove leading colon
    readme_path=${readme_path//:/\/} # Replace colons with slashes
    readme_path="${readme_path}/README.md" #Append the filename

    # Check if README.md exists and create it if not
    if [[ ! -f "$readme_path" ]]; then
        echo "Creating README.md for ${module_path}"

        # Determine the depth of the module based on the number of colons
        local depth=$(awk -F: '{print NF-1}' <<< "${module_path}")

        # Construct the relative image path with the correct number of "../"
        local relative_image_path="../"
        for ((i=1; i<$depth; i++)); do
            relative_image_path+="../"
        done
        relative_image_path+="docs/images/graphs/${file_name}.svg"

        echo "# ${module_path} module" > "$readme_path"
        echo "## Dependency graph" >> "$readme_path"
        echo "![Dependency graph](${relative_image_path})" >> "$readme_path"
    fi
}

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
        ./gradlew generateModulesGraphvizText \
          -Pmodules.graph.output.gv="/tmp/${file_name}.gv" \
          -Pmodules.graph.of.module="${module_path}" </dev/null

        # Convert to SVG using dot, and cleanup/compress using svgo
        dot -Tsvg "/tmp/${file_name}.gv" |
          svgo --multipass --pretty --output="docs/images/graphs/${file_name}.svg" -
        # Remove the temporary .gv file
        rm "/tmp/${file_name}.gv"
    fi
done
