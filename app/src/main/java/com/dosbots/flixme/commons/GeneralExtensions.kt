package com.dosbots.flixme.commons

fun <T> T?.or(other: () -> T): T {
    return this ?: other()
}