# In your library's consumer-proguard-rules.pro
-keep, allowoptimization class * implements com.example.mylibrary.MyWorker {
    <init>();
}

-keepattributes *Annotation*

-keep @interface com.example.mylibrary.OnEvent

-keepclasseswithmembers class * {
    @com.example.mylibrary.OnEvent <methods>;
}

-keep @interface com.example.mylibrary.ReflectiveExecutor

-keep @com.example.mylibrary.ReflectiveExecutor class *{
    # Keep the public, no-argument constructor so that an instance of the class can be created.
    # <init> is the internal name for a constructor.
    public <init>();

    # Keep the public execute() method that has no parameters.
    # This is critical because TaskRunner calls getMethod("execute").
    # If this method is renamed (obfuscated) or removed (shrunk), your app will crash.
    public void execute();
}


