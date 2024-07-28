package edu.udel.fca

/**
 *
 * @param iterator
 * @param support the minimal sequenceCount, i.e., the minimal number of objects contained
 * in the lower neighbor.
 * @param confidence the minimal confidence, i.e., the minimal fraction l/u, where
 * l is the number of objects in the lower neighbor and u is the
 * number of objects in the upper neighbor. Must be a value between
 * 0 and 1.
 * @param difference the maximal difference between the number of attributes
 * in the lower neighbor and the number of attributes in the upper
 * neighbor.
 */
class ViolationIterator<O, A>(
    private val iterator: Iterator<Pair<Concept<O, A>, Concept<O, A>>>,
    private val support: Int,
    private val confidence: Double,
    private val difference: Int
) : AbstractIterator<Pair<Concept<O, A>, Concept<O, A>>>() {

    override fun computeNext() {
        while (iterator.hasNext()) {
            val edge = iterator.next()
            val (upper, lower) = edge

            if (lower.objects.size >= support
                && (lower.attributes.size - upper.attributes.size) <= difference
                && (lower.objects.size.toDouble() / upper.objects.size) >= confidence
            ) {
                return setNext(edge)
            }
        }
        done()
    }
}
