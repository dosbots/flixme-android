package com.dosbots.flixme.commons

fun <T> List<T>.toMap(keyGenerator: (T) -> String): Map<String, T> {
    val map = HashMap<String, T>()
    forEach { listItem ->
        val key = keyGenerator(listItem)
        map[key] = listItem
    }
    return map
}

fun <T> MutableList<T>.addOrReplace(value: T) {
    val index = indexOf(value)
    if (index >= 0) {
        removeAt(index)
        add(index, value)
    } else {
        add(value)
    }
}