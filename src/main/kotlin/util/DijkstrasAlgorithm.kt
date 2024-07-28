@file:Suppress("UnstableApiUsage")

package edu.udel.util

import com.google.common.graph.ElementOrder
import com.google.common.graph.Graph
import com.google.common.graph.ValueGraph
import com.google.common.graph.ValueGraphBuilder
import java.util.*


class DijkstrasAlgorithm<N>(
    val source: N,
    neighbors: (N) -> Iterable<N>,
    stopWhen: (N) -> Boolean = { false },
    weight: (N, N) -> Double = { _, _ -> 1.0 }
) {

    private val prev = mutableMapOf<N, N>()

    private val cost = mutableMapOf<N, Double>()
        .apply {
            this[source] = 0.0
        }
        .withDefault { Double.POSITIVE_INFINITY }

    init {

        val queue = PriorityQueue(compareBy(cost::getValue))
        queue.add(source)

        val visited = mutableSetOf<N>()

        while (queue.isNotEmpty()) {

            val current = queue.remove()

            if (stopWhen(current)) break

            if (visited.add(current)) {
                for (neighbor in neighbors(current)) {
                    val relaxed = if (current == source) {
                        weight(current, neighbor)
                    } else {
                        cost.getValue(current) + weight(current, neighbor)
                    }

                    if (relaxed < cost.getValue(neighbor)) {
                        cost[neighbor] = relaxed
                        prev[neighbor] = current
                        if (neighbor !in queue) {
                            queue.add(neighbor)
                        }
                    }
                }
            }
        }
    }

    fun shortestPathTo(target: N): Path<N> {
        val distance = cost.getValue(target)

        val nodes = LinkedList<N>()

        if (distance < Double.POSITIVE_INFINITY) {
            var u = target

            while (u in prev) {
                nodes.addFirst(u)
                u = prev.getValue(u)
            }
            nodes.addFirst(u)
        }

        return Path(distance, nodes)
    }

    fun shortestPathGraph(): ValueGraph<N, Path<N>> =
        ValueGraphBuilder.directed()
            .allowsSelfLoops(true)
            .build<N, Path<N>>()
            .apply {
                addNode(source)

                for ((v, c) in cost) {
                    putEdgeValue(source, v, shortestPathTo(v))
                }
            }

}


fun <N> Graph<N>.shortestPathBetween(source: N, target: N, weight: (N, N) -> Double = { _, _ -> 1.0 }): Path<N> {
    return DijkstrasAlgorithm(source, this::successors, { it == target }, weight).shortestPathTo(target)
}

fun main() {

    val g = ValueGraphBuilder.directed()
        .nodeOrder(ElementOrder.natural<Int>())
        .allowsSelfLoops(false)
        .build<Int, Double>()
        .apply {
            putEdgeValue(1, 3, -2.0)
            putEdgeValue(3, 4, 2.0)
            putEdgeValue(4, 2, -1.0)
            putEdgeValue(2, 1, 4.0)
            putEdgeValue(2, 3, 3.0)

            addNode(-1)
        }

    println(g)

    println("pair\tcost\tpath")
    for (source in g.nodes()) {
        val dijkstra =
            DijkstrasAlgorithm(source, g::successors) { u, v -> g.edgeValue(u, v).orElse(Double.POSITIVE_INFINITY) }
        for (target in g.nodes()) {
            val (cost, path) = dijkstra.shortestPathTo(target)
            System.out.printf("%s to %s\t%.2f\t%s\n", source, target, cost, path)
        }
    }


    println(DijkstrasAlgorithm(1, g::successors) { u, v -> g.edgeValue(u, v).orElse(Double.POSITIVE_INFINITY) }.shortestPathGraph())
}