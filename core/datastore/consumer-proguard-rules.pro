# Keep DataStore fields
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite* {
   <fields>;
}

-keepclassmembers,includedescriptorclasses class com.google.samples.apps.nowinandroid.LibraryClass {
    private * secretMessage;
}

-printconfiguration r8/full-r8-config.txt # Prints the entire configuration for the app
-printusage r8/usage.txt # Prints where R8 removed code from the app
-printseeds r8/seeds.txt