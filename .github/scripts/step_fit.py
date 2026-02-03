import json
import math
import glob
import os

# ----------- CONFIG -----------
JSON_REPORTS_DIR = "benchmarks/json_reports"
BENCHMARK_NAME = "startupPrecompiledWithBaselineProfile"
METRIC_KEY = "timeToInitialDisplayMs"
# ------------------------------

def sum_squared_error(values):
    avg = sum(values) / len(values)
    return sum((v - avg) ** 2 for v in values)

def step_fit(before, after):
    total_squared_error = sum_squared_error(before) + sum_squared_error(after)
    step_error = math.sqrt(total_squared_error) / (len(before) + len(after))
    if step_error == 0.0:
        return 0.0
    return (sum(before) / len(before) - sum(after) / len(after)) / step_error

def extract_median_from_file(path):
    with open(path, "r") as f:
        data = json.load(f)
    for bench in data.get("benchmarks", []):
        if bench.get("name") == BENCHMARK_NAME:
            metrics = bench.get("metrics", {})
            metric = metrics.get(METRIC_KEY, {})
            return metric.get("median")
    raise ValueError(f"Metric not found in {path}")

def main():
    before = []
    after = []

    json_files = sorted(glob.glob(os.path.join(JSON_REPORTS_DIR, "*.json")))

    if len(json_files) == 0:
        raise RuntimeError("No JSON files found.")

    for path in json_files:
        median = extract_median_from_file(path)
        filename = os.path.basename(path).lower()
        if "v1" in filename:
            before.append(median)
        elif "v2" in filename:
            after.append(median)
        else:
            print(f"Skipping file with unknown label: {filename}")
        print(f"{filename}: median={median:.3f} ms")

    if len(before) != 5 or len(after) != 5:
        raise RuntimeError(f"Expected 5 runs each, got v1={len(before)}, v2={len(after)}")

    result = step_fit(before, after)

    print("\n-----------------------------")
    print(f"v1 medians: {before}")
    print(f"v2 medians: {after}")
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
