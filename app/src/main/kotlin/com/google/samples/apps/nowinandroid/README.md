## Appodeal Integration

The application integrates Appodeal advertising in two formats, according to the task requirements:

1. **Banner advertisement** displayed on the main screen
2. **Interstitial advertisement** shown when the user taps any item in the bottom navigation bar

---

### Banner Implementation Details

At the moment, Appodeal banner ads are not fully compatible with Jetpack Compose.

Specifically, `BannerView` requires the banner container to be a **top-level view in the view hierarchy** and must not be overlapped by other views.  
However, in a Compose-based UI, the entire screen is rendered inside a single `ComposeView`, and there is no traditional hierarchical structure of nested Android `View`s that can satisfy this requirement.

Because of this limitation, using `BannerView` directly inside Compose does not work reliably.

As a workaround, the banner is displayed using Appodeal’s predefined placement modes (`TOP`, `LEFT`, `BOTTOM`). These modes render the banner **above the entire `ComposeView`**, bypassing Compose layout constraints.

In the current implementation, the banner is shown **on the left side of the screen**, positioned above all other UI elements, including the bottom navigation bar.

---

### Interstitial Ads

Interstitial ads are triggered when the user taps any button in the bottom navigation bar, as required by the task.  
The logic for displaying interstitial ads is centralized and decoupled from UI components to avoid tight coupling with Compose navigation logic.

---

### Known Limitations

- Precise placement of banner ads **inside** the Compose layout (for example, strictly above the in-app `NavigationBar`) is not possible using `BannerView` due to Appodeal SDK constraints.
- Full Compose-native banner support would require changes on the Appodeal SDK side.

---

### Appodeal Package Name Limitation

During development, Appodeal may report errors related to an **invalid or unregistered package name**.

This behavior is expected and intentional.

According to the task requirements, the application had to be implemented as a **fork of an existing project** with a predefined `applicationId` (package name).  
This package name is **not registered** in the Appodeal system and cannot be registered under a new Appodeal account.

To proceed with the integration, a separate Appodeal account was created and its own API keys were used with the forked project. As a result, Appodeal correctly detects a mismatch between the registered application and the actual package name used in the app, which leads to warning or error messages during ad loading.

In a production-ready scenario, this issue would be resolved by one of the following approaches:
- Creating a new, clean project and registering its package name in Appodeal
- Using existing Appodeal credentials that are already associated with the required package name