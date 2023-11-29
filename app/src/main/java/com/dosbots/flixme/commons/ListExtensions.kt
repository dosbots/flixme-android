package com.dosbots.flixme.commons

fun <T> List<T>.toMap(keyGenerator: (T) -> String): Map<String, T> {
    val map = HashMap<String, T>()
    forEach { listItem ->
        val key = keyGenerator(listItem)
        map[key] = listItem
    }
    return map
}

fun <T> MutableList<T>.replaceWhen(replacement: T, condition: (T) -> Boolean) {
    val index = indexOfFirst(condition)
    if (index >= 0) {
        removeAt(index)
        add(index, replacement)
    }
}

/**
 * Adds a value if it's not in the list. Removes it otherwise
 */
fun <T> MutableList<T>.addOrRemove(value: T) {
    val index = indexOf(value)
    if (index >= 0) {
        removeAt(index)
    } else {
        add(value)
    }
}
