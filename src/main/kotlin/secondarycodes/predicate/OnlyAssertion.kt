package edu.udel.secondarycodes.predicate

import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.highlevelcodes.Predicate
import edu.udel.util.isJUnitCalls
import edu.udel.util.isOtherAssertion
import edu.udel.util.testMethods

class OnlyAssertion : Predicate() {

    fun getListOfAssertions(test: PsiMethod): List<PsiMethodCallExpression>{

        val tests = test.containingClass?.testMethods()?.toList().orEmpty()
        if (tests.size == 1) return emptyList()
        val listOfStatements = tests.map { m -> m.body?.statements?.toList().orEmpty() }
        val statementsForCur = test.body?.statements.orEmpty().toList()

        for (statements in listOfStatements){
            for (statement in statements){
                if (statement is PsiExpressionStatement){
                    if (statement.expression is PsiMethodCallExpression){
                        if (isJUnitCalls(statement.expression as PsiMethodCallExpression)
                            || isOtherAssertion(statement.expression as PsiMethodCallExpression)){

                            if (statements != statementsForCur)
                                return emptyList()
                        }
                    }
                }
            }
        }

        for (statements in listOfStatements){
            for (statement in statements){
                if (statement is PsiExpressionStatement){
                    if (statement.expression is PsiMethodCallExpression){
                        if (isJUnitCalls(statement.expression as PsiMethodCallExpression)
                            || isOtherAssertion(statement.expression as PsiMethodCallExpression)){

                             if (statements == statementsForCur)
                                 return statementsForCur.filterIsInstance(PsiExpressionStatement::class.java)
                                     .map{ mc -> mc.expression}
                                     .filterIsInstance(PsiMethodCallExpression::class.java)
                                     .filter { mc -> isJUnitCalls(mc) || isOtherAssertion(mc) }
                        }
                    }
                }
            }
        }

        return emptyList()
    }
}