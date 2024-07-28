@file:Suppress("UnstableApiUsage")

package edu.udel.util

import com.google.common.graph.Traverser
import com.intellij.psi.PsiMethod
import edu.udel.fca.Concept
import edu.udel.fca.Context
import edu.udel.highlevelcodes.Action
import edu.udel.highlevelcodes.Predicate
import edu.udel.highlevelcodes.Scenario
import edu.udel.secondarycodes.CUTCallArguments
import edu.udel.secondarycodes.CUTMethodCall
import edu.udel.secondarycodes.OtherCallArguments
import edu.udel.secondarycodes.action.OtherMethodCall
import edu.udel.secondarycodes.predicate.*
import edu.udel.secondarycodes.scenario.*

fun getBits(test: PsiMethod): List<String> {

    if (!Action().hasAction(test) && !Predicate().hasPredicate(test) && !Scenario().hasScenario(test)){

        val result = arrayListOf<String>()

        val testCUTCall = CUTMethodCall().getCUTCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
        val testOtherCall = OtherMethodCall().getOtherCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
        val testCutArgument = CUTCallArguments().getCUTCallArguments(test).map { arg -> arg.second.toString()
            .replace("PsiLiteralExpression", "")
            .replace("PsiLambdaExpression", "")
            .replace("PsiBinaryExpression", "")
            .replace("PsiNewExpression", "")
            .replace("PsiReferenceExpression", "")
            .replace("PsiMethodCallExpression", "")
            .replace("PsiClassObjectAccessExpression", "")}
        val testOtherArgument = OtherCallArguments().getOtherCallArguments(test).map { arg -> arg.second.toString()
            .replace("PsiLiteralExpression", "")
            .replace("PsiLambdaExpression", "")
            .replace("PsiBinaryExpression", "")
            .replace("PsiNewExpression", "")
            .replace("PsiReferenceExpression", "")
            .replace("PsiMethodCallExpression", "")
            .replace("PsiClassObjectAccessExpression", "")
        }

        if (!testCUTCall.isEmpty()){
            result.add(testCUTCall[0])
        } else if (!testOtherCall.isEmpty()){
            result.add(testOtherCall[0])
        } else if (!testCutArgument.isEmpty()){
            result.add(testCutArgument[0])
        } else if (!testOtherArgument.isEmpty()){
            result.add(testOtherArgument[0])
        }
        //Action

        val testActualExpectedParameters = ActualExpectedParameters().getAllParameters(test)
        val testActualParameter = ActualParameter().getActualParameters(test)
        val testExpectedParameter = ExpectedParameter().getExpectedParameters(test)
        val testAssertionCall = AssertionCall().getAllCalls(test)
        val testOtherCheckingCall = OtherCheckingCall().getOtherCalls(test)
        val testOnlyAssertion = OnlyAssertion().getListOfAssertions(test)
        val testActualParameterExtended = ActualParameter().getActualParametersExtended(test)


        if (!testActualExpectedParameters.isEmpty()){
            result.add(testActualExpectedParameters[0][0].toString());
        } else if (!testActualParameter.isEmpty()){
            result.add(testActualParameter[0].toString())
        } else if (!testExpectedParameter.isEmpty()){
            result.add(testExpectedParameter[0].toString())
        } else if (!testAssertionCall.isEmpty()){
            result.add(testAssertionCall[0])
        }else if (!testOtherCheckingCall.isEmpty()){
            result.add(testOtherCheckingCall[0])
        }else if (!testOnlyAssertion.isEmpty()){
            result.add(testOnlyAssertion[0].text)
        }else if (!testActualParameterExtended.isEmpty()){
            result.add(testActualParameterExtended[0])
        }
        //Predicate

        val testOutVariableInit = OUTVariableInit().getONEOutVariableInit(test)?.text.orEmpty()
        val testOtherVariableInit = OtherVariableInit().getONEOtherVariableInit(test)?.text.orEmpty()
        val testOtherInitArguments = OtherInitArguments().getOtherVariableArguments(test)
            .orEmpty().map { m -> m.text.orEmpty() }
        val testControlFlowVariable = ControlFlowVariable().getCFVariable(test)
            .map { m -> m.methodExpression.referenceName.orEmpty() }
        val testStateChangingCUTCall = StateChangingCUTCall().getStateChangingCall(test)

        if (!testOutVariableInit.isEmpty()){
            result.add(testOutVariableInit);
        } else if (!testOtherVariableInit.isEmpty()){
            result.add(testOtherVariableInit)
        } else if (!testOtherInitArguments.isEmpty()){
            result.add(testOtherInitArguments[0])
        } else if (!testControlFlowVariable.isEmpty()){
            result.add(testControlFlowVariable[0])
        }else if (!testStateChangingCUTCall.isEmpty()){
            result.add(testStateChangingCUTCall)
        }
        //Scenario

        return result
    }

    else if (!Action().hasAction(test) && !Predicate().hasPredicate(test)){

        val result = arrayListOf<String>()

        val testCUTCall = CUTMethodCall().getCUTCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
        val testOtherCall = OtherMethodCall().getOtherCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
        val testCutArgument = CUTCallArguments().getCUTCallArguments(test).map { arg -> arg.second.toString()
            .replace("PsiLiteralExpression", "")
            .replace("PsiLambdaExpression", "")
            .replace("PsiBinaryExpression", "")
            .replace("PsiNewExpression", "")
            .replace("PsiReferenceExpression", "")
            .replace("PsiMethodCallExpression", "")
            .replace("PsiClassObjectAccessExpression", "")}
        val testOtherArgument = OtherCallArguments().getOtherCallArguments(test).map { arg -> arg.second.toString()
            .replace("PsiLiteralExpression", "")
            .replace("PsiLambdaExpression", "")
            .replace("PsiBinaryExpression", "")
            .replace("PsiNewExpression", "")
            .replace("PsiReferenceExpression", "")
            .replace("PsiMethodCallExpression", "")
            .replace("PsiClassObjectAccessExpression", "")
        }

        if (!testCUTCall.isEmpty()){
            result.add(testCUTCall[0])
        } else if (!testOtherCall.isEmpty()){
            result.add(testOtherCall[0])
        } else if (!testCutArgument.isEmpty()){
            result.add(testCutArgument[0])
        } else if (!testOtherArgument.isEmpty()){
            result.add(testOtherArgument[0])
        }
        //Action

        val testActualExpectedParameters = ActualExpectedParameters().getAllParameters(test)
        val testActualParameter = ActualParameter().getActualParameters(test)
        val testExpectedParameter = ExpectedParameter().getExpectedParameters(test)
        val testAssertionCall = AssertionCall().getAllCalls(test)
        val testOtherCheckingCall = OtherCheckingCall().getOtherCalls(test)
        val testOnlyAssertion = OnlyAssertion().getListOfAssertions(test)
        val testActualParameterExtended = ActualParameter().getActualParametersExtended(test)


        if (!testActualExpectedParameters.isEmpty()){
            result.add(testActualExpectedParameters[0][0].toString());
        } else if (!testActualParameter.isEmpty()){
            result.add(testActualParameter[0].toString())
        } else if (!testExpectedParameter.isEmpty()){
            result.add(testExpectedParameter[0].toString())
        } else if (!testAssertionCall.isEmpty()){
            result.add(testAssertionCall[0])
        }else if (!testOtherCheckingCall.isEmpty()){
            result.add(testOtherCheckingCall[0])
        }else if (!testOnlyAssertion.isEmpty()){
            result.add(testOnlyAssertion[0].text)
        }else if (!testActualParameterExtended.isEmpty()){
            result.add(testActualParameterExtended[0])
        }
        //Predicate

        return result
    }

    else if (!Predicate().hasPredicate(test) && !Scenario().hasScenario(test)){

        val result = arrayListOf<String>()

        val testActualExpectedParameters = ActualExpectedParameters().getAllParameters(test)
        val testActualParameter = ActualParameter().getActualParameters(test)
        val testExpectedParameter = ExpectedParameter().getExpectedParameters(test)
        val testAssertionCall = AssertionCall().getAllCalls(test)
        val testOtherCheckingCall = OtherCheckingCall().getOtherCalls(test)
        val testOnlyAssertion = OnlyAssertion().getListOfAssertions(test)
        val testActualParameterExtended = ActualParameter().getActualParametersExtended(test)


        if (!testActualExpectedParameters.isEmpty()){
            result.add(testActualExpectedParameters[0][0].toString());
        } else if (!testActualParameter.isEmpty()){
            result.add(testActualParameter[0].toString())
        } else if (!testExpectedParameter.isEmpty()){
            result.add(testExpectedParameter[0].toString())
        } else if (!testAssertionCall.isEmpty()){
            result.add(testAssertionCall[0])
        }else if (!testOtherCheckingCall.isEmpty()){
            result.add(testOtherCheckingCall[0])
        }else if (!testOnlyAssertion.isEmpty()){
            result.add(testOnlyAssertion[0].text)
        }else if (!testActualParameterExtended.isEmpty()){
            result.add(testActualParameterExtended[0])
        }
        //Predicate

        val testOutVariableInit = OUTVariableInit().getONEOutVariableInit(test)?.text.orEmpty()
        val testOtherVariableInit = OtherVariableInit().getONEOtherVariableInit(test)?.text.orEmpty()
        val testOtherInitArguments = OtherInitArguments().getOtherVariableArguments(test)
            .orEmpty().map { m -> m.text.orEmpty() }
        val testControlFlowVariable = ControlFlowVariable().getCFVariable(test)
            .map { m -> m.methodExpression.referenceName.orEmpty() }
        val testStateChangingCUTCall = StateChangingCUTCall().getStateChangingCall(test)

        if (!testOutVariableInit.isEmpty()){
            result.add(testOutVariableInit);
        } else if (!testOtherVariableInit.isEmpty()){
            result.add(testOtherVariableInit)
        } else if (!testOtherInitArguments.isEmpty()){
            result.add(testOtherInitArguments[0])
        } else if (!testControlFlowVariable.isEmpty()){
            result.add(testControlFlowVariable[0])
        }else if (!testStateChangingCUTCall.isEmpty()){
            result.add(testStateChangingCUTCall)
        }
        //Scenario

        return result
    }

    else if (!Action().hasAction(test) && !Scenario().hasScenario(test)){

        val result = arrayListOf<String>()

        val testCUTCall = CUTMethodCall().getCUTCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
        val testOtherCall = OtherMethodCall().getOtherCalls(test).map { mc -> mc.methodExpression.referenceName.toString()}
        val testCutArgument = CUTCallArguments().getCUTCallArguments(test).map { arg -> arg.second.toString()
            .replace("PsiLiteralExpression", "")
            .replace("PsiLambdaExpression", "")
            .replace("PsiBinaryExpression", "")
            .replace("PsiNewExpression", "")
            .replace("PsiReferenceExpression", "")
            .replace("PsiMethodCallExpression", "")
            .replace("PsiClassObjectAccessExpression", "")}
        val testOtherArgument = OtherCallArguments().getOtherCallArguments(test).map { arg -> arg.second.toString()
            .replace("PsiLiteralExpression", "")
            .replace("PsiLambdaExpression", "")
            .replace("PsiBinaryExpression", "")
            .replace("PsiNewExpression", "")
            .replace("PsiReferenceExpression", "")
            .replace("PsiMethodCallExpression", "")
            .replace("PsiClassObjectAccessExpression", "")
        }

        if (!testCUTCall.isEmpty()){
            result.add(testCUTCall[0])
        } else if (!testOtherCall.isEmpty()){
            result.add(testOtherCall[0])
        } else if (!testCutArgument.isEmpty()){
            result.add(testCutArgument[0])
        } else if (!testOtherArgument.isEmpty()){
            result.add(testOtherArgument[0])
        }
        //Action

        val testOutVariableInit = OUTVariableInit().getONEOutVariableInit(test)?.text.orEmpty()
        val testOtherVariableInit = OtherVariableInit().getONEOtherVariableInit(test)?.text.orEmpty()
        val testOtherInitArguments = OtherInitArguments().getOtherVariableArguments(test)
            .orEmpty().map { m -> m.text.orEmpty() }
        val testControlFlowVariable = ControlFlowVariable().getCFVariable(test)
            .map { m -> m.methodExpression.referenceName.orEmpty() }
        val testStateChangingCUTCall = StateChangingCUTCall().getStateChangingCall(test)

        if (!testOutVariableInit.isEmpty()){
            result.add(testOutVariableInit);
        } else if (!testOtherVariableInit.isEmpty()){
            result.add(testOtherVariableInit)
        } else if (!testOtherInitArguments.isEmpty()){
            result.add(testOtherInitArguments[0])
        } else if (!testControlFlowVariable.isEmpty()){
            result.add(testControlFlowVariable[0])
        }else if (!testStateChangingCUTCall.isEmpty()){
            result.add(testStateChangingCUTCall)
        }
        //Scenario

        return result
    }

    return emptyList()
}


fun getCombination(test:PsiMethod, context: Context<PsiMethod, String>): String{

    val graph = Traverser.forGraph<Concept<PsiMethod, String>>
    { it.predecessors }.depthFirstPreOrder(context.bottom)

    val outDegreeForAttribute = hashMapOf<String, Set<PsiMethod>>()

    graph.forEach { c -> c.attributes.forEach { a ->
        if (!outDegreeForAttribute.containsKey(a)){
            val set = hashSetOf<PsiMethod>()
            outDegreeForAttribute[a] = set
        }
        outDegreeForAttribute[a] = outDegreeForAttribute[a].orEmpty().plus(c.objects)
    } }

    val allUniqueBits = arrayListOf<String>()

    for (concept in graph) {

        if (concept.objects.size == 1 && concept.objects.first() == test) {

            val attributes = concept.attributes

            for (attribute in attributes) {

                allUniqueBits.add(attribute)
            }
        }
    }


    return allUniqueBits.joinToString { "" }
}