# Steps for Capturing and Analyzing Android Heap Dumps

This guide provides step-by-step instructions on how to capture a heap dump of your Android application using Android Studio, export it to your local Desktop, and analyze it using the Perfetto UI.

---

## Step 1: Capture a Heap Dump in Android Studio

1. **Run the Application:**
   * Run your app in **Debug** mode on a connected device or emulator.

2. **Open the Profiler:**
   * In Android Studio, go to the top menu and select **View** > **Tool Windows** > **Profiler**.
   * Alternatively, click the **Profiler** tab in the tool window bar at the bottom of Android Studio.

3. **Start a Profiling Session:**
   * In the Profiler window, click the **+** (New Session) button in the top-left corner of the **Sessions** pane.
   * Select your connected device and the target application process.

4. **Navigate to the Memory Profiler:**
   * Click anywhere on the **Memory** timeline/row to zoom into the detailed memory usage view.

5. **Capture the Heap Dump:**
   * Click **Record** (or click the dropdown and choose **Capture heap dump**).
   * Select **Capture heap dump** and click **Record** (or double-click the option).
   * Android Studio will initiate the capture. Wait a few moments for the capture to finish and load the heap dump visualization.

---

## Step 2: Export the Heap Dump to your Desktop

1. **Locate the Capture Session:**
   * In the **Sessions** pane on the left side of the Profiler, locate the captured heap dump (it will appear under the active session tab, labeled with the time it was captured).

2. **Export the Capture:**
   * Right-click the captured heap dump item in the list and select **Export**.
   * Alternatively, select the heap dump, and look for the export/save icon (an arrow pointing down or out of a folder/box) next to the session name.

3. **Save to the Desktop:**
   * Choose your local machine's **Desktop** directory as the destination folder.
   * Save the file (it will typically have a `.hprof` extension, e.g., `memory_dump.hprof`).

---

## Step 3: Analyze the Heap Dump in Perfetto

1. **Open Perfetto UI:**
   * Open your web browser and navigate to [https://ui.perfetto.dev/](https://ui.perfetto.dev/).

2. **Load the File:**
   * Drag the exported `.hprof` file from your **Desktop** and drop it directly onto the Perfetto browser page.
   * *Alternatively*, click the **Open trace file** link in the left-hand sidebar of Perfetto, navigate to your Desktop, and select the `.hprof` file.

3. **Explore and Analyze:**
   * Perfetto will process the heap dump, allowing you to search, query, and visualize memory objects, paths to GC roots, and potential memory leaks (such as retained bitmaps or large object trees).

---

## Reference Heap Dumps

For your convenience, we have already captured and saved reference heap dumps inside the [tmp/heapdumps/](file:///Users/rahulrayy/AndroidStudioProjects/NIA/nowinandroid/tmp/heapdumps) folder. You can drag and drop these directly into Perfetto:

### 1. Large Bitmap Issue (NewsResourceCard)
These heap dumps refer to the bitmap creation code in [NewsResourceCard.kt:L200](file:///Users/rahulrayy/AndroidStudioProjects/NIA/nowinandroid/core/ui/src/main/kotlin/com/google/samples/apps/nowinandroid/core/ui/NewsResourceCard.kt#L200):
* **`tmp-1`** ([memory-20260708T135256-tmp-1.hprof](file:///Users/rahulrayy/AndroidStudioProjects/NIA/nowinandroid/tmp/heapdumps/memory-20260708T135256-tmp-1.hprof)): Shows the issue with a large `2000X2000` bitmap using the `ARGB_8888` configuration and no downsampling.
* **`tmp-2`** ([memory-20260708T135608-tmp-2.hprof](file:///Users/rahulrayy/AndroidStudioProjects/NIA/nowinandroid/tmp/heapdumps/memory-20260708T135608-tmp-2.hprof)): Shows the improvement when using downsampling.
* **`tmp-3`** ([memory-20260708T135753-tmp-3.hprof](file:///Users/rahulrayy/AndroidStudioProjects/NIA/nowinandroid/tmp/heapdumps/memory-20260708T135753-tmp-3.hprof)): Shows the improvement when using downsampling together with the `RGB_565` configuration.

### 2. Duplicate Bitmaps Issue (FilterActivity)
These heap dumps refer to the filter screen behavior in [FilterActivity.kt](file:///Users/rahulrayy/AndroidStudioProjects/NIA/nowinandroid/feature/interests/impl/src/main/kotlin/com/google/samples/apps/nowinandroid/feature/interests/impl/filter/FilterActivity.kt):
* **`tmp-4`** ([memory-20260708T140014-tmp-4.hprof](file:///Users/rahulrayy/AndroidStudioProjects/NIA/nowinandroid/tmp/heapdumps/memory-20260708T140014-tmp-4.hprof)): Captures duplicate bitmaps being loaded and retained during the apply filter use case.
* **`tmp-5`** ([memory-20260708T140418-tmp-5.hprof](file:///Users/rahulrayy/AndroidStudioProjects/NIA/nowinandroid/tmp/heapdumps/memory-20260708T140418-tmp-5.hprof)): Captures the state with no duplicate bitmaps after applying code fixes.




