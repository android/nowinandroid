#!/usr/bin/env sh
set -e

OUTPUT_DIR="benchmarks/build/outputs/connected_android_test_additional_output"
JSON_REPORTS_DIR="benchmarks/build/json_reports"

run_benchmark() {
  VERSION_LABEL="$1"   # v1 or v2
  RUN_NUMBER="$2"      # 1..5

  echo "=============================="
  echo "Running benchmark for $VERSION_LABEL run $RUN_NUMBER"
  echo "=============================="

  # Clear app data to keep runs consistent
  adb shell pm clear com.google.samples.apps.nowinandroid || true

  # Ensure clean slate so only one JSON exists after run
  rm -rf "$OUTPUT_DIR"
  mkdir -p "$OUTPUT_DIR"
  
  # Run only the Startup benchmark
  # We might need to replace gradle with adb later to run the benchmark faster
  # but we will need to deal with making sure things are running correctly
  # and locating the output JSON files.
  ./gradlew :benchmarks:connectedDemoBenchmarkAndroidTest \
    -Pandroid.testInstrumentationRunnerArguments.class=com.google.samples.apps.nowinandroid.startup.StartupBenchmark \
    -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.suppressErrors=EMULATOR

  JSON_REPORT=$(find "$OUTPUT_DIR" -type f -name "*.json")
  COUNT=$(echo "$JSON_REPORT" | wc -l | tr -d ' ')

  if [ "$COUNT" -ne 1 ]; then
    echo "Error: Expected exactly 1 JSON file, found $COUNT"
    find "$OUTPUT_DIR" -type f -name "*.json"
    exit 1
  fi

  # Create JSON reports directory if it doesn't exist
  mkdir -p "$JSON_REPORTS_DIR"

  NEW_JSON_NAME="$JSON_REPORTS_DIR/benchmark_${VERSION_LABEL}_run${RUN_NUMBER}.json"
  cp "$JSON_REPORT" "$NEW_JSON_NAME"

  echo "Saved result to $NEW_JSON_NAME"
}

# Alternate runs: v1, v2, v1, v2 ...
for i in 1 2 3 4 5; do
  run_benchmark "v1" "$i"
  run_benchmark "v2" "$i"
done