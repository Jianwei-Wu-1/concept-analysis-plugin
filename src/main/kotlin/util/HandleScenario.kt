@file:Suppress("NAME_SHADOWING")

package edu.udel.util

import com.intellij.psi.PsiMethod
import edu.udel.fca.Context
import edu.udel.secondarycodes.scenario.*
import org.jsoup.Jsoup

fun getScenario (test:PsiMethod): List<String>{

    if (!getOutVariableInit(test).isNullOrEmpty()) return getOutVariableInit(test)
    if (!getOtherVariableInit(test).isNullOrEmpty()) return getOtherVariableInit(test)
    if (!getOtherInitArguments(test).isNullOrEmpty()) return getOtherInitArguments(test)
    if (!getControlFlowVariable(test).isNullOrEmpty()) return getControlFlowVariable(test)
    if (!getStateChangingCUTCall(test).isNullOrEmpty()) return getStateChangingCUTCall(test)

    return emptyList()
}

//fun getOutVariableInit (test:PsiMethod): List<String>{
//    return listOf(OUTVariableInit().getONEOutVariableInit(test)?.text.orEmpty())
//        .map { m -> "scenario:OUTVariableInit:${escape3.escape(Jsoup.parse(m).text()).orEmpty()}" }
//}
//
//fun getOtherVariableInit (test:PsiMethod): List<String>{
//    return listOf(OtherVariableInit().getONEOtherVariableInit(test)?.text.orEmpty())
//        .map { m -> "scenario:OtherVariableInit:${escape3.escape(Jsoup.parse(m).text()).orEmpty()}" }
//}
//
//fun getOtherInitArguments (test:PsiMethod): List<String>{
//    return OtherInitArguments().getOtherVariableArguments(test).orEmpty().map { m -> m.text.orEmpty() }
//        .map { m -> "scenario:OtherInitArguments:${escape3.escape(Jsoup.parse(m).text()).orEmpty()}" }
//}
//
//fun getControlFlowVariable (test:PsiMethod): List<String>{
//    return ControlFlowVariable().getCFVariable(test)
//        .map { m -> m.methodExpression.referenceName.orEmpty() }
//        .map { m -> "scenario:ControlFlowVariable:${escape3.escape(Jsoup.parse(m).text()).orEmpty()}" }
//}
//
//fun getStateChangingCUTCall (test:PsiMethod): List<String>{
//    return listOf(StateChangingCUTCall().getStateChangingCall(test).orEmpty())
//        .map { m -> "scenario:StateChangingCUTCall:${escape3.escape(Jsoup.parse(m).text()).orEmpty()}" }
//}

