package edu.udel.secondarycodes.scenario

import com.intellij.psi.*
import edu.udel.highlevelcodes.Scenario
import edu.udel.util.isJUnitCalls
import edu.udel.util.isOtherAssertion
import java.util.*


class OUTVariableInit: Scenario() {

    fun getALLOutVariableInit(test: PsiMethod): List<PsiExpression>? {

        val statements = test.body?.statements?.filterIsInstance(PsiExpressionStatement::class.java)
            ?.map { it.expression }?.filterIsInstance(PsiMethodCallExpression::class.java)?.toList().orEmpty()
        val assertions = statements.filter{ s -> isJUnitCalls(s) || isOtherAssertion(s) }.map { it.text }
        val mcs = statements.filterNot{ s -> isJUnitCalls(s) || isOtherAssertion(s)}.map { it.text }
        val decs = test.body?.statements?.filterIsInstance(PsiDeclarationStatement::class.java).orEmpty()
        val results = LinkedList<PsiExpression>()

        if (statements.isNullOrEmpty() || assertions.isNullOrEmpty() || decs.isNullOrEmpty()){
            return null
        }

        for (statement in decs){

            for (element in statement.declaredElements.filterIsInstance(PsiLocalVariable::class.java).toList()){

                if (mcs.isNullOrEmpty()){

                    val lastAssert = assertions.findLast { ast -> ast.contains(element.name.toString()) }

                    if (!lastAssert.isNullOrBlank()) {
                        element.initializer?.let { results.add(it) }
                    } else{
                        continue
                    }
                } else{

                    val lastAssert = assertions.findLast { ast -> ast.contains(element.name.toString()) }
                    val lastMethodCall = mcs.findLast { mc -> mc.contains(element.name.toString()) }

                    if (!lastAssert.isNullOrBlank() || !lastMethodCall.isNullOrBlank()) {
                        element.initializer?.let { results.add(it) }
                    } else{
                        continue
                    }
                }
            }
        }
        return results
    }

    fun getONEOutVariableInit(test: PsiMethod): PsiExpression? {

        val statements = test.body?.statements?.filterIsInstance(PsiExpressionStatement::class.java)
            ?.map { it.expression }?.filterIsInstance(PsiMethodCallExpression::class.java)?.toList().orEmpty()
        val assertions = statements.filter{ s -> isJUnitCalls(s) || isOtherAssertion(s) }.map { it.text }
        val mcs = statements.filterNot{ s -> isJUnitCalls(s) || isOtherAssertion(s)}.map { it.text }
        val decs = test.body?.statements?.filterIsInstance(PsiDeclarationStatement::class.java).orEmpty()

        if (statements.isNullOrEmpty() || decs.isNullOrEmpty()){
            return null
        }

        for (statement in decs){

            for (element in statement.declaredElements.filterIsInstance(PsiLocalVariable::class.java).toList()){

                if (mcs.isNullOrEmpty() && !assertions.isNullOrEmpty()){

                    val lastAssert = assertions.findLast { ast -> ast.contains(element.name.toString()) }

                    if (!lastAssert.isNullOrBlank()) {
                        return element.initializer
                    } else{
                        continue
                    }
                } else if (!mcs.isNullOrEmpty() && assertions.isNullOrEmpty()) {

                    val lastMethodCall = mcs.findLast { mc -> mc.contains(element.name.toString()) }

                    if (!lastMethodCall.isNullOrBlank()) {
                        return element.initializer
                    } else{
                        continue
                    }
                }
            }
        }
        return null
    }
}