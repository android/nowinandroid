# Preview Screenshot Tests

`preview-screenshots` performs automatic `@Preview` screenshot testing utilizing
[Showkase](https://github.com/airbnb/Showkase),
[Paparazzi](https://github.com/cashapp/paparazzi) and
[TestParameterInjector](https://github.com/google/TestParameterInjector).

Showkase aggregates all `@Preview` definitions, and collects them into a list of components
via a KSP processor. We can then use these components as parameterized inputs to tests
configured with TestParameterInjector, and then we use Paparazzi to render each preview
and diff the output.

This CL configures Showkase to run on all UI modules, and then aggregates them all in a test-only
preview-screenshots module. These tests run all previews in a matrix of configurations:
- Each `@Preview`
- Nexus 5, Pixel 5, Pixel C
- Font scale of `1.0` and `1.5`

This could be expanded even further if desired (locale, theming, etc.)

Screenshots are rendered with `layoutlib`, which is the same tool that drives rendering previews
in Android Studio. As a result, the screenshots being taken do not have the same fidelity as what
will actually displayed on a real device. In particular, certain devices might behave slightly
differently, and it isn't possible to verify display interactions with the system (dialogs,
soft keyboard, etc.)

The advantage, however, is that Paparazzi runs in JVM tests, which means they are faster, and don't
require managing a virtual or physical device, making it easier to parameterize across different
device sizes and configurations.

Recording tests can be done with `./gradlew recordPaparazziDemoDebug`, and then checked with
`./gradlew verifyPaparazziDemoDebug`. `check` is configured to depend on `verifyPaparazziDemoDebug`.
