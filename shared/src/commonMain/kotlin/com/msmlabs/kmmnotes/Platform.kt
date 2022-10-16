package com.msmlabs.kmmnotes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform