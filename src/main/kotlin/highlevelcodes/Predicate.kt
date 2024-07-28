package edu.udel.highlevelcodes

import com.intellij.execution.junit.JUnitUtil
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.siyeh.ig.testFrameworks.AssertHint
import edu.udel.util.find
import edu.udel.util.isJUnitAssertions
import edu.udel.util.isOtherAssertion
import edu.udel.util.isOtherVerificationCall
import java.util.*

open class Predicate {

    fun hasPredicate(test: PsiMethod): Boolean{

        val statements = test.body?.statements ?: return false
        val methodCalls = statements.filterIsInstance(PsiMethodCallExpression::class.java)
        val listOfAssertions = AssertHint.JUnitCommonAssertNames.ASSERT_METHOD_2_PARAMETER_COUNT.keys

        for (methodCall in methodCalls) {

            val call = methodCall.methodExpression.qualifiedName

            if (listOfAssertions.contains(call)){
                return true
            }

        }
        val calls = test.body?.find<PsiMethodCallExpression>()

        if (calls != null) {
            for (call in calls){
                if (isJUnitAssertions(call) || isOtherAssertion(call) || isOtherVerificationCall(call)){
                    return true
                }
            }
        }

        return false
    }
}