@file:Suppress("UnstableApiUsa ge", "UnstableApiUsage")

package edu.udel

import com.google.common.escape.Escaper
import com.google.common.escape.Escapers
import com.google.common.graph.Traverser
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod
import edu.udel.fca.Concept
import edu.udel.fca.Context
import edu.udel.stats.AnalyzeData
import edu.udel.util.*
import org.jsoup.Jsoup
import util.testClasses
import java.io.File
import java.util.*
import java.io.FileOutputStream
import java.io.PrintStream

//const val savedFolder = "dot"
//const val savedFolder = "barbecue"
//const val savedFolder = "exoplayer"
//const val path = "/Users/wujianwei/IdeaProjects/concept-analysis-plugin/resources"

fun decodeStrings(string: String) : String {

    val temp = escaped1.escape(Jsoup.parse(string).text()).orEmpty()

    val strings = temp.split(",", ".").toMutableList()
        .map { m -> m.trim() }
        .filter { m -> !m.isBlank() && m.toCharArray().any { m1 -> m1.isLetter() } }

    var result = ""

    if (strings.size == 1) return strings[0]

    for (i in 0 until strings.size){
        result += strings[i].capitalize()
    }
    return result
}

fun removeSetGet(string: String) : String {

    if (string.isEmpty()
        || string == "set"
        || string == "get"
        || string == "Set"
        || string == "Get") return string

    return when {
        string.startsWith("set") -> string.removePrefix("set")
        string.startsWith("get") -> string.removePrefix("get")
        string.startsWith("Set") -> string.removePrefix("Set")
        string.startsWith("Get") -> string.removePrefix("Get")
        string.contains("set") -> string.replace("set", "")
        string.contains("get") -> string.replace("get", "")
        string.contains("Set") -> string.replace("Set", "")
        string.contains("Get") -> string.replace("Get", "")
        else -> string
    }
}

val savedFolder = "result"
val defaultNodeAttributes = mapOf("shape" to "Mrecord")

fun Concept<PsiMethod, String>.nodeAttributes(): Map<String, String> {

    return mutableMapOf<String, String>().apply {
        this["label"] = "{%s|%s}".format(
            sparseAttributes.joinToString("\\n"),
            sparseObjects.joinToString("\\n") { o -> o.name }
        )
    }
}

fun recordDotFiles(builder:Context.Builder<PsiMethod, String>, testClass:String){

    val context = builder.build()

    val concepts = Traverser.forGraph<Concept<PsiMethod, String>>
    { it.predecessors }.depthFirstPreOrder(context.bottom)

    File(
        "/Users/wujianwei/IdeaProjects/concept-analysis-plugin/" +
                "$savedFolder/$testClass.dot"
    )
        .bufferedWriter()
        .use { writer -> exporter.export(writer, concepts) { it.successors } }
}

val exporter = DOTExporter<Concept<PsiMethod, String>>(
    defaultNodeAttributes = defaultNodeAttributes, nodeAttributesProvider = { it.nodeAttributes() }
)

val escaped: Escaper = Escapers.builder()
    .addEscape('\n', "")
    .addEscape('\'', "")
    .addEscape('\"', "")
    .addEscape(' ', "")
    .addEscape(':', "")
    .addEscape('\\',"")
    .addEscape(',', ", ")
    .addEscape('[', "")
    .addEscape(']', "")
    .addEscape('{', "")
    .addEscape('}', "")
    .addEscape('"', "")
    .build()

val escaped1: Escaper = Escapers.builder()
    .addEscape('\n', "")
    .addEscape('\'', "")
    .addEscape('\"', "")
    .addEscape(' ', "")
    .addEscape(':', "")
    .addEscape('\\',"")
    .addEscape('[', "")
    .addEscape(']', "")
    .addEscape('{', "")
    .addEscape('}', "")
    .addEscape('"', "")
    .addEscape('(', "")
    .addEscape(')', "")
    .addEscape('*', "")
    .addEscape('/', "")
    .addEscape('-', "")
    .addEscape(' ', "")
    .addEscape('%', "")
    .addEscape('&', "")
    .addEscape('_', "")
    .addEscape('!', "")
    .addEscape('@', "")
    .addEscape('$', "")
    .addEscape('#', "")
    .addEscape('^', "")
    .addEscape('~', "")
    .addEscape('=', "")
    .build()


val analyzer = AnalyzeData()

//Note: the following if-else conditions are used to keep "Uniqueness" of each test

fun handleProject(project: Project){

//    val out = PrintStream(FileOutputStream("/Users/wujianwei/desktop/output.txt"))
//    System.setOut(out)
    //Note: Special code

    val actualTests = HashSet<PsiMethod>()

    for(Class in project.testClasses()) { actualTests.addAll(Class.testMethods()) }

    analyzer.numOfTests = actualTests.size.toDouble()
    //Count tests

    var counter = 0

    for(testClass in project.testClasses()) {

        println("\n\nAnalyzing class: " + testClass.name + " ...")

        val builder = Context.Builder<PsiMethod, String>()
        val map = hashMapOf<PsiMethod, MutableList<List<String>>>()

        for (test in testClass.testMethods()) {

            println("\ntest: ${test.name}")
            print("---Count: " + (++counter) + " - " + actualTests.size + "\n")

            val attributes = arrayListOf<String>()

            val actionPair = getUniqueAction(test, testClass.testMethods().toList())
            val predicatePair = getUniquePredicate(test, testClass.testMethods().toList())

            val actionBits = actionPair.first
                .filter { m -> !m.isBlank() && m.toCharArray().any { m1 -> m1.isLetter() } }
            val predicateBits = predicatePair.first
                .filter { m -> !m.isBlank() && m.toCharArray().any { m1 -> m1.isLetter() } }
            val scenarioBits = getUniqueScenario(test, testClass.testMethods().toList())
                .filter { m -> !m.isBlank() && m.toCharArray().any { m1 -> m1.isLetter() } }

            if (!actionBits.isNullOrEmpty()) {
                println("action candidates: $actionBits")
                attributes.addAll(actionBits)

                builder.setAll(test, attributes.map { m ->
                    m.replace("||", "")
                        .replace("&&", "")
                        .replace("==", "") })

                recordDotFiles(builder, testClass.name.toString())
            }
            else if (!predicateBits.isNullOrEmpty()) {
                println("predicate candidates: $predicateBits")
                attributes.addAll(predicateBits)

                builder.setAll(test, attributes.map { m ->
                    m.replace("||", "")
                        .replace("&&", "")
                        .replace("==", "") })

                recordDotFiles(builder, testClass.name.toString())
            }
            else if (!scenarioBits.isNullOrEmpty()) {
                println("scenario candidates: $scenarioBits")
                attributes.addAll(scenarioBits)

                builder.setAll(test, attributes.map { m ->
                    m.replace("||", "")
                        .replace("&&", "")
                        .replace("==", "") })

                recordDotFiles(builder, testClass.name.toString())
            }

            map[test] = mutableListOf()

            val newActionBitsSet = hashSetOf<String>()
            val newPredicateBitsSet = hashSetOf<String>()

            if (!actionBits.isNullOrEmpty()) {
                if (actionPair.second == "CUTMethodCall" || actionPair.second == "OtherMethodCall") {

                    actionBits.forEach { m -> newActionBitsSet.add(removeSetGet(m)) }
                } else {

                    actionBits.forEach { m -> newActionBitsSet.add(m) }
                }
            }
            map[test]?.add(newActionBitsSet.toMutableList())

            if (!predicateBits.isNullOrEmpty()) {
                if (predicatePair.second == "ActualParameter" || predicatePair.second == "ExpectedParameter"
                    || predicatePair.second == "ActualExpectedParameters"
                ) {

                    predicateBits.forEach { m -> newPredicateBitsSet.add(removeSetGet(m)) }
                } else {

                    predicateBits.forEach { m -> newPredicateBitsSet.add(m) }
                }
            }
            map[test]?.add(newPredicateBitsSet.toMutableList())

            map[test]?.add(scenarioBits)
        }

        println("\nReport: \n")

        for (test in testClass.testMethods()) {

            println(test.name + " - {")

            var descriptiveName = "test_"

            val actionFinal = map[test].orEmpty()[0].sorted()
            val predicateFinal = map[test].orEmpty()[1].sorted()
            val scenarioFinal = map[test].orEmpty()[2].sorted()

            if (!actionFinal.isNullOrEmpty()){

                for (i in 0 until actionFinal.size){

                    if (actionFinal[i].isNotBlank() && actionFinal[i].isNotEmpty()){

                        if (i == actionFinal.size - 1){

                            descriptiveName += decodeStrings(actionFinal[i])
                                .replace("||", "")
                                .replace(", ", "")
                                .replace(",", "")
                                .replace("&&", "")
                                .replace("==", "")
                                .capitalize()

                            break
                        }

                        descriptiveName += decodeStrings(actionFinal[i])
                            .replace("||", "")
                            .replace(", ", "")
                            .replace(",", "")
                            .replace("&&", "")
                            .replace("==", "").capitalize() + "_"
                    }
                }

                println("  Descriptive Name: $descriptiveName")
                println("} ")
                continue
            }

            if (!predicateFinal.isNullOrEmpty()) {

                for (i in 0 until predicateFinal.size) {

                    if (predicateFinal[i].isNotBlank() && predicateFinal[i].isNotEmpty()) {

                        var predicate = predicateFinal[i]
                            .replace(">", "GreaterThan")
                            .replace("<", "LesserThan")
                            .replace("=", "Equals")
                            .replace("+", "And")
                            .replace("0", "Zero")
                            .replace("1", "One")
                            .replace("2", "Two")
                            .replace("3", "Three")
                            .replace("4", "Four")
                            .replace("5", "Five")
                            .replace("6", "Six")
                            .replace("7", "Seven")
                            .replace("8", "Eight")
                            .replace("9", "Nine")

                        predicate = if (predicateFinal[i].startsWith("assert") || predicateFinal[i].startsWith("Assert"))
                            "If.$predicate"
                        else if (predicateFinal[i].startsWith("set") || predicateFinal[i].startsWith("get"))
                            "When.$predicate"
                        else
                            "Check.$predicate"

                        if (i == predicateFinal.size - 1) {

                            descriptiveName += decodeStrings(predicate)
                                .replace("||", "")
                                .replace(", ", "")
                                .replace(",", "")
                                .replace("&&", "")
                                .replace("==", "")
                                .capitalize()

                            break
                        }

                        descriptiveName += decodeStrings(predicate)
                            .replace("||", "")
                            .replace(", ", "")
                            .replace(",", "")
                            .replace("&&", "")
                            .replace("==", "").capitalize() + "_"
                    }
                }

                println("  Descriptive Name: $descriptiveName")
                println("} ")
                continue
            }

            if (!scenarioFinal.isNullOrEmpty()) {

                for (i in 0 until scenarioFinal.size) {

                    if (scenarioFinal[i].isNotBlank() && scenarioFinal[i].isNotEmpty()) {

                        if (i == scenarioFinal.size - 1) {

                            descriptiveName += decodeStrings(scenarioFinal[i])
                                .replace("||", "")
                                .replace(", ", "")
                                .replace(",", "")
                                .replace("&&", "")
                                .replace("==", "")
                                .capitalize()

                            break
                        }

                        descriptiveName += decodeStrings(scenarioFinal[i])
                            .replace("||", "")
                            .replace(", ", "")
                            .replace(",", "")
                            .replace("&&", "")
                            .replace("==", "").capitalize() + "_"
                    }
                }

                println("  Descriptive Name: $descriptiveName")
                println("} ")
                continue
            }
            //Name Generation
        }
    }
}

fun main(projects: Array<Project>) {
    projects.forEach { handleProject(it) }
}