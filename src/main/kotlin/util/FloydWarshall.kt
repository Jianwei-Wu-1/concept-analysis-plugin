@file:Suppress("UnstableApiUsage")

import com.google.common.graph.*
import edu.udel.util.Path

private operator fun <N> EndpointPair<N>.component1(): N = this.nodeU()
private operator fun <N> EndpointPair<N>.component2(): N = this.nodeV()

class FloydWarshall<N>(
    private val nodes: Iterable<N>,
    neighbors: (N) -> Iterable<N>,
    weight: (N, N) -> Double
) {

    constructor(
        graph: Graph<N>,
        weight: (N, N) -> Double = { u, v -> if (graph.hasEdgeConnecting(u, v)) 1.0 else Double.POSITIVE_INFINITY }
    ) : this(graph.nodes(), graph::successors, weight)

    constructor(valueGraph: ValueGraph<N, Double>) : this(
        valueGraph.asGraph(),
        { u, v -> valueGraph.edgeValue(u, v).orElse(Double.POSITIVE_INFINITY) })

    private val cost = mutableMapOf<Pair<N, N>, Double>()
        .apply {
            for (u in nodes) {
                this[u to u] = 0.0
                for (v in neighbors(u)) {
                    this[u to v] = weight(u, v)
                }
            }
        }
        .withDefault { Double.POSITIVE_INFINITY }

    private val next = mutableMapOf<Pair<N, N>, N>()
        .withDefault { it.second }

    init {
        for (k in nodes) {
            for (i in nodes) {
                for (j in nodes) {
                    val relaxed = cost.getValue(i to k) + cost.getValue(k to j)
                    if (relaxed < cost.getValue(i to j)) {
                        cost[i to j] = relaxed
                        next[i to j] = next.getValue(i to k)
                    }
                }
            }
        }
    }

    fun shortestPathBetween(source: N, target: N): Path<N> {
        val distance = cost.getValue(source to target)

        val nodes = mutableListOf<N>()

        if (distance < Double.POSITIVE_INFINITY) {

            var current = source
            while (current != target) {
                nodes += current
                current = next.getValue(current to target)

            }
            nodes += target
        }

        return Path(distance, nodes)
    }

    fun shortestPathGraph(): ValueGraph<N, Path<N>> =
        ValueGraphBuilder.directed()
            .allowsSelfLoops(true)
            .build<N, Path<N>>()
            .apply {
                nodes.forEach { addNode(it) }

                for ((edge, c) in cost) {
                    val (u, v) = edge
                    putEdgeValue(u, v, shortestPathBetween(u, v))
                }
            }
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
        }


    val floyd = FloydWarshall(g)

    println(g)

    println(floyd.shortestPathGraph())

    println("pair\tcost\tpath")
    for (source in g.nodes()) {
        for (target in g.nodes()) {
            val (cost, path) = floyd.shortestPathBetween(source, target)
            System.out.printf("%s to %s\t%.2f\t%s\n", source, target, cost, path)
        }
    }
}