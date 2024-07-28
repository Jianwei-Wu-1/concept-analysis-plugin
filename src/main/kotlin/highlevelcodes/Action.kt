package edu.udel.highlevelcodes

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.util.find
import edu.udel.util.isJUnitCalls
import edu.udel.util.isOtherAssertion
import edu.udel.util.isOtherVerificationCall

open class Action {

    fun hasAction(test: PsiMethod): Boolean{

        val calls = test.body?.find<PsiMethodCallExpression>()
        val methodCalls =
            calls.orEmpty().filterNot { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) || isOtherVerificationCall(mc)}

         return !methodCalls.isNullOrEmpty()
    }

    fun getAction(test: PsiMethod): String{

        val calls = test.body?.find<PsiMethodCallExpression>()
        val methodCalls =
            calls.orEmpty().filterNot { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) || isOtherVerificationCall(mc)}

        return methodCalls[0].methodExpression.text
    }
}