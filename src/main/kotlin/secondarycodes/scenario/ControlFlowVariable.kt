package edu.udel.secondarycodes.scenario

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Scenario
import edu.udel.util.extractMCFromIfElseUnderTest
import edu.udel.util.extractMCFromLoopUnderTest


class ControlFlowVariable: Scenario() {

    fun getCFVariable (test: PsiMethod): List<PsiMethodCallExpression>{

        val mcFromIfElse = test.extractMCFromIfElseUnderTest()
        val mcFromLoop = test.extractMCFromLoopUnderTest()

        return if ((mcFromIfElse + mcFromLoop).isNullOrEmpty()){
            emptyList()
        } else {
            (mcFromIfElse + mcFromLoop)
        }
    }
}