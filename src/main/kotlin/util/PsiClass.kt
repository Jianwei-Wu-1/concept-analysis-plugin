package edu.udel.util

import com.intellij.execution.junit.JUnitUtil
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.testIntegration.TestFinderHelper

fun PsiClass.testsFromClass(): Collection<PsiMethod> =
    TestFinderHelper.findTestsForClass(this).filterIsInstance(PsiMethod::class.java)

fun PsiClass.isTestClass(
    checkAbstract: Boolean = true,
    checkForTestCaseInheritance: Boolean = true
): Boolean {
    return JUnitUtil.isTestClass(this, checkAbstract, checkForTestCaseInheritance)
}

fun PsiClass.testMethods(
    checkAbstract: Boolean = true,
    checkRunWith: Boolean = true,
    checkClass: Boolean = true
): Collection<PsiMethod> {
    return methods.filter { it.isTest(checkAbstract, checkRunWith, checkClass)}
}