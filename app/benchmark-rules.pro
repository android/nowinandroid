# Proguard rules for the `benchmark` build type.
#
# Obsfuscation must be disabled for the build variant that generates Baseline Profile, otherwise
# wrong symbols would be generated. The generated Baseline Profile will be properly applied when generated
# without obfuscation and your app is being obfuscated.
-dontobfuscate