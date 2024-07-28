@file:Suppress("UnstableApiUsage")

package edu.udel.util

import com.google.common.escape.Escaper
import com.google.common.escape.Escapers
import com.intellij.psi.*
import edu.udel.fca.Context
import edu.udel.highlevelcodes.Action
import edu.udel.secondarycodes.CUTCallArguments
import edu.udel.secondarycodes.CUTMethodCall
import edu.udel.secondarycodes.OtherCallArguments
import edu.udel.secondarycodes.action.OtherMethodCall
import org.jsoup.Jsoup

fun getAction (test:PsiMethod): List<String>{

    if (!getCUTCall(test).isNullOrEmpty()) return getCUTCall(test)
    if (!getOtherCall(test).isNullOrEmpty()) return getCUTCall(test)
    if (!getCutArgument(test).isNullOrEmpty()) return getCutArgument(test)
    if (!getOtherArgument(test).isNullOrEmpty()) return getOtherArgument(test)

    return emptyList()
}

val escape3: Escaper = Escapers.builder()
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

fun getCUTCall (test:PsiMethod): List<String>{
    return CUTMethodCall().getCUTCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getOtherCall (test:PsiMethod): List<String>{
    return OtherMethodCall().getOtherCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getCutArgument (test:PsiMethod): List<String>{
    return CUTCallArguments().getCUTCallArguments(test).map { arg -> arg.second.toString()
        .replace("PsiLiteralExpression", "")
        .replace("PsiLambdaExpression", "")
        .replace("PsiBinaryExpression", "")
        .replace("PsiNewExpression", "")
        .replace("PsiPrefixExpression", "")
        .replace("PsiReferenceExpression", "")
        .replace("PsiMethodCallExpression", "")
        .replace("PsiTypeCastExpression", "")
        .replace("PsiClassObjectAccessExpression", "")}
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getOtherArgument (test:PsiMethod): List<String>{
    return OtherCallArguments().getOtherCallArguments(test).map { arg -> arg.second.toString()
        .replace("PsiLiteralExpression", "")
        .replace("PsiLambdaExpression", "")
        .replace("PsiBinaryExpression", "")
        .replace("PsiNewExpression", "")
        .replace("PsiReferenceExpression", "")
        .replace("PsiPrefixExpression", "")
        .replace("PsiTypeCastExpression", "")
        .replace("PsiMethodCallExpression", "")
        .replace("PsiClassObjectAccessExpression", "")}
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}


fun getUniqueAction (test:PsiMethod, tests:List<PsiMethod>): Pair<List<String>, String> {

    val builder1 = Context.Builder<PsiMethod, String>()
    val builder2 = Context.Builder<PsiMethod, String>()
    val builder3 = Context.Builder<PsiMethod, String>()
    val builder4 = Context.Builder<PsiMethod, String>()

    for (t in tests){

        val attributes = getCUTCall(t)
        // if re1 is null -> not makes it unique -> move on to val attributes = getOtherCall(t)
        // then to val attributes = getCutArgument(t)
        // then to val attributes = getOtherArgument(t)
        builder1.setAll(t, attributes)
    }

    val context1 = builder1.build()

    val re1 = checkUniqueness(test, context1)

    if (!re1.isNullOrEmpty()) {
        println("CUTMethodCall")
        return Pair(re1,"CUTMethodCall")
    }

    for (t in tests){

        val attributes = getOtherCall(t)
        builder2.setAll(t, attributes)
    }

    val context2 = builder2.build()

    val re2 = checkUniqueness(test, context2)

    if (!re2.isNullOrEmpty()) {
        println("OtherMethodCall")
        return Pair(re2,"OtherMethodCall")
    }

    for (t in tests){

        val attributes = getCutArgument(t)
        builder3.setAll(t, attributes)
    }

    val context3 = builder3.build()

    val re3 = checkUniqueness(test, context3)

    if (!re3.isNullOrEmpty()) {
        println("CUTMethodCallArgument")
        return Pair(re3,"CUTMethodCallArgument")
    }

    for (t in tests){

        val attributes = getOtherArgument(t)
        builder4.setAll(t, attributes)
    }

    val context4 = builder4.build()

    val re4 = checkUniqueness(test, context4)

    if (!re4.isNullOrEmpty()) {
        println("OtherMethodCallArgument")
        return Pair(re4,"OtherMethodCallArgument")
    }

    return Pair(emptyList(), "")
}

//    val allBits1 = mutableListOf<List<String>>()
//    val allBits2 = mutableListOf<List<String>>()
//    val allBits3 = mutableListOf<List<String>>()
//    val allBits4 = mutableListOf<List<String>>()



//    if (!testCUTCall.isNullOrEmpty()) {
//
//        for (method in tests) {
//            if (Action().hasAction(method)) {
//                if (!CUTMethodCall().getCUTCalls(method).isNullOrEmpty()) {
//                    val temp = CUTMethodCall().getCUTCalls(method).map { mc -> mc.methodExpression.referenceName.toString() }
//                    allBits1.add(temp)
//                }
//            }
//        }
//
//        for (targetCall in testCUTCall) {
//            if (performCheckForOne(allBits1, targetCall)){
//                println("CUTMethodCall")
//                return listOf(targetCall)
//            } else{
//                continue
//            }
//        }
//    }
//
//    val testOtherCall = OtherMethodCall().getOtherCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
//    //println(testOtherCall)
//    if (!testOtherCall.isNullOrEmpty()) {
//
//        for (method in tests) {
//            if (Action().hasAction(method)) {
//                if (!OtherMethodCall().getOtherCalls(method).isNullOrEmpty()) {
//                    val temp = OtherMethodCall().getOtherCalls(method).map { mc -> mc.methodExpression.referenceName.toString() }
//                    allBits2.add(temp)
//                }
//            }
//        }
//
//        for (targetCall in testOtherCall) {
//            if (performCheckForOne(allBits2, targetCall)){
//                println("OtherMethodCall")
//                return listOf(targetCall)
//            } else{
//                continue
//            }
//        }
//    }
//
//    val testCutArgument = CUTCallArguments().getCUTCallArguments(test).map { arg -> arg.second.toString()
//        .replace("PsiLiteralExpression", "")
//        .replace("PsiLambdaExpression", "")
//        .replace("PsiBinaryExpression", "")
//        .replace("PsiNewExpression", "")
//        .replace("PsiReferenceExpression", "")
//        .replace("PsiMethodCallExpression", "")
//        .replace("PsiClassObjectAccessExpression", "")}
//
//    if (!testCutArgument.isNullOrEmpty()) {
//        for (method in tests) {
//            if (Action().hasAction(method)) {
//                if (!CUTCallArguments().getCUTCallArguments(method).isNullOrEmpty()) {
//                    val temp = CUTCallArguments().getCUTCallArguments(method).map { arg -> arg.second.toString()
//                        .replace("PsiLiteralExpression", "")
//                        .replace("PsiLambdaExpression", "")
//                        .replace("PsiBinaryExpression", "")
//                        .replace("PsiNewExpression", "")
//                        .replace("PsiReferenceExpression", "")
//                        .replace("PsiMethodCallExpression", "")
//                        .replace("PsiClassObjectAccessExpression", "")
//                    }
//                    allBits3.add(temp)
//                }
//            }
//        }
//
//        for (targetCall in testCutArgument) {
//            if (performCheckForOne(allBits3, targetCall)){
//                println("CUTCallArguments")
//                return listOf(targetCall)
//            } else{
//                continue
//            }
//        }
//    }
//
//    val testOtherArgument = OtherCallArguments().getOtherCallArguments(test).map { arg -> arg.second.toString()
//        .replace("PsiLiteralExpression", "")
//        .replace("PsiLambdaExpression", "")
//        .replace("PsiBinaryExpression", "")
//        .replace("PsiNewExpression", "")
//        .replace("PsiReferenceExpression", "")
//        .replace("PsiMethodCallExpression", "")
//        .replace("PsiClassObjectAccessExpression", "")
//    }
//
//    if (!testOtherArgument.isNullOrEmpty()) {
//        for (method in tests) {
//            if (Action().hasAction(method)) {
//                if (!OtherCallArguments().getOtherCallArguments(method).isNullOrEmpty()) {
//                    val temp = OtherCallArguments().getOtherCallArguments(method).map { arg -> arg.second.toString()
//                        .replace("PsiLiteralExpression", "")
//                        .replace("PsiLambdaExpression", "")
//                        .replace("PsiBinaryExpression", "")
//                        .replace("PsiNewExpression", "")
//                        .replace("PsiReferenceExpression", "")
//                        .replace("PsiMethodCallExpression", "")
//                        .replace("PsiClassObjectAccessExpression", "")
//                    }
//                    allBits4.add(temp)
//                }
//            }
//        }
//
//        for (targetCall in testOtherArgument) {
//            if (performCheckForOne(allBits3, targetCall)){
//                println("OtherCallArguments")
//                return listOf(targetCall)
//            } else{
//                continue
//            }
//        }
//    }