# `:core:ads-impl`
```md
# Appodeal Ads Integration (Now in Android)

This fork demonstrates a production-oriented integration of **Appodeal Ads SDK** into the *Now in Android* sample app.

The focus is on **SDK lifecycle, Compose compatibility, and predictable ad behavior**, rather than UI appearance.

---

## What is implemented

- **Banner ads inside a feed**
  - Explicit preload to avoid layout gaps
  - Safe attach/detach for Compose + Lazy layouts
- **Interstitial ads during navigation**
  - Preloaded on screen entry
  - Shown as a navigation gate
  - Always resolves to exactly one outcome (dismiss or fail → navigate)

---

## Architecture

Ads integration is split into two modules:

```

core/ads-api   // Public contracts, no SDK dependency
core/ads-impl  // Appodeal SDK implementation

````

This keeps feature code independent from Appodeal APIs and allows replacing the ad network with minimal changes.

---

## Key design decisions

### Explicit preload vs implicit SDK behavior
Although Appodeal may start loading ads on `show()`, this is not guaranteed.  
Preload (`cache`) is used explicitly to make loading deterministic and independent from UI timing.

### Compose-safe banner handling
- Single `BannerView` instance per screen
- Strict control over view ownership to avoid duplicate parent crashes
- Recomposition and configuration changes handled without timers or delays

### Interstitial as navigation gate
Interstitials are treated as a navigation concern rather than a UI element.  
Navigation callbacks are wrapped via a small composable helper:

```kotlin
val onTopicClickWithAds = rememberInterstitialGate(
    placement = "nav_interstitial",
    onProceed = navigate
)
````

This keeps:

* ViewModels free from `Activity`
* navigation declarative
* SDK calls centralized and lifecycle-aware

---

## Lifecycle considerations

* SDK initialization is lazy and idempotent
* `show()` may be called multiple times safely
* All ad operations tolerate recomposition, scrolling, and rotation

---

## Scope notes

Client-side frequency capping, analytics hooks, and multiple placements were intentionally kept minimal to focus on integration correctness and SDK behavior.

---

## Summary

This implementation demonstrates:

* clean separation between app code and ad SDK
* Compose-aware handling of views and lifecycle
* predictable preload and display flow without timing hacks

```
```