fun getOutVariableInit (test:PsiMethod): List<String>{
    return OUTVariableInit().getALLOutVariableInit(test).orEmpty().map { m -> m.text.orEmpty() }
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getOtherVariableInit (test:PsiMethod): List<String>{
    return OtherVariableInit().getALLOtherVariableInit(test).map { m -> m.text.orEmpty() }
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getOtherInitArguments (test:PsiMethod): List<String>{
    return OtherInitArguments().getOtherVariableArguments(test).orEmpty().map { m -> m.text.orEmpty() }
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getControlFlowVariable (test:PsiMethod): List<String>{
    return ControlFlowVariable().getCFVariable(test)
        .map { m -> m.methodExpression.referenceName.orEmpty() }
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getStateChangingCUTCall (test:PsiMethod): List<String>{
    return listOf(StateChangingCUTCall().getStateChangingCall(test))
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}


fun getUniqueScenario (test: PsiMethod, tests:List<PsiMethod>): List<String>{

    val builder1 = Context.Builder<PsiMethod, String>()
    val builder2 = Context.Builder<PsiMethod, String>()
    val builder3 = Context.Builder<PsiMethod, String>()
    val builder4 = Context.Builder<PsiMethod, String>()
    val builder5 = Context.Builder<PsiMethod, String>()

    for (t in tests){
        val attributes = getOutVariableInit(t)
        builder1.setAll(t, attributes)
    }

    val context1 = builder1.build()

    val re1 = checkUniqueness(test, context1)

    if (!re1.isNullOrEmpty()) {
        println("OUTVariableInit")
        return re1
    }

    for (t in tests){

        val attributes = getOtherVariableInit(t)
        builder2.setAll(t, attributes)
    }

    val context2 = builder2.build()

    val re2 = checkUniqueness(test, context2)

    if (!re2.isNullOrEmpty()) {
        println("OtherVariableInit")
        return re2
    }

    for (t in tests){

        val attributes = getOtherInitArguments(t)
        builder3.setAll(t, attributes)
    }

    val context3 = builder3.build()

    val re3 = checkUniqueness(test, context3)

    if (!re3.isNullOrEmpty()) {
        println("OtherInitArguments")
        return re3
    }

    for (t in tests){

        val attributes = getStateChangingCUTCall(t)
        builder5.setAll(t, attributes)
    }

    val context5 = builder5.build()

    val re5 = checkUniqueness(test, context5)

    if (!re5.isNullOrEmpty()) {
        println("StateChangingCall")
        return re5
    }

    for (t in tests){

        val attributes = getControlFlowVariable(t)
        builder4.setAll(t, attributes)
    }

    val context4 = builder4.build()

    val re4 = checkUniqueness(test, context4)

    if (!re4.isNullOrEmpty()) {
        println("ControlFlowVariable")
        return re4
    }

    return emptyList()
}


//    if (testControlFlowVariable.isNullOrEmpty() || testControlFlowVariable.any { e -> !Scenario().set.contains(e) }) {
//        if (!testOutVariableInit.isEmpty()) {
//            for (method in tests) {
//                if (Scenario().hasScenario(method)) {
//                    if (OUTVariableInit().getONEOutVariableInit(method) != null) {
//                        val temp = OUTVariableInit().getONEOutVariableInit(method)?.text.orEmpty()
//                        if (temp.isBlank() || temp.isEmpty() || temp == "null" || temp == "") continue
//                        allBits1.add(listOf(temp))
//                    }
//                }
//            }
//
//            val chars =  escape3.escape(Jsoup.parse(testOutVariableInit).text()).orEmpty()
//                .toList().all { c -> c.isDigit() }
//
//            if (!chars) {
//                if (performCheckForOne(allBits1, testOutVariableInit)) {
//                    println("OUTVariableInit")
//                    return listOf(testOutVariableInit)
//                }
//            }
//        }
//    }
//    else{
//        if (!testControlFlowVariable.isNullOrEmpty()){
//            for (method in tests){
//                if (Scenario().hasScenario(method)){
//                    val temp = ControlFlowVariable().getCFVariable(method)
//                        .map { m -> m.methodExpression.referenceName.orEmpty() }
//                    if (!temp.isNullOrEmpty()){
//                        allBits4.add(temp)
//                    }
//                }
//            }
//            for (targetCall in testControlFlowVariable) {
//                if (performCheckForOne(allBits4, targetCall)){
//                    println("ControlFlowVariable")
//                    return listOf(targetCall)
//                } else{
//                    continue
//                }
//            }
//        }
//    }
//
//    if (!testOtherVariableInit.isEmpty()){
//        for (method in tests) {
//            if (Scenario().hasScenario(method)) {
//                if (OtherVariableInit().getONEOtherVariableInit(method) != null) {
//                    val temp = OtherVariableInit().getONEOtherVariableInit(method)?.text.orEmpty()
//                    if (temp.isBlank() || temp.isEmpty() || temp == "null" || temp == "") continue
//                    allBits2.add(listOf(temp))
//                }
//            }
//        }
//
//        val chars =  escape3.escape(Jsoup.parse(testOtherVariableInit).text()).orEmpty()
//            .toList().all { c -> c.isDigit() }
//
//        if (!chars) {
//            if (performCheckForOne(allBits2, testOtherVariableInit)) {
//                println("OtherVariableInit")
//                return listOf(testOtherVariableInit)
//            }
//        }
//    }
//
//    if (!testOtherInitArguments.isNullOrEmpty()){
//        for (method in tests){
//            if (Scenario().hasScenario(method)){
//                val temp = OtherInitArguments().getOtherVariableArguments(method).orEmpty()
//                    .map { m -> m.text.orEmpty() }
//                if (!temp.isNullOrEmpty()){
//                    allBits3.add(temp)
//                }
//            }
//        }
//        for (targetCall in testOtherInitArguments) {
//            if (performCheckForOne(allBits3, targetCall)){
//                println("OtherInitArguments")
//                return listOf(targetCall)
//            } else{
//                continue
//            }
//        }
//    }
//
//    if (!testStateChangingCUTCall.isEmpty()){
//        for (method in tests) {
//            if (Scenario().hasScenario(method)) {
//                if (!StateChangingCUTCall().getStateChangingCall(method).isEmpty()) {
//                    val temp = StateChangingCUTCall().getStateChangingCall(method)
//                    if (temp.isBlank() || temp.isEmpty() || temp == "null" || temp == "") continue
//                    allBits5.add(listOf(temp))
//                }
//            }
//        }
//        if (performCheckForOne(allBits5, testStateChangingCUTCall)){
////            println(allBits5 + " -- " +testStateChangingCUTCall)
//            println("StateChangingCUTCall")
//            return listOf(testOtherVariableInit)
//        }
//    }

