package edu.udel.util

import com.intellij.execution.PsiLocation
import com.intellij.execution.junit.JUnitUtil
import com.intellij.psi.*
import com.intellij.testIntegration.TestFinderHelper
import com.siyeh.ig.testFrameworks.AssertHint

fun PsiMethod.isTest(
    checkAbstract: Boolean = true,
    checkRunWith: Boolean = true,
    checkClass: Boolean = true
): Boolean {
    return JUnitUtil.isTestMethod(PsiLocation(this), checkAbstract, checkRunWith, checkClass)
}

fun PsiMethod.classUnderTest(): Collection<PsiClass> = TestFinderHelper
    .findClassesForTest(this).filterIsInstance(PsiClass::class.java)

fun PsiMethod.isJUnitMethod(): Boolean {
    val listOfAssertions = AssertHint.JUnitCommonAssertNames.ASSERT_METHOD_2_PARAMETER_COUNT.keys

    return name in listOfAssertions
            && containingClass?.name == "TestCase"
            && containingClass?.superClass?.name == "Assert"
}

fun PsiMethod.objectUnderTest(): List<PsiLocalVariable>{
    val statements = body?.statements ?: return emptyList()
    val declarationStatements = statements.filterIsInstance(PsiDeclarationStatement::class.java)
    return declarationStatements
        .flatMap { it.children.filterIsInstance(PsiLocalVariable::class.java) }
        .toList()
}

fun PsiMethod.objectNameUnderTest(): List<String> {
    val statements = body?.statements ?: return emptyList()
    val declarationStatements = statements.filterIsInstance(PsiDeclarationStatement::class.java)
    val localVariables = declarationStatements
        .flatMap { it.children.filterIsInstance(PsiLocalVariable::class.java) }
        .toList()
    return localVariables.map { it.name.toString() }.toList()
}

fun PsiMethod.tryCatchUnderTest(): List<String> {
    val statements = body?.statements
    val exceptions = mutableListOf<String>()
    statements?.forEach { statement ->
        if (statement is PsiTryStatement){
            statement.catchBlockParameters.forEach { parameters ->
                exceptions.add(parameters.type.presentableText)
            }
        }
    }
    return exceptions
}

fun PsiMethod.extractMCFromIfElseUnderTest(): List<PsiMethodCallExpression> {
    val statements = body?.statements
    val innerMethodCalls = mutableListOf<PsiMethodCallExpression>()
    statements?.forEach { statement ->
        if (statement is PsiIfStatement){
            statement.condition?.children?.forEach { element ->
                if (element is PsiMethodCallExpression){
                    innerMethodCalls.add(element)
                }
                if (element is PsiBinaryExpression){
                    element.find<PsiMethodCallExpression>().forEach {
                            m  -> innerMethodCalls.add(m)
                    }
                }
            }
        }
    }
    return innerMethodCalls
}

fun PsiMethod.extractMCFromLoopUnderTest(): List<PsiMethodCallExpression> {
    val statements = body?.statements
    val innerMethodCalls = mutableListOf<PsiMethodCallExpression>()
    statements?.forEach { statement ->
        if (statement is PsiWhileStatement){
            statement.condition?.children?.forEach { element ->
                if (element is PsiMethodCallExpression){
                    innerMethodCalls.add(element)
                }
                if (element is PsiBinaryExpression){
                    element.find<PsiMethodCallExpression>().forEach {
                            m  -> innerMethodCalls.add(m)
                    }
                }
            }
        }
        if (statement is PsiForStatement){
            statement.condition?.children?.forEach { element ->
                if (element is PsiMethodCallExpression){
                    innerMethodCalls.add(element)
                }
                if (element is PsiBinaryExpression){
                    element.find<PsiMethodCallExpression>().forEach {
                        m  -> innerMethodCalls.add(m)
                    }
                }
            }
        }
        if (statement is PsiDoWhileStatement){
            statement.condition?.children?.forEach { element ->
                if (element is PsiMethodCallExpression){
                    innerMethodCalls.add(element)
                }
                if (element is PsiBinaryExpression){
                    element.find<PsiMethodCallExpression>().forEach {
                            m  -> innerMethodCalls.add(m)
                    }
                }
            }
        }
        if (statement is PsiForeachStatement) {
            statement.iterationParameter.children.forEach { element ->
                if (element is PsiMethodCallExpression) {
                    innerMethodCalls.add(element)
                }
                if (element is PsiBinaryExpression){
                    element.find<PsiMethodCallExpression>().forEach {
                            m  -> innerMethodCalls.add(m)
                    }
                }
            }
        }
    }
    return innerMethodCalls
}
