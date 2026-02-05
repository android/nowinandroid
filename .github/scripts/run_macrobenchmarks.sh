#!/usr/bin/env bash

set -euo pipefail

APP_PKG="com.google.samples.apps.nowinandroid"
BENCHMARK_PKG="com.google.samples.apps.nowinandroid.benchmarks"
TEST_RUNNER="androidx.test.runner.AndroidJUnitRunner"
JSON_REPORTS_DIR="benchmarks/json_reports"

PATH_APK_VERSION_1="$1"
PATH_APK_VERSION_2="$2"

install_apk() {
  adb install -r "$1"

  # Clear app data and prepare storage
  adb shell pm clear "$APP_PKG" || true
  adb shell mkdir -p /sdcard/Download
  adb shell rm /sdcard/Download/*.json || true
  adb shell rm /sdcard/Download/*.perfetto-trace || true
  adb shell rm /sdcard/Download/*.txt || true
}

run_benchmark() {
  VERSION_LABEL="$1"   # v1 or v2
  RUN_NUMBER="$2"      # 1..5

  echo "=============================="
  echo "Running benchmark for $VERSION_LABEL run $RUN_NUMBER"
  echo "=============================="

  # Run only the Startup Baseline Profile benchmark
  adb shell am instrument -w \
    -e class com.google.samples.apps.nowinandroid.startup.StartupBenchmark#startupPrecompiledWithBaselineProfile \
    -e androidx.benchmark.suppressErrors EMULATOR \
    -e androidx.benchmark.profiling.mode none \
    -e no-isolated-storage true \
    -e additionalTestOutputDir /sdcard/Download \
    "$BENCHMARK_PKG/$TEST_RUNNER"

  # Ensure the local directory exists for the pull
  mkdir -p "$JSON_REPORTS_DIR/tmp_results"

  # Pull the benchmarks output produced on the device
  adb pull /sdcard/Download/. "$JSON_REPORTS_DIR/tmp_results"

  # Take only the generated JSON file (ignore perfetto traces and text files)
  # There should only be one JSON file because of the rm at the start
  NEW_JSON_NAME="$JSON_REPORTS_DIR/benchmark_${VERSION_LABEL}_run${RUN_NUMBER}.json"
  mv "$JSON_REPORTS_DIR/tmp_results/"*.json "$NEW_JSON_NAME"
  rm -rf "$JSON_REPORTS_DIR/tmp_results"

  echo "Saved result to $NEW_JSON_NAME"
}

# Alternate runs: v1, v2, v1, v2 ...
for i in {1..5}; do
  install_apk "$PATH_APK_VERSION_1"
  run_benchmark "v1" "$i"

  install_apk "$PATH_APK_VERSION_2"
  run_benchmark "v2" "$i"
done
