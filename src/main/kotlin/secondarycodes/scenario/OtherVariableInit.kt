package edu.udel.secondarycodes.scenario

import com.intellij.psi.*
import edu.udel.highlevelcodes.Scenario
import edu.udel.util.find
import java.util.*

class OtherVariableInit: Scenario() {

    fun getONEOtherVariableInit(test: PsiMethod): PsiExpression?{

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

        if (initializations.isNullOrEmpty()) return null

        return initializations[0]
    }

    fun getALLOtherVariableInit(test: PsiMethod): List<PsiExpression> {

        val initializations = LinkedList<PsiExpression>()
        val decs = test.body?.find<PsiDeclarationStatement>()
        val assigns = test.body?.find<PsiAssignmentExpression>()

        if (decs != null) {
            for (dec in decs){
                for (locals in dec.declaredElements.filterIsInstance(PsiLocalVariable::class.java).toList()) {
                    val initializer = locals.initializer
                    if (initializer != null) {
                        initializations.add(initializer)
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

        return initializations
    }
}