package edu.udel.secondarycodes

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Action
import edu.udel.util.*
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
class CUTCallArguments: Action(){

    fun getCUTCallArguments(test: PsiMethod): List<Pair<String, List<PsiExpression>>>{

        val methodCalls = test.body?.find<PsiMethodCallExpression>().orEmpty()
            .filterNot { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) || isOtherVerificationCall(mc)}
        val allArguments = LinkedList<Pair<String, List<PsiExpression>>>()

        for (mc in methodCalls){
            if (isClassUnderTest(mc, test) && !isJUnitCalls(mc) && !isOtherAssertion(mc)){
                allArguments.add(Pair(mc.methodExpression.referenceName.toString(), mc.argumentList.expressions.toList()))
            }
        }
        return allArguments
    }

    fun analyzeCutArguments(test: PsiMethod): List<PsiExpression>{

        val cutCalls = CUTMethodCall().getCUTCalls(test)
        val arguments = LinkedList<PsiExpression>()

        return if (cutCalls.isEmpty()) {
            return arguments
        }
        else if (cutCalls.size == 1){
            cutCalls[0].argumentList.expressions.toList()
        } else{

            val outs = test.objectNameUnderTest()

            if (outs.isEmpty()){
                return arguments
            }
            else {
                for (out in outs) {
                    val lastCutCall = cutCalls.findLast { mc ->
                        mc.text.contains(out)
                    }
                    if (lastCutCall == null) {
                        continue
                    } else {
                        return lastCutCall.argumentList.expressions.toList()
                    }
                }
            }
            cutCalls[cutCalls.size - 1].argumentList.expressions.toList()
        }
    }

    fun getMcFromArguments(test: PsiMethod): List<PsiMethodCallExpression>{

        return this.analyzeCutArguments(test).filterIsInstance(PsiMethodCallExpression::class.java)
    }
}