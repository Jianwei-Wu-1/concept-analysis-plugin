package edu.udel.secondarycodes.predicate

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Predicate
import edu.udel.util.find
import edu.udel.util.isJUnitAssertions
import edu.udel.util.isOtherAssertion

class AssertionCall : Predicate(){

    fun getAllCalls (test: PsiMethod) : List<String> {

        val assertions = test.body?.find<PsiMethodCallExpression>().orEmpty().toList()
            .filter { mc -> isJUnitAssertions(mc) || isOtherAssertion(mc) }

        if (assertions.isNullOrEmpty()) return emptyList()

        return assertions.map { mc -> mc.methodExpression.referenceName.orEmpty() }
    }
}