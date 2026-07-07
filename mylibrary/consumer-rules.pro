
# Declare that the default constructor is called reflectively.
-dontwarn com.example.mylibrary.StartupTask
-dontwarn com.example.mylibrary.TaskRunner

-keep class * implements com.example.mylibrary.StartupTask {
    <init>();
    run();
}

-keep class * {
   *;
}