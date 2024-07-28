package edu.udel.secondarycodes.action

import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Action
import edu.udel.util.*
import java.util.*

class OtherMethodCall : Action() {

    fun getOtherCalls(test: PsiMethod): List<PsiMethodCallExpression>{

        val otherCalls = LinkedList<PsiMethodCallExpression>()
        val methodCalls = test.body?.find<PsiMethodCallExpression>().orEmpty()
            .filterNot { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) || isOtherVerificationCall(mc)}
//        val methodCalls = test.body?.statements?.filterIsInstance(PsiExpressionStatement::class.java)
//            .orEmpty().map { it.expression }.filterIsInstance(PsiMethodCallExpression::class.java).toList()
//            .filterNot { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) || isOtherVerificationCall(mc)}

        for (mc in methodCalls){
            if (!isClassUnderTest(mc, test)){
                otherCalls.add(mc)
            }
        }

        return otherCalls
    }

    fun analyzeOtherCalls(test: PsiMethod): PsiMethodCallExpression? {

        val otherCalls = this.getOtherCalls(test)

        return if (otherCalls.isEmpty()) {
            return null
        }
        else if (otherCalls.size == 1){
            otherCalls[0]
        } else{

            val outs = test.objectNameUnderTest()
            var statements = test.body?.statements?.filterIsInstance(
                PsiExpressionStatement::class.java)
                ?.map { it.expression }?.filterIsInstance(PsiMethodCallExpression::class.java)?.toList()

            if (!statements.isNullOrEmpty()) {
                statements = statements.filterNot{ s -> isJUnitCalls(s) || isOtherAssertion(s) }

                if (!statements.isNullOrEmpty() ){
                    return statements[statements.size - 1]
                }
            }

            if (outs.isEmpty()){
                return otherCalls[otherCalls.size - 1]
            }
            else {
                for (out in outs) {
                    val lastCutCall = otherCalls.findLast { mc ->
                        mc.text.contains(out)
                    }
                    if (lastCutCall == null) {
                        continue
                    } else {
                        return lastCutCall
                    }
                }
            }
            otherCalls[otherCalls.size - 1]
        }
    }
}