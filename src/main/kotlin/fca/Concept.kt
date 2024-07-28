package edu.udel.fca

data class Concept<O, A>(
    val objects: Set<O>,
    val attributes: Set<A>,
    private val context: Context<O, A>
) : Comparable<Concept<O, A>> {

    val successors: Set<Concept<O, A>> by lazy {
        context.successors(this).toSet()
    }
    val predecessors: Set<Concept<O, A>> by lazy {
        context.predecessors(this).toSet()
    }

    val sparseObjects: Set<O> by lazy {
        objects - successors.flatMap { it.objects }
    }
    val sparseAttributes: Set<A> by lazy {
        attributes - predecessors.flatMap { it.attributes }
    }

    val uniqueAttributes: Set<A> by lazy {
        sparseAttributes.takeIf { it.isNotEmpty() } ?: successors.flatMap { it.uniqueAttributes }.toSet()
        //here predecessors -> successors
    }
    //Compare and compute

    val support: Double by lazy { objects.size.toDouble() / context.objects.size }

    override fun compareTo(other: Concept<O, A>): Int {
        return when {
            other.objects.containsAll(objects) -> -1
            objects.containsAll(other.objects) -> 1
            else -> 0
        }
    }

    override fun toString(): String = "Concept(objects=$objects, attributes=$attributes)"

    companion object {
        fun weightedSimilarity(
            weight: Double,
            similarity: (Set<*>, Set<*>) -> Double
        ): (Concept<*, *>, Concept<*, *>) -> Double = fun(c1: Concept<*, *>, c2: Concept<*, *>): Double {
            return weight * similarity(c1.objects, c2.objects) +
                    (1 - weight) * similarity(c1.attributes, c2.attributes)
        }
    }
}
