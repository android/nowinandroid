

package com.example.mylibrary

interface StartupTask {
    fun run()
}

// The library object that loads and executes the task.
object TaskRunner {
    fun execute(className: String) {
        // R8 won't retain classes specified by this string value at runtime
        val taskClass = Class.forName(className)
        val task = taskClass.getDeclaredConstructor().newInstance() as StartupTask
        task.run()
    }
}
