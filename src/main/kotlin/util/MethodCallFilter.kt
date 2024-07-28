package edu.udel.util

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.siyeh.ig.testFrameworks.AssertHint

fun isJUnitAssertions(call: PsiMethodCallExpression): Boolean {
    val listOfAssertions = AssertHint.JUnitCommonAssertNames.ASSERT_METHOD_2_PARAMETER_COUNT.keys

    return when {
        call.methodExpression.referenceName.isNullOrBlank() -> false
        call.methodExpression.referenceName == "fail" -> return false
        else -> {
            val name = call.methodExpression.referenceName.toString()
            return listOfAssertions.contains(name)
        }
    }
}

fun isJUnitCalls(callExpression: PsiMethodCallExpression): Boolean{

    val listOfAssertions = AssertHint.JUnitCommonAssertNames.ASSERT_METHOD_2_PARAMETER_COUNT.keys

    return if (callExpression.methodExpression.referenceName.isNullOrBlank()){ false }
    else{
        val name = callExpression.methodExpression.referenceName.toString()
        return listOfAssertions.contains(name)
    }
}

fun isOtherAssertion(callExpression: PsiMethodCallExpression): Boolean{

    val listOfAssertions = AssertHint.JUnitCommonAssertNames.ASSERT_METHOD_2_PARAMETER_COUNT.keys

    return if (callExpression.methodExpression.referenceName.isNullOrBlank()){ false }
    else{
        val name = callExpression.methodExpression.referenceName.toString()
        return !listOfAssertions.contains(name) && name.contains("assert")
    }
}

fun isClassUnderTest (callExpression: PsiMethodCallExpression, test: PsiMethod): Boolean{
    val classes = test.classUnderTest()
    val methods = mutableListOf<PsiMethod>()

    classes.forEach { cls -> methods.addAll(cls.allMethods.toList()) }
    methods.forEach { method ->
        if (callExpression.methodExpression.referenceName == method.name){
            return true
        }
    }
    return false
}

fun isOtherVerificationCall(callExpression: PsiMethodCallExpression): Boolean{

    val listOfCalls = listOf("when(", "verify(", "verify", "reset(", "doCallRealMethod(", "doReturn(",
        "validate(", "reset(", "clearInvocations(", "doCallRealMethod(", "doAnswer(", "doNothing(", "doReturn(", "fail")

    return if (callExpression.methodExpression.referenceName.isNullOrBlank()){ false }
    else{
        val text = callExpression.methodExpression.text.orEmpty()
        for (call in listOfCalls){
            if (text.startsWith(call)) return true
        }
        return false
    }
}