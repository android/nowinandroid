#!/usr/bin/env sh
set -e

OUTPUT_DIR="benchmarks/build/outputs/connected_android_test_additional_output"
JSON_REPORTS_DIR="$OUTPUT_DIR/json_reports"

run_benchmark() {
  VERSION_LABEL="$1"   # v1 or v2
  RUN_NUMBER="$2"      # 1..5

  echo "=============================="
  echo "Running benchmark for $VERSION_LABEL run $RUN_NUMBER"
  echo "=============================="

  # Clear app data to keep runs consistent
  adb shell pm clear com.google.samples.apps.nowinandroid || true

  # Run only the Startup benchmark
  ./gradlew :benchmarks:connectedDemoBenchmarkAndroidTest \
    -Pandroid.testInstrumentationRunnerArguments.class=com.google.samples.apps.nowinandroid.startup.StartupBenchmark \
    -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.suppressErrors=EMULATOR

  mkdir -p "$JSON_REPORTS_DIR"

  # Find newest JSON result file
  LATEST_JSON=$(find "$OUTPUT_DIR" -name "*.json" -type f | xargs ls -t | head -n 1)

  if [ -z "$LATEST_JSON" ]; then
    echo "Error: No benchmark JSON file found"
    exit 1
  fi

  NEW_JSON_NAME="$JSON_REPORTS_DIR/benchmark_${VERSION_LABEL}_run${RUN_NUMBER}.json"
  cp "$LATEST_JSON" "$NEW_JSON_NAME"

  echo "Saved result to $NEW_JSON_NAME"
}

# Alternate runs: v1, v2, v1, v2 ...
for i in 1 2 3 4 5; do
  run_benchmark "v1" "$i"
  run_benchmark "v2" "$i"
done