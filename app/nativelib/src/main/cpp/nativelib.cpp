#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeLib_stringFromKotlin(
        JNIEnv* env,
        jobject /* this */, // Represents the NativeLib instance
        jobject kotlinObjectInstance) { // This is the MyKotlinObject instance passed from Kotlin

    // 1. Get the class of the Kotlin object
    // You need to use the correct class path for MyKotlinObject
    // Assuming MyKotlinObject is in com.example.nativelib package
    jclass kotlinClass = env->GetObjectClass(kotlinObjectInstance);
    if (!kotlinClass) {
        __android_log_print(ANDROID_LOG_ERROR, "NativeCode", "Failed to get class for MyKotlinObject");
        return;
    }

    // 2. Get the field IDs for myString
    jfieldID stringFieldId = env->GetFieldID(kotlinClass, "myString", "Ljava/lang/String;");

    if (!stringFieldId) {
        __android_log_print(ANDROID_LOG_ERROR, "NativeCode", "Failed to get field ID for myString");
        // It's good practice to release the class reference if you're returning early
        env->DeleteLocalRef(kotlinClass);
        return;
    }

    // 3. Get the values of the fields
    jstring jStringValue = (jstring)env->GetObjectField(kotlinObjectInstance, stringFieldId);

    // 4. Convert jstring to a C++ string
    const char* cStringValue = nullptr;
    if (jStringValue != nullptr) { // Important: Check for null before using GetStringUTFChars
        cStringValue = env->GetStringUTFChars(jStringValue, nullptr);
    }

    std::string cppStringValue = (cStringValue != nullptr) ? cStringValue : "null"; // Handle null string

    if (jStringValue != nullptr && cStringValue != nullptr) {
        env->ReleaseStringUTFChars(jStringValue, cStringValue);
    }

    // It's good practice to delete local references when you're done with them
    if (jStringValue != nullptr) {
        env->DeleteLocalRef(jStringValue);
    }
    env->DeleteLocalRef(kotlinClass);


    // 5. Use the values in your C++ code
    __android_log_print(ANDROID_LOG_ERROR, "NativeCode", "String from Kotlin: %s", cppStringValue.c_str());
}