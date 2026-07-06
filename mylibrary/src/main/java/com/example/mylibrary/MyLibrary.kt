

package com.example.mylibrary

interface StartupTask {
    fun run()
}

// The library object that loads and executes the task.
object TaskRunner {
    fun execute(taskClassFromApp: Class<out StartupTask>) {
        // R8 will remove the class specified by this string.
        val taskClassInstance =
            taskClassFromApp.getDeclaredConstructor().newInstance() as StartupTask
        taskClassInstance.run()
    }
}
