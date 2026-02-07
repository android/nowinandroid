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

    # Using glob on Path objects
    baseline_files = sorted([str(p) for p in baseline_dir.glob("*.json")])
    candidate_files = sorted([str(p) for p in candidate_dir.glob("*.json")])

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
    for i in range(min_len):
        print(f'{i + 1} {baseline_files[i]} <-> {candidate_files[i]}')

    baseline_medians  = extract_median_from_files(baseline_files[:min_len])
    candidate_medians = extract_median_from_files(candidate_files[:min_len])
    assert (len(baseline_medians) == len(candidate_medians))

    result = step_fit(baseline_medians, candidate_medians)

    print("\n-----------------------------")
    print(f"Baseline medians : {baseline_medians}")
    print(f"Candidate medians: {candidate_medians}")
    print(f"Step Fit Result: {result:.4f}")
    print("-----------------------------")

    if abs(result) <= 25:
        print("➡️ Difference is within noise range (low confidence of real regression)")
    elif result > 0:
        print("⚠️ v2 is slower than v1 (possible regression)")
    else:
        print("🚀 v2 is faster than v1 (possible improvement)")

if __name__ == "__main__":
    main()
