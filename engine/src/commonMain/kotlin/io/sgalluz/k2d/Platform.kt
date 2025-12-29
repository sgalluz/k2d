package io.sgalluz.k2d

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform