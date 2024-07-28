package edu.udel.secondarycodes.scenario

import com.intellij.psi.*
import edu.udel.highlevelcodes.Scenario
import edu.udel.util.find
import java.util.*


class OtherInitArguments: Scenario() {

    fun getOtherVariableArguments(test: PsiMethod): List<PsiExpression>? {

        val initializations = LinkedList<PsiExpression>()
        val decs = test.body?.find<PsiDeclarationStatement>()
        val assigns = test.body?.find<PsiAssignmentExpression>()

        if (decs != null) {
            for (dec in decs){
                for (declarationElement in dec.declaredElements) {
                    if (declarationElement is PsiLocalVariable) {
                        val initializer = declarationElement.initializer
                        if (initializer != null) {
                            initializations.add(initializer)
                        }
                    }
                }
            }
        }
        if (assigns != null) {
            for (aig in assigns){
                aig.rExpression?.let { initializations.add(it) }
            }
        }

        if (initializations.isNullOrEmpty()) return emptyList()

        val expression = initializations[0]

        if (expression is PsiBinaryExpression){

            return expression.children.filterIsInstance(PsiExpression::class.java).toList()
        }
        if (expression is PsiMethodCallExpression){

            return expression.argumentList.expressions.toList()
        }
        if (expression is PsiNewExpression){

            return expression.argumentList?.expressions?.toList()
        }
        return null
    }
}