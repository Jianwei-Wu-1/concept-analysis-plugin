package edu.udel.fca

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import com.intellij.psi.PsiMethodCallExpression

class Context<O, A>(table: Table<O, A, Boolean>) {

    private val incidenceMatrix = ImmutableTable.copyOf(table)

    val objects: Set<O> by lazy { incidenceMatrix.rowKeySet() }

    val attributes: Set<A> by lazy { incidenceMatrix.columnKeySet() }

    fun objectsWith(attribute: A): Set<O> {
        require(attribute in attributes)
        return incidenceMatrix.column(attribute).keys
    }

    fun attributesWith(obj: O): Set<A> {
        require(obj in objects)
        return incidenceMatrix.row(obj).keys
    }

    /**
     * Returns a set of objects which have all the specified attributes.
     *
     * More formally, this method returns a set that contains all objects `o` for which it is true that for
     * all attributes `a` that are contained in `coll` the pair (`o`, `a`) is contained in this relation.
     *
     * @param attributes a set of attributes.
     *
     * @return the set of objects that have all the attributes contained in the collection `attributes`.
     */
    fun commonObjects(attributes: Iterable<A>): Set<O> =
        attributes.map { objectsWith(it) }.fold(objects) { acc, objs -> acc.intersect(objs) }


    /**
     * Returns a set of attributes which are associated with all the specified objects.
     *
     * More formally, this method returns a set that contains all nodeAttributes `a` for which it is true that
     * for all objects `o` that are contained in `coll` the pair (`o`, `a`)
     * is contained in this relation.
     *
     * @param objects a set contextOf objects.
     * *
     * @return the set of attributes all objects contained in the collection `objects`.
     */
    fun commonAttributes(objects: Iterable<O>): Set<A> =
        objects.map { attributesWith(it) }.fold(attributes) { acc, attrs -> acc.intersect(attrs) }


    val top: Concept<O, A> by lazy {
        conceptFromAttributes(emptySet())
    }
    val bottom: Concept<O, A> by lazy {
        conceptFromObjects(emptySet())
    }

//    /**
//     * Returns the least upper bound of the concepts contained in
//     * the collection `concepts`.
//     * @param concepts the concepts whose least upper bound shall be computed.
//     * @return the least upper bound of the concepts contained in `concepts`.
//     */
//    fun join(concepts: Iterable<Concept<O, A>>): Concept<O, A> {
//        val sharedAttributes = concepts.map { it.attributes }.fold(attributes) { acc, attrs -> acc.intersect(attrs) }
//        return conceptFromAttributes(sharedAttributes)
//    }
//
//
//    /**
//     * Returns the greatest lower bound of the concepts contained in
//     * the collection `concepts`.
//     * @param concepts the concepts whose greatest lower bound shall be computed.
//     * @return the greatest lower bound of the concepts contained in `concepts`
//     */
//    fun meet(concepts: Iterable<Concept<O, A>>): Concept<O, A> {
//        val sharedObjects = concepts.map { it.objects }.fold(objects) { acc, objs -> acc.intersect(objs) }
//        return conceptFromObjects(sharedObjects)
//    }


    /**
     * Returns the least concept that contains all objects contained in `objects`.
     *
     * Returns the concept that contains the common attributes of the objects contained in `objects` and
     * their common objects, but no other objects or attributes.
     *
     * More formally, returns the concept (`object`'', `object`').
     *
     * @param objects the set of objects from which the concept shall be computed.
     * *
     * @return the concept computed from `objects`.
     */
    fun conceptFromObjects(objects: Iterable<O>): Concept<O, A> {
        val commonAttributes = commonAttributes(objects)
        val commonObjects = commonObjects(commonAttributes)
        return Concept(commonObjects, commonAttributes, this)
    }

    /**
     * Returns the greatest concept that contains all attributes contained in `attributes`.
     *
     * Returns the concept that contains the common objects of the attributes contained in `attributes` and
     * their common attributes, but no other objects or attributes.
     *
     * More formally, returns the concept (`attributes`', `attributes`').
     *
     * @param attributes the set of attributes from which the concept shall be computed.
     *
     * @return the concept computed from `attributes`.
     */
    fun conceptFromAttributes(attributes: Iterable<A>): Concept<O, A> {
        val commonObjects = commonObjects(attributes)
        val commonAttributes = commonAttributes(commonObjects)
        return Concept(commonObjects, commonAttributes, this)
    }


    fun predecessors(concept: Concept<O, A>) = sequence {

        val candidateObjects = objects - concept.objects
        val min = candidateObjects.toMutableSet()

        for (obj in candidateObjects) {

            val sharedAttributes = attributesWith(obj).intersect(concept.attributes)
            val sharedObjects = commonObjects(sharedAttributes)

            val intersection = min.intersect(sharedObjects)

            if (intersection.isEmpty() || intersection == setOf(obj)) {
                yield(Concept(sharedObjects, sharedAttributes, this@Context))
            } else {
                min.remove(obj)
            }
        }
    }


    fun successors(concept: Concept<O, A>) = sequence {

        val candidateAttributes = attributes - concept.attributes
        val min = candidateAttributes.toMutableSet()

        for (attr in candidateAttributes) {
            val sharedObjects = objectsWith(attr).intersect(concept.objects)
            val sharedAttributes = commonAttributes(sharedObjects)

            val intersection = min.intersect(sharedAttributes)

            if (intersection.isEmpty() || intersection == setOf(attr)) {
                yield(Concept(sharedObjects, sharedAttributes, this@Context))
            } else {
                min.remove(attr)
            }
        }
    }

    override fun toString(): String = incidenceMatrix.toString()

    class Builder<O, A> {

        private val incidenceMatrix: HashBasedTable<O, A, Boolean> = HashBasedTable.create()

        fun set(obj: O, attribute: A) = incidenceMatrix.put(obj, attribute, true)

        fun setAll(obj: O, attributes: Iterable<A>) = attributes.forEach { set(obj, it) }

        fun build(): Context<O, A> = Context(incidenceMatrix)
    }

    companion object {
        fun <O, A> builder(block: Context.Builder<O, A>.() -> Unit) =
            Context.Builder<O, A>().apply(block).build()

        fun <O, A> of(vararg pairs: Pair<O, Iterable<A>>) = builder<O, A> {
            pairs.forEach { setAll(it.first, it.second) }
        }

        fun <O, A> from(objects: Iterable<O>, extractor: (O) -> Iterable<A>) = builder<O, A> {
            objects.forEach { setAll(it, extractor(it)) }
        }


        fun <O, A> parse(
            str: String,
            objectTransform: (String) -> O,
            attributeTransform: (String) -> A
        ) = builder<O, A> {

            val lines = str.lines()
                .map { it.split("#").first() }
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val attributes = lines.first()
                .split("|")
                .drop(1) //skip first (object) column
                .map { it.trim() }
                .map { attributeTransform(it) }

            for (row in lines.subList(1, lines.size)) {

                val cells = row.split("|")
                    .map { it.trim() }

                val obj = objectTransform(cells.first())

                cells.subList(1, cells.size)
                    .withIndex()
                    .filter { (_, mark) -> mark.isNotBlank() }
                    .map { (i, _) -> attributes[i] }
                    .let { setAll(obj, it) }

            }
        }

        fun parse(str: String): Context<String, String> = parse(str, { it }, { it })
    }
}
