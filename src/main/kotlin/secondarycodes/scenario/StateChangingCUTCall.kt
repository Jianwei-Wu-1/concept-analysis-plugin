package edu.udel.secondarycodes.scenario

import com.intellij.psi.*
import edu.udel.highlevelcodes.Scenario
import edu.udel.secondarycodes.action.OtherMethodCall
import edu.udel.util.find
import edu.udel.util.isJUnitAssertions
import edu.udel.util.isOtherAssertion

class StateChangingCUTCall: Scenario() {

    fun getStateChangingCall(test: PsiMethod): String {

        val whenStatements = test.body?.statements?.filterIsInstance(PsiExpressionStatement::class.java)
            ?.map { it.expression }?.filterIsInstance(PsiMethodCallExpression::class.java)?.toList().orEmpty()
            .filter { p -> p.text.startsWith("when") }

        val dualCallStatements = test.body?.statements?.filterIsInstance(PsiExpressionStatement::class.java)
            ?.map { it.expression }?.filterIsInstance(PsiMethodCallExpression::class.java)?.toList().orEmpty()
            .filterNot { p -> p.text.startsWith("when")
                    || p.text.startsWith("Assert")
                    || p.text.startsWith("mock")
                    || p.text.startsWith("spy") }

        val otherCalls = OtherMethodCall().getOtherCalls(test)

        val otherVariable = OtherVariableInit().getALLOtherVariableInit(test)

        val assertions = test.body?.find<PsiMethodCallExpression>().orEmpty().toList()
            .filter { mc -> isJUnitAssertions(mc) || isOtherAssertion(mc) }

        var callForAll = ""

        for (call in otherCalls){

            if (otherVariable.any { m -> m.toString().contains(call.methodExpression.referenceName.orEmpty()) }
                && assertions.any { m -> m.toString().contains(call.methodExpression.referenceName.orEmpty()) }){
                callForAll = call.methodExpression.referenceName.toString()
            }
        }

        return if (!whenStatements.isEmpty()){
            whenStatements[0].methodExpression.referenceName.orEmpty()+ whenStatements[0].argumentList.text.orEmpty()
        }
        else if (whenStatements.isEmpty() && !dualCallStatements.isEmpty()){
            if (dualCallStatements.size == 1) {
                dualCallStatements[0].methodExpression.qualifierExpression?.text.orEmpty()
            } else{
                if (dualCallStatements[dualCallStatements.size - 1].methodExpression.qualifierExpression !is PsiMethodCallExpression){
                    dualCallStatements[dualCallStatements.size - 1].methodExpression.qualifierExpression?.text.orEmpty()
                } else{
                    (dualCallStatements[dualCallStatements.size - 1].methodExpression.qualifierExpression as PsiMethodCallExpression)
                        .methodExpression.referenceName.orEmpty() +
                            (dualCallStatements[dualCallStatements.size - 1].methodExpression.qualifierExpression as PsiMethodCallExpression)
                                .argumentList.text.orEmpty()
                }
            }

        } else{ callForAll }
    }
}