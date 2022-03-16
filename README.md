Now in Android App
==================
This is the repo for the Now in Android app. 

TODOs:
- Add note about version catalogs for dependencies

## Baseline profile

The baseline profile for this app is located at `app/src/main/baseline-prof.txt`.
It contains rules that enable AOT compilation of the critical user path taken during app launch.
For more information on baseline profiles, read [this document](https://developer.android.com/studio/profile/baselineprofiles).

| Note: The baseline profile needs to be re-generated for release builds that touched code which changes app startup.

To generate the baseline profile, select the `benchmark` build variant and run the
`BaselineProfileGenerator` benchmark test on an AOSP Android Emulator.
Then copy the resulting baseline profile from the emulator to `app/src/main/baseline-prof.txt`.