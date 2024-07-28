@file:Suppress("UnstableApiUsage")

package edu.udel.util

import com.google.common.graph.Traverser
import com.intellij.psi.PsiMethod
import edu.udel.fca.Concept
import edu.udel.fca.Context

fun checkUniqueness(test:PsiMethod, context: Context<PsiMethod, String>): List<String>{

    val graph = Traverser.forGraph<Concept<PsiMethod, String>>
    { it.predecessors }.depthFirstPreOrder(context.bottom)

    //if (test.name == "testBoundsAreNotZero") graph.forEach { concept -> println(concept.toString()) }

    val allUniqueBits = arrayListOf<String>()

    for (concept in graph) {

        if (concept.objects.size == 1 && concept.objects.first() == test) {

//        if (concept.objects.contains(test)) {

            val attributes = concept.uniqueAttributes

            for (attribute in attributes) {

                allUniqueBits.add(attribute)
            }
        }
    }

    return allUniqueBits
}


//                val strings = s.split(":")
//                if (outDegreeForAttribute[s].orEmpty().size == 1) {
//
//                    if (signals.contains(strings[0])) continue
//
//                    if (strings[2].all { it.isDigit() }) continue
//
//                    allUniqueBits.add(s)
//
//                    signals.add(strings[0])
//                    signals.add(strings[1])
//                    //println("  " + strings[0] + " - " + strings[1] + " - " + strings[2])
//
//                } else {
//
//                    if (strings[0] == "action" && !signals.contains("action")) actionComb.add(s)
//                    if (strings[0] == "predicate" && !signals.contains("predicate")) predicateComb.add(s)
//                    if (strings[0] == "scenario" && !signals.contains("scenario")) scenarioComb.add(s)
//                }
//val countAction = hashSetOf<Int>()
//val countPredicate = hashSetOf<Int>()
//val countScenario = hashSetOf<Int>()
//
//for (set in nodeList.values){
//
//    var count1 = 0
//    var count2 = 0
//    var count3 = 0
//
//    for (action in actionComb){
//
//        if (set.contains(action)) count1++
//    }
//
//    if (count1 > 0) countAction.add(count1)
//
//    for (predicate in predicateComb){
//
//        if (set.contains(predicate)) count2++
//    }
//
//    if (count2 > 0) countPredicate.add(count2)
//
//    for (scenario in scenarioComb){
//
//        if (set.contains(scenario)) count3++
//    }
//
//    if (count3 > 0) countScenario.add(count3)
//}
//
//if (!actionComb.isNullOrEmpty() && !countAction.contains(actionComb.size) && !actionComb.all { s -> s.all { it.isDigit() } }) {
//    allUniqueBits.add("action:comb:" + actionComb.joinToString (separator = "") { m -> escaped2.escape(Jsoup.parse(m.split(":")[2]).text()).orEmpty().toLowerCase().capitalize() })
//}
//if (!predicateComb.isNullOrEmpty() && !countPredicate.contains(predicateComb.size) && !predicateComb.all { s -> s.all { it.isDigit() } }) {
//    allUniqueBits.add("predicate:comb:" + predicateComb.joinToString (separator = "") { m -> escaped2.escape(Jsoup.parse(m.split(":")[2]).text()).orEmpty().toLowerCase().capitalize() })
//}
//if (!scenarioComb.isNullOrEmpty() && !countScenario.contains(scenarioComb.size) && !scenarioComb.all { s -> s.all { it.isDigit() } }) {
//    allUniqueBits.add("scenario:comb:" + scenarioComb.joinToString (separator = "") { m -> escaped2.escape(Jsoup.parse(m.split(":")[2]).text()).orEmpty().toLowerCase().capitalize() })
//}