import argparse
import json
import math
import sys
from pathlib import Path

# ----------- CONFIG -----------
BENCHMARK_NAME = "startupPrecompiledWithBaselineProfile"
METRIC_KEY = "timeToInitialDisplayMs"
# ------------------------------

def step_fit(a, b):
    def sum_squared_error(values):
        avg = sum(values) / len(values)
        return sum((v - avg) ** 2 for v in values)

    if not a or not b:
            return 0.0

    total_squared_error = sum_squared_error(a) + sum_squared_error(b)
    step_error = math.sqrt(total_squared_error) / (len(a) + len(b))
    if step_error == 0.0:
        return 0.0

    return (sum(a) / len(a) - sum(b) / len(b)) / step_error

def extract_median_from_files(paths):
    medians = []

    for path in paths:
        with open(path, "r") as f:
            data = json.load(f)

        found = False
        for bench in data.get("benchmarks", []):
            if bench.get("name") == BENCHMARK_NAME:
                metrics = bench.get("metrics", {})
                metric = metrics.get(METRIC_KEY, {})
                medians.append(metric.get("median"))
                found = True

        if not found:
            raise ValueError(f"Metric not found in {path}")

    return medians

def main():
    parser = argparse.ArgumentParser(prog='Comperator', description='Compare between multiple macrobenchmark test results')
    parser.add_argument('baseline_dir', help='Baseline macrobenchmark reports directory')
    parser.add_argument('candidate_dir', help='Candidate macrobenchmark reports directory')
    args = parser.parse_args()

    baseline_dir = Path(args.baseline_dir)
    candidate_dir = Path(args.candidate_dir)
    baseline_files = sorted(baseline_dir.glob("*.json"))
    candidate_files = sorted(candidate_dir.glob("*.json"))

    if len(baseline_files) <= 0:
        print('ERR: baseline has no macrobenchmark results', file=sys.stderr)
        exit(1)

    if len(candidate_files) <= 0:
        print('ERR: candidate has no macrobenchmark results', file=sys.stderr)
        exit(1)

    min_len = min(len(baseline_files), len(candidate_files))
    if len(baseline_files) != len(candidate_files):
        print(f"WARN: Length mismatch, using first {min_len} samples. baseline: {len(baseline_files)}, candidate: {len(candidate_files)}")

    print('Macrobenchmark Result Mapping:')
    print('| Index | Baseline | Candidate |')
    print('--------------------------------')

    mismatch_count = 0
    for i in range(min_len):
        baseline_filename = baseline_files[i].name.upper()
        candidate_filename = candidate_files[i].name.upper()
        if baseline_filename != candidate_filename:
            mismatch_count += 1
            print('* ', end='')
        print(f'{i + 1} {baseline_files[i]} <-> {candidate_files[i]}')

    print('--------------------------------')
    print(f'# Match   : {min_len - mismatch_count}')
    print(f'# Mismatch: {mismatch_count}')
    if mismatch_count > 0:
        print("WARN: filename mapping mismatch detected. Output prediction may be incorrect")
    print()

    baseline_medians  = extract_median_from_files(baseline_files[:min_len])
    candidate_medians = extract_median_from_files(candidate_files[:min_len])
    assert (len(baseline_medians) == len(candidate_medians))

    print(f"Benchmark        : {BENCHMARK_NAME}")
    print(f"Metric           : {METRIC_KEY}")
    print(f"Baseline medians : {baseline_medians}")
    print(f"Candidate medians: {candidate_medians}")
    print("-----------------------------")
    print("Result: ", end="")

    result = step_fit(baseline_medians, candidate_medians)
    if abs(result) <= 25:
        print("Within noise range", end="")
    elif result < 0:
        print("POSSIBLE REGRESSION", end="")
    else:
        print("POSSIBLE IMPROVEMENT", end="")
    print(f" (Step fit: {result:.4})")

if __name__ == "__main__":
    main()
