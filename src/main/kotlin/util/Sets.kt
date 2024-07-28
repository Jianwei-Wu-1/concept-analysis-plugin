package edu.udel.util

import com.google.common.collect.Sets

fun jaccardSimilarity(s1: Set<*>, s2: Set<*>): Double {
    val intersection = s1.intersect(s2)
    val union = s1.union(s2)
    return intersection.size.toDouble() / union.size
}

fun sorenesenSimilarity(s1: Set<*>, s2: Set<*>): Double {
    val intersection = s1.intersect(s2)
    return 2.0 * intersection.size / (s1.size + s2.size)
}

fun symmetricSimilarity(s1: Set<*>, s2: Set<*>): Double {
    val symmetricDifference = Sets.symmetricDifference(s1, s2)
    val union = s1.union(s2)
    return 1.0 - (symmetricDifference.size.toDouble() / union.size)
}
