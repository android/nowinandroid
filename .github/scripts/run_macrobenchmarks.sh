#!/usr/bin/env bash

set -euo pipefail

# TODO: pass number of runs as a commandline argument
NUMBER_OF_RUNS=2

APP_PKG="com.google.samples.apps.nowinandroid"
BENCHMARK_PKG="com.google.samples.apps.nowinandroid.benchmarks"
TEST_RUNNER="androidx.test.runner.AndroidJUnitRunner"
EMULATOR_BENCHMARK_RESULT_DIR="/sdcard/Download"

PATH_APK_BASELINE="${1:-}"
PATH_APK_CANDIDATE="${2:-}"
OUTPUT_DIR="${3:-./macrobenchmark_results}"

TEMP_DIR="$(mktemp -d)"
trap 'rm -rf "${TEMP_DIR}"' EXIT

install_apk() {
  local apk_path="${1}"

  adb install -r "${apk_path}"

  adb shell pm clear "$APP_PKG" || true
  adb shell pm clear "${BENCHMARK_PKG}" || true
  adb shell rm -rf "${EMULATOR_BENCHMARK_RESULT_DIR}" || true
  adb shell mkdir -p "${EMULATOR_BENCHMARK_RESULT_DIR}"
}

run_benchmark() {
  adb shell am instrument -w \
    -e class com.google.samples.apps.nowinandroid.startup.StartupBenchmark#startupPrecompiledWithBaselineProfile \
    -e androidx.benchmark.suppressErrors EMULATOR \
    -e androidx.benchmark.profiling.mode none \
    -e no-isolated-storage true \
    -e additionalTestOutputDir "${EMULATOR_BENCHMARK_RESULT_DIR}" \
    "$BENCHMARK_PKG/$TEST_RUNNER"
}

write_benchmark_result() {
  local output_path="${1}"

  adb pull "${EMULATOR_BENCHMARK_RESULT_DIR}/." "${TEMP_DIR}/pull_out/"

  mv "${TEMP_DIR}/pull_out/"*.json "${output_path}"
  rm -rf "${TEMP_DIR}/pull_out/"
}

if [[ -z "${PATH_APK_BASELINE}" || -z "${PATH_APK_CANDIDATE}" ]]; then
    echo "Usage: $0 <path_to_baseline.apk> <path_to_candidate.apk> [output_dir]"
    exit 1
fi

mkdir -p "${OUTPUT_DIR}/baseline" "${OUTPUT_DIR}/candidate"

# Alternate runs: v1, v2, v1, v2 ...
for ((i=1; i<=${NUMBER_OF_RUNS}; i++)); do
  start_time=$(date +%s)

  timestamp=$(date +"%Y-%m-%dT%H-%M-%S")
  output_filename="${BENCHMARK_PKG}_${timestamp}.json"
  baseline_output_path="${OUTPUT_DIR}/baseline/${output_filename}"
  candidate_output_path="${OUTPUT_DIR}/candidate/${output_filename}"

  echo "=============================="
  echo "Start iteration (${i} / ${NUMBER_OF_RUNS})"
  echo "=============================="

  echo "Starting Baseline Benchmark:"
  echo "    >> APK file        : ${PATH_APK_BASELINE}"
  echo "    >> Output file path: ${baseline_output_path}"

  install_apk "${PATH_APK_BASELINE}"
  run_benchmark
  write_benchmark_result "${baseline_output_path}"

  echo "Starting Candidate Benchmark:"
  echo "    >> APK file        : ${PATH_APK_CANDIDATE}"
  echo "    >> Output file path: ${candidate_output_path}"

  install_apk "${PATH_APK_CANDIDATE}"
  run_benchmark
  write_benchmark_result "${candidate_output_path}"

  end_time=$(date +%s)
  duration=$((end_time - start_time))

  echo "=============================="
  echo "End iteration (${i} / ${NUMBER_OF_RUNS}) took ${duration}s"
  echo "=============================="
done
