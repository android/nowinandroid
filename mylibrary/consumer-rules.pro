
# Declare that the default constructor is called reflectively.
-keep class * implements com.example.mylibrary.StartupTask {  <init>(); }

-keep class  * {
        public *;
    }



-dontwarn com.example.mylibrary.StartupTask
-dontwarn com.example.mylibrary.TaskRunner