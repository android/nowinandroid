/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mylibrary

import android.util.Log

/**
 * This annotation is now targeted at classes.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ReflectiveExecutor

class TaskRunner {
    fun process(task: Any) {
        Log.e("R8","TaskRunner processing " + task.toString())
        // Get the Java Class object for the instance
        val taskClass = task::class.java
        // 1. Check if the CLASS is annotated with @ReflectiveExecutor
        if (taskClass.isAnnotationPresent(ReflectiveExecutor::class.java)) {
            // 2. If it's annotated, we assume a contract: the class must have a public method named "execute".
            // We use getMethod() to find that specific method.
            val methodToCall = taskClass.getMethod("execute") // Throws an exception if not found

            // 3. Invoke the "execute" method on the provided 'task' instance.
            methodToCall.invoke(task)
        }
    }
}
