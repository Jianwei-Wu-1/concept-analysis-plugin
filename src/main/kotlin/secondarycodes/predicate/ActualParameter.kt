package edu.udel.secondarycodes.predicate

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Predicate
import edu.udel.util.find
import edu.udel.util.isJUnitAssertions
import edu.udel.util.isOtherAssertion

class ActualParameter : Predicate() {

    fun getActualParameters(test: PsiMethod): List<PsiExpression?> {

        val assertions = test.body?.find<PsiMethodCallExpression>().orEmpty().toList()
            .filter { mc -> isJUnitAssertions(mc) || isOtherAssertion(mc) }

        if (assertions.isNullOrEmpty()) return emptyList()

        return assertions.map { mc ->
            when {
                mc.argumentList.expressions.isEmpty() -> null
                mc.argumentList.expressions.size == 1 -> mc.argumentList.expressions[0]
                mc.argumentList.expressions.size == 2 -> mc.argumentList.expressions[1]
                mc.argumentList.expressions.size == 3 -> mc.argumentList.expressions[2]
                else -> mc.argumentList.expressions[mc.argumentList.expressions.size - 1]
            }
        }.toList()
    }

    fun getActualParametersExtended(test: PsiMethod): List<String> {

        val whens = test.body?.statements?.filterIsInstance(PsiExpressionStatement::class.java)
            ?.map { it.expression }?.filterIsInstance(PsiMethodCallExpression::class.java)?.toList().orEmpty()
            .filter {
                it.methodExpression.text.startsWith("mock(")
                        || it.methodExpression.text.startsWith("verify(")
                        || it.methodExpression.text.startsWith("spy(")
            }

        if (whens.isNullOrEmpty()) return emptyList()

        return whens.map { mc -> mc.text.replace(mc.methodExpression.qualifierExpression?.text + ".", "") }
    }
}