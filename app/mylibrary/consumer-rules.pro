# In your library's consumer-proguard-rules.pro
-keep, allowoptimization class ** implements com.example.mylibrary.MyWorker {
    <init>();
   <methods>;
}
-whyareyoukeeping class com.example.mylibrary.MyWorker{
    <methods>;
}


-keepattributes RuntimeVisibleAnnotations

-keep @interface com.example.mylibrary.OnEvent

-keepclasseswithmembers class **{
   <init>();
    @com.example.mylibrary.OnEvent <methods>;
}

-keep class com.example.mylibrary.EventBus {
 <methods>;
}

-whyareyoukeeping class com.example.mylibrary.EventBus {
 <methods>;
<fields>;
}


-keep @interface com.example.mylibrary.ReflectiveExecutor

-keep @com.example.mylibrary.ReflectiveExecutor class **{
    # Keep the public, no-argument constructor so that an instance of the class can be created.
    # <init> is the internal name for a constructor.
    public <init>();

    # Keep the public execute() method that has no parameters.
    # This is critical because TaskRunner calls getMethod("execute").
    # If this method is renamed (obfuscated) or removed
    # (shrunk), your app wont work the intended way.
    public void execute();
}


