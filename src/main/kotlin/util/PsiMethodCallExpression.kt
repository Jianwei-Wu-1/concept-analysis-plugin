package edu.udel.util

import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression

fun PsiMethodCallExpression.objectFromArguments(): List<String>{

    val parameters = argumentList
    return parameters.expressions
        .filterIsInstance(PsiReferenceExpression::class.java)
        .map { it.referenceName.toString() }
        .toList()
}