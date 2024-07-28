package edu.udel.secondarycodes

import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Action
import edu.udel.util.*
import java.util.*

class CUTMethodCall : Action(){

    fun getCUTCalls(test: PsiMethod): List<PsiMethodCallExpression>{

        val cutCalls = LinkedList<PsiMethodCallExpression>()
        val methodCalls = test.body?.find<PsiMethodCallExpression>().orEmpty()
            .filterNot { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) || isOtherVerificationCall(mc)}
//        val methodCalls = test.body?.statements?.filterIsInstance(PsiExpressionStatement::class.java)
//            .orEmpty().map { it.expression }.filterIsInstance(PsiMethodCallExpression::class.java).toList()
//            .filterNot { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) || isOtherVerificationCall(mc)}

        for (mc in methodCalls){
            if (isClassUnderTest(mc, test)){
                cutCalls.add(mc)
            }
        }
        return cutCalls
    }

    fun analyzeCutCalls(test: PsiMethod): PsiMethodCallExpression? {

        val cutCalls = this.getCUTCalls(test)

        return if (cutCalls.isEmpty()) {
            return null
        }
        else if (cutCalls.size == 1){
            cutCalls[0]
        } else{

            //The following code reflects the concept of focal method call
            val allOuts = test.objectNameUnderTest()
            var statements = test.body?.statements?.filterIsInstance(PsiExpressionStatement::class.java)
                ?.map { it.expression }?.filterIsInstance(PsiMethodCallExpression::class.java)?.toList()

            if (!statements.isNullOrEmpty()) {
                statements = statements.filterNot{ s -> isJUnitCalls(s) || isOtherAssertion(s)}

                if (!statements.isNullOrEmpty() ){
                    return statements[statements.size - 1]
                }
            }

            if (allOuts.isEmpty()){
                return cutCalls[cutCalls.size - 1]
            }
            else {

                for (out in allOuts) {
                    val lastCutCall = cutCalls.findLast { mc ->
                        mc.text.contains(out)
                    }

                    if (lastCutCall == null) {
                        continue
                    } else {
                        return lastCutCall
                    }
                }
            }
            cutCalls[cutCalls.size - 1]
        }
    }
}