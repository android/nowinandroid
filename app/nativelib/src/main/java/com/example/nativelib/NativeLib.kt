package com.example.nativelib

data class MyKotlinObject(val myString: String?)

class NativeLib {

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun stringFromKotlin(myObject: MyKotlinObject)

    companion object {
        // Used to load the 'nativelib' library on application startup.
        init {
            System.loadLibrary("nativelib")
        }
    }

}