package edu.udel.util

import java.io.PrintWriter
import java.io.Writer

@Suppress("UnstableApiUsage")
class DOTExporter<V>(
    private val graphAttributes: Map<String, String>? = null,
    private val defaultNodeAttributes: Map<String, String>? = null,
    private val defaultEdgeAttributes: Map<String, String>? = null,
    private val nodeIDProvider: (V) -> String = { "\"0x%x\"".format(it.hashCode()) },
    private val nodeAttributesProvider: ((V) -> Map<String, String>)? = null,
    private val edgeAttributesProvider: ((V, V) -> Map<String, String>)? = null
) {

    private fun formatAttributes(attributes: Map<String, String>) =
        attributes.entries.joinToString(prefix = "[", postfix = "]") { "${it.key} = \"${it.value}\"" }

    fun export(
        writer: Writer,
        nodes: Iterable<V>,
        neighbors: (V) -> Iterable<V>
        ) {


        with(PrintWriter(writer)) {
            print("digraph {")

            graphAttributes?.let { println("\tgraph ${formatAttributes(it)}") }
            defaultNodeAttributes?.let { println("\tnode ${formatAttributes(it)}") }
            defaultEdgeAttributes?.let { println("\tedge ${formatAttributes(it)}") }

            for (node in nodes) {
                print("\t${nodeIDProvider(node)}")
                nodeAttributesProvider?.let { print(" ${formatAttributes(it(node))}") }
                println()
            }

            for (node in nodes) {
                for (neighbor in neighbors(node)) {
                    print("\t${nodeIDProvider(node)} -> ${nodeIDProvider(neighbor)}")
                    edgeAttributesProvider?.let { print(" ${formatAttributes(it(node, neighbor))}") }
                    println()
                }
            }

            println("}")
            flush()
        }
    }
}
