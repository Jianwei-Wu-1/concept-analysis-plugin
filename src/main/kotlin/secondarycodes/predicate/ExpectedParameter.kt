package edu.udel.secondarycodes.predicate

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Predicate
import edu.udel.util.find
import edu.udel.util.isJUnitAssertions
import edu.udel.util.isOtherAssertion

class ExpectedParameter : Predicate() {

    fun getExpectedParameters (test: PsiMethod) : List<PsiExpression?> {

        val assertions = test.body?.find<PsiMethodCallExpression>().orEmpty().toList()
            .filter { mc -> isJUnitAssertions(mc) || isOtherAssertion(mc) }

        if (assertions.isNullOrEmpty()) return emptyList()

        return assertions.map { mc ->
            when {
                mc.argumentList.expressions.isEmpty() -> null
                mc.argumentList.expressions.size == 1 -> null
                mc.argumentList.expressions.size == 2 -> mc.argumentList.expressions[0]
                mc.argumentList.expressions.size == 3 -> mc.argumentList.expressions[1]
                else -> mc.argumentList.expressions[mc.argumentList.expressions.size - 2]
            }
        }.toList()
    }
}