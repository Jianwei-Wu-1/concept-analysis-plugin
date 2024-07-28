package edu.udel.highlevelcodes

import com.intellij.psi.*
import edu.udel.util.find
import java.util.*

open class Scenario {

    val set = hashSetOf("length")

    fun hasScenario(test: PsiMethod): Boolean{

        val decs = test.body?.find<PsiDeclarationStatement>()
        val assigns = test.body?.find<PsiAssignmentExpression>()

        if (decs != null) {
            for (dec in decs){
                for (declarationElement in dec.declaredElements) {
                    if (declarationElement is PsiLocalVariable) {
                        val initializer = declarationElement.initializer
                        if (initializer != null) {
                            return true
                        }
                    }
                }
            }
        }
        if (assigns != null) return true

        val calls = test.body?.find<PsiMethodCallExpression>()

        if (!calls.isNullOrEmpty()) return true

        return false
    }
}