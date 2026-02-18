#!/usr/bin/env bash

set -euo pipefail

# Configuration & Defaults
NUMBER_OF_RUNS=1
PATH_APK_BASELINE=""
PATH_APK_CANDIDATE=""
PATH_APK_BENCHMARK=""
INSTRUMENT_PASSTHROUGH_ARGS=()
OUTPUT_DIR="./macrobenchmark_results"
TEST_RUNNER="androidx.test.runner.AndroidJUnitRunner"
EMULATOR_BENCHMARK_RESULT_DIR="/sdcard/Download"

# Cleanup
TEMP_DIR="$(mktemp -d)"
trap 'rm -rf "${TEMP_DIR}"' EXIT

die() {
  echo "err: $*" 1>&2
  exit 1
}

print_usage() {
  cat << EOF
Usage: $(basename "$0") [OPTIONS] --baseline_apk <path> --candidate_apk <path> --benchmark_apk <path> [-- INSTRUMENT_ARGS]

Automated benchmark script for APKs.

Options:
  -o, --output-dir <path>      Directory where benchmark results will be saved. (Default: "${OUTPUT_DIR}")
  --baseline-apk <path>        Path to the baseline APK file.
  --candidate-apk <path>       Path to the candidate APK file.
  --benchmark-apk <path>       Path to the benchmark APK. Must contain instrumented tests.
  -n, --runs <number>          Set number of runs per benchmark. (Default: 1)
  -h, --help                   Display this help message and exit.

Additional Arguments:
  --                           Everything after '--' is passed directly to
                               the adb instrumentation command.

Example:
  $(basename "$0") -o ./macrobenchmark_results --baseline_apk base.apk --candidate_apk candidate.apk -- -e androidx.benchmark.profiling.mode none
EOF
}

get_pkg_name() {
  local apk="${1}"
  apkanalyzer manifest application-id "${apk}"
}

install_apk() {
  local apk="${1}"
  echo "Installing APK: ${apk}"
  adb install -d "${apk}" > /dev/null || die "failed to install apk '${apk}'"
  adb shell pm clear "${APP_PKG_NAME}" > /dev/null 2>&1 || true
  adb shell pm clear "${BENCHMARK_PKG_NAME}" > /dev/null 2>&1 || true
  adb shell "rm -rf ${EMULATOR_BENCHMARK_RESULT_DIR} && mkdir -p ${EMULATOR_BENCHMARK_RESULT_DIR}" > /dev/null || true
}

run_benchmark() {
  echo "Running benchmarks..."
  adb shell am instrument -w \
    -e androidx.benchmark.suppressErrors EMULATOR \
    -e androidx.benchmark.profiling.mode none \
    -e no-isolated-storage true \
    -e additionalTestOutputDir "${EMULATOR_BENCHMARK_RESULT_DIR}" \
    "${INSTRUMENT_PASSTHROUGH_ARGS[@]}" \
    "${BENCHMARK_PKG_NAME}/$TEST_RUNNER"
}

write_benchmark_result() {
  local dest_path="${1}"
  local pull_temp="${TEMP_DIR}/pull_$(date +%s)"
  adb pull "${EMULATOR_BENCHMARK_RESULT_DIR}/." "${pull_temp}" > /dev/null
  mkdir -p $(dirname "${dest_path}") && mv "${pull_temp}/"*.json "${dest_path}"
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    -h|--help)
      print_usage
      exit 0
      ;;
    -o|--output-dir)
      OUTPUT_DIR="$2"
      shift 2
      ;;
    --baseline-apk)
      PATH_APK_BASELINE="$2"
      shift 2
      ;;
    --candidate-apk)
      PATH_APK_CANDIDATE="$2"
      shift 2
      ;;
    --benchmark-apk)
      PATH_APK_BENCHMARK="$2"
      shift 2
      ;;
    -n|--runs)
      NUMBER_OF_RUNS="$2"
      if ! [[ "$NUMBER_OF_RUNS" -eq "$NUMBER_OF_RUNS" ]] 2> /dev/null; then
          print_usage
          exit 1
      fi
      shift 2
      ;;
    --)
      shift
      INSTRUMENT_PASSTHROUGH_ARGS+=("$@")
      break
      ;;
    *)
      echo "$(basename "$0"): invalid option -- '$1'"
      echo "Try '$(basename "$0") --help' for more information"
      exit 1
      ;;
  esac
done

if [[ -z "${PATH_APK_BASELINE}" || -z "${PATH_APK_CANDIDATE}" || -z "${PATH_APK_BENCHMARK}" ]]; then
    print_usage
    exit 1
fi

APP_PKG_NAME=$(get_pkg_name "${PATH_APK_BASELINE}")
BENCHMARK_PKG_NAME=$(get_pkg_name "${PATH_APK_BENCHMARK}")

install_apk "${PATH_APK_BENCHMARK}"
for ((i=1; i<=${NUMBER_OF_RUNS}; i++)); do
  echo "--- Starting benchmark run (${i} / ${NUMBER_OF_RUNS}) ---"

  start_time=$SECONDS
  output_filename="${BENCHMARK_PKG_NAME}_$(date +"%Y-%m-%dT%H-%M-%S").json"

  # Baseline
  install_apk "${PATH_APK_BASELINE}"
  run_benchmark
  write_benchmark_result "${OUTPUT_DIR}/baseline/${output_filename}"

  # Candidate
  install_apk "${PATH_APK_CANDIDATE}"
  run_benchmark
  write_benchmark_result "${OUTPUT_DIR}/candidate/${output_filename}"

  duration=$((SECONDS - start_time))
  echo "--- Ending benchmark run (${i} / ${NUMBER_OF_RUNS}) took ${duration}s ---"
done

echo "Benchmark completed. Results in '$OUTPUT_DIR'"
