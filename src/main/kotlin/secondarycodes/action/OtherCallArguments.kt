package edu.udel.secondarycodes

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Action
import edu.udel.util.*
import java.util.*

class OtherCallArguments : Action() {

    fun getOtherCallArguments(test: PsiMethod): List<Pair<String, List<PsiExpression>>>{

        val methodCalls = test.body?.find<PsiMethodCallExpression>().orEmpty()
            .filterNot { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) || isOtherVerificationCall(mc)}
        val allArguments = LinkedList<Pair<String, List<PsiExpression>>>()

        for (mc in methodCalls.orEmpty()){
            if (!isClassUnderTest(mc, test) && !isJUnitCalls(mc) && !isOtherAssertion(mc)){
                allArguments.add(Pair(mc.methodExpression.referenceName.toString(), mc.argumentList.expressions.toList()))
            }
        }
        return allArguments
    }

    fun analyzeOtherArguments(test: PsiMethod): List<PsiExpression>{

        val otherCalls = CUTMethodCall().getCUTCalls(test)
        val arguments = LinkedList<PsiExpression>()

        return if (otherCalls.isEmpty()) {
            return arguments
        }
        else if (otherCalls.size == 1){
            otherCalls[0].argumentList.expressions.toList()
        } else{

            val outs = test.objectNameUnderTest()

            if (outs.isEmpty()){
                return arguments
            }
            else {
                for (out in outs) {
                    val lastCutCall = otherCalls.findLast { mc ->
                        mc.text.contains(out)
                    }
                    if (lastCutCall == null) {
                        continue
                    } else {
                        return lastCutCall.argumentList.expressions.toList()
                    }
                }
            }
            otherCalls[otherCalls.size - 1].argumentList.expressions.toList()
        }
    }

    fun getMcFromArguments(test: PsiMethod): List<PsiMethodCallExpression>{

        return this.analyzeOtherArguments(test).filterIsInstance(PsiMethodCallExpression::class.java)
    }
}