package edu.udel.secondarycodes.predicate

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Predicate
import edu.udel.util.find
import edu.udel.util.isJUnitAssertions
import edu.udel.util.isOtherAssertion
import edu.udel.util.isOtherVerificationCall

class OtherCheckingCall: Predicate() {

    fun getOtherCalls (test: PsiMethod) : List<String> {

        val otherCalls = test.body?.find<PsiMethodCallExpression>().orEmpty().toList()
            .filter { mc -> isOtherVerificationCall(mc) }

        if (otherCalls.isNullOrEmpty()) return emptyList()

        return otherCalls.map { mc -> mc.methodExpression.referenceName.orEmpty() }
    }
}