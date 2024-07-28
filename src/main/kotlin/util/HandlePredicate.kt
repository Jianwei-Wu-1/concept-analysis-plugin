package edu.udel.util

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.fca.Context
import edu.udel.highlevelcodes.Predicate
import edu.udel.secondarycodes.predicate.*
import org.jsoup.Jsoup

fun getPredicate (test:PsiMethod): List<String>{

    if (!getActualParameter(test).isNullOrEmpty()) return getActualParameter(test)
    if (!getExpectedParameter(test).isNullOrEmpty()) return getExpectedParameter(test)
    if (!getActualExpectedParameters(test).isNullOrEmpty()) return getActualExpectedParameters(test)
    if (!getActualParameterExtended(test).isNullOrEmpty()) return getActualParameterExtended(test)
    if (!getAssertionCall(test).isNullOrEmpty()) return getAssertionCall(test)
    if (!getOtherCheckingCall(test).isNullOrEmpty()) return getOtherCheckingCall(test)
    if (!getOnlyAssertion(test).isNullOrEmpty()) return getOnlyAssertion(test)

    return emptyList()
}

fun getActualParameter (test:PsiMethod): List<String>{
    return ActualParameter().getActualParameters(test).map { it?.text.orEmpty() }
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getExpectedParameter (test:PsiMethod): List<String>{
    return ExpectedParameter().getExpectedParameters(test).map { it?.text.orEmpty() }
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty()
            .replace("PsiLiteralExpression", "")
            .replace("PsiLambdaExpression", "")
            .replace("PsiBinaryExpression", "")
            .replace("PsiPrefixExpression", "")
            .replace("PsiNewExpression", "")
            .replace("PsiReferenceExpression", "")
            .replace("PsiMethodCallExpression", "")
            .replace("PsiClassObjectAccessExpression", "")}

}

fun getActualExpectedParameters (test:PsiMethod): List<String>{
    return ActualExpectedParameters().getAllParameters(test).map { list -> list.map { it.text.orEmpty() } }
        .map { m -> escape3.escape(Jsoup.parse(m.joinToString { it }).text()).orEmpty() }
}

fun getActualParameterExtended (test:PsiMethod): List<String>{
    return ActualParameter().getActualParametersExtended(test)
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getAssertionCall (test:PsiMethod): List<String>{
    return AssertionCall().getAllCalls(test)
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getOtherCheckingCall (test:PsiMethod): List<String>{
    return OtherCheckingCall().getOtherCalls(test)
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getOnlyAssertion (test:PsiMethod): List<String>{
    return OnlyAssertion().getListOfAssertions(test)  .map { it.methodExpression.referenceName.orEmpty() }
        .map { m -> escape3.escape(Jsoup.parse(m).text()).orEmpty() }
}

fun getUniquePredicate (test: PsiMethod, tests:List<PsiMethod>): Pair<List<String>, String>{

    val builder1 = Context.Builder<PsiMethod, String>()
    val builder2 = Context.Builder<PsiMethod, String>()
    val builder3 = Context.Builder<PsiMethod, String>()
    val builder4 = Context.Builder<PsiMethod, String>()
    val builder5 = Context.Builder<PsiMethod, String>()
    val builder6 = Context.Builder<PsiMethod, String>()
    val builder7 = Context.Builder<PsiMethod, String>()

    for (t in tests){

        val attributes = getActualParameter(t)
        builder1.setAll(t, attributes)
    }

    val context1 = builder1.build()

    val re1 = checkUniqueness(test, context1)

    if (!re1.isNullOrEmpty()) {
        println("ActualParameter")
        return Pair(re1,"ActualParameter")
    }

    for (t in tests){

        val attributes = getExpectedParameter(t)
        builder2.setAll(t, attributes)
    }

    val context2 = builder2.build()

    val re2 = checkUniqueness(test, context2)

    if (!re2.isNullOrEmpty()) {
        println("ExpectedParameter")
        return Pair(re2,"ExpectedParameter")
    }

    for (t in tests){

        val attributes = getActualExpectedParameters(t)
        builder3.setAll(t, attributes)
    }

    val context3 = builder3.build()

    val re3 = checkUniqueness(test, context3)

    if (!re3.isNullOrEmpty()) {
        println("ActualExpectedParameters")
        return Pair(re3,"ActualExpectedParameters")
    }

    for (t in tests){

        val attributes = getActualParameterExtended(t)
        builder4.setAll(t, attributes)
    }

    val context4 = builder4.build()

    val re4 = checkUniqueness(test, context4)

    if (!re4.isNullOrEmpty()) {
        println("ActualParameter")
        return Pair(re4,"ActualParameter")
    }


    for (t in tests){

        val attributes = getAssertionCall(t)
        builder5.setAll(t, attributes)
    }

    val context5 = builder5.build()

    val re5 = checkUniqueness(test, context5)

    if (!re5.isNullOrEmpty()) {
        println("AssertionCall")
        return Pair(re5,"AssertionCall")
    }


    for (t in tests){

        val attributes = getOtherCheckingCall(t)
        builder6.setAll(t, attributes)
    }

    val context6 = builder6.build()

    val re6 = checkUniqueness(test, context6)

    if (!re6.isNullOrEmpty()) {
        println("OtherCheckingCall")
        return Pair(re6,"OtherCheckingCall")
    }

    for (t in tests){

        val attributes = getOnlyAssertion(t)
        builder7.setAll(t, attributes)
    }

    val context7 = builder7.build()

    val re7 = checkUniqueness(test, context7)

    if (!re7.isNullOrEmpty()) {
        println("OnlyAssertion")
        return Pair(re7,"OnlyAssertion")
    }

    return Pair(emptyList(), "")

//    var signal = false
//
//    if (!testActualParameter.isNullOrEmpty()){
//        for (method in tests){
//            if (Predicate().hasPredicate(method)){
//                if (!ActualParameter().getActualParameters(method).isNullOrEmpty()){
//                    val temp = ActualParameter().getActualParameters(method).map { it?.text.orEmpty() }
//                    if (!temp.isNullOrEmpty()) allBits7.add(temp)
//                }
//            }
//        }
//        for (parameter in testActualParameter.map { it?.text.orEmpty() }){
//            if (!performCheckForOne(allBits7, parameter)){ signal = true } else { continue }
//        }
//    }
//    if (!testExpectedParameter.isNullOrEmpty()){
//        for (method in tests){
//            if (Predicate().hasPredicate(method)){
//                if (!ExpectedParameter().getExpectedParameters(method).isNullOrEmpty()){
//                    val temp = ExpectedParameter().getExpectedParameters(method).map { it?.text.orEmpty() }
//                    if (!temp.isNullOrEmpty()) allBits8.add(temp)
//                }
//            }
//        }
//        for (parameter in testExpectedParameter.map { it?.text.orEmpty() }){
//            if (!performCheckForOne(allBits8, parameter)){ signal = true } else { continue }
//        }
//    }
//    //println(signal)
//
//    if (!signal) {
//        if (!testActualExpectedParameters.isNullOrEmpty()) {
//            for (method in tests) {
//                if (Predicate().hasPredicate(method)) {
//                    if (!ActualExpectedParameters().getAllParameters(method).isNullOrEmpty()) {
//
//                        val temp = ActualExpectedParameters().getAllParameters(method)
//                            .map { list -> list.map { it.text.orEmpty() } }
//                        //println(temp.toString())
//                        if (!temp.isNullOrEmpty()) allBits1.addAll(temp)
//                    }
//                }
//            }
//
//            val parameter = testActualExpectedParameters[0].map { it.text.toString() }
//            //println(parameter)
//
//            if (performCheckForOne(allBits1, parameter)) {
//                println("ActualExpectedParameters")
//                return listOf(parameter.toString())
//            }
//        }
//    }
//
//    if (!testActualParameter.isNullOrEmpty()){
//        for (method in tests){
//            if (Predicate().hasPredicate(method)){
//                if (!ActualParameter().getActualParameters(method).isNullOrEmpty()){
//                    val temp = ActualParameter().getActualParameters(method).map { it?.text.orEmpty() }
//                    if (!temp.isNullOrEmpty()) allBits2.add(temp)
//                }
//            }
//        }
//
//        for (parameter in testActualParameter.map { it?.text.orEmpty() }){
//            if (performCheckForOne(allBits2, parameter)){
//                println("ActualParameter")
//                return listOf(parameter)
//            } else{
//                continue
//            }
//        }
//    }
//
//    if (!testActualParameterExtended.isNullOrEmpty()){
//        for (method in tests){
//            if (Predicate().hasPredicate(method)){
//                if (!ActualParameter().getActualParametersExtended(method).isNullOrEmpty()){
//                    val temp = ActualParameter().getActualParametersExtended(method)
//                    if (!temp.isNullOrEmpty()) allBits9.add(temp)
//                }
//            }
//        }
//
//        for (parameter in testActualParameterExtended){
//            if (performCheckForOne(allBits9, parameter)){
//                println("ActualParameter")
//                return listOf(parameter)
//            } else{
//                continue
//            }
//        }
//    }
//
//    if (!testExpectedParameter.isNullOrEmpty()){
//        for (method in tests){
//            if (Predicate().hasPredicate(method)){
//                if (!ExpectedParameter().getExpectedParameters(method).isNullOrEmpty()){
//                    val temp = ExpectedParameter().getExpectedParameters(method).map { it?.text.orEmpty() }
//                    if (!temp.isNullOrEmpty()) allBits3.add(temp)
//                }
//            }
//        }
//
//        for (parameter in testExpectedParameter.map { it?.text.orEmpty() }){
//            if (performCheckForOne(allBits3, parameter)){
//                println("ExpectedParameter")
//                return listOf(parameter)
//            } else{
//                continue
//            }
//        }
//    }
//
//    if (!testAssertionCall.isNullOrEmpty()){
//        for (method in tests){
//            if (Predicate().hasPredicate(method)){
//                if (!AssertionCall().getAllCalls(method).isNullOrEmpty()){
//                    val temp = AssertionCall().getAllCalls(method).map { it }
//                    if (!temp.isNullOrEmpty()) allBits4.add(temp)
//                }
//            }
//        }
//
//        for (parameter in testAssertionCall){
//            if (performCheckForOne(allBits4, parameter)){
//                println("AssertionCall")
//                return listOf(parameter)
//            } else{
//                continue
//            }
//        }
//    }
//
//    if (!testOtherCheckingCall.isNullOrEmpty()){
//        for (method in tests){
//            if (Predicate().hasPredicate(method)){
//                if (!OtherCheckingCall().getOtherCalls(method).isNullOrEmpty()){
//                    val temp = OtherCheckingCall().getOtherCalls(method).map { it }
//                    if (!temp.isNullOrEmpty()) allBits5.add(temp)
//                }
//            }
//        }
//
//        for (parameter in testOtherCheckingCall){
//            if (performCheckForOne(allBits5, parameter)){
//                println("OtherCheckingCall")
//                return listOf(parameter)
//            } else{
//                continue
//            }
//        }
//    }
//
//    if (!testOnlyAssertion.isNullOrEmpty()){
//        for (method in tests){
//            if (Predicate().hasPredicate(method)){
//                if (!OnlyAssertion().getListOfAssertions(method).isNullOrEmpty()){
//                    val temp = OnlyAssertion().getListOfAssertions(method)
//                        .map { it.methodExpression.referenceName.orEmpty() }
//                    if (!temp.isNullOrEmpty()) allBits6.add(temp)
//                }
//            }
//        }
//
//        for (parameter in testOnlyAssertion.map { it.methodExpression.referenceName.orEmpty() }){
//            if (performCheckForOne(allBits6, parameter)){
//                println("OnlyAssertion")
//                return listOf(parameter)
//            } else{
//                continue
//            }
//        }
//    }
//
//    return emptyList()
}