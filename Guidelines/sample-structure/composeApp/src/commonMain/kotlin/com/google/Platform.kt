package com.google

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform