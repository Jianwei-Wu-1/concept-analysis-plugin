@file:Suppress("UnstableApiUsage")

package edu.udel.util

fun performCheckForOne (input: List<List<String>>, target: String): Boolean{
    //For current element to be unique

    if (input.isEmpty()) return false

    return input.count { it.contains(target) } == 1
}

fun performCheckForOne (input: List<List<String>>, target: List<String>): Boolean{
    //For current element to be unique

    if (input.isEmpty()) return false

    return input.count { it == target } == 1
}

fun performCheckAll (input: List<List<String>>): Boolean{
    //For everything to be unique

    if (input.isEmpty()) return false

    return input.groupingBy { it }.eachCount().filter { it.value > 1 }.isEmpty()
}