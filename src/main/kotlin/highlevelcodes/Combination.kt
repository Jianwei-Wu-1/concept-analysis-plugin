package edu.udel.highlevelcodes

import com.intellij.psi.PsiMethod

open class Combination {

    fun hasCombination(test: PsiMethod): Boolean{

        if (Action().hasAction(test) && Predicate().hasPredicate(test) && Scenario().hasScenario(test)){
            return true
        }

        else if (!Action().hasAction(test) && !Predicate().hasPredicate(test)){
            return true
        }

        else if (!Predicate().hasPredicate(test) && !Scenario().hasScenario(test)){
            return true
        }

        else if (!Action().hasAction(test) && !Scenario().hasScenario(test)){
            return true
        }

        return false
    }

    fun getCombination(test: PsiMethod): String {

        if (!Action().hasAction(test) && !Predicate().hasPredicate(test) && !Scenario().hasScenario(test)){
            return "action + predicate + scenario"
        }

        else if (!Action().hasAction(test) && !Predicate().hasPredicate(test)){
            return "action + predicate"
        }

        else if (!Predicate().hasPredicate(test) && !Scenario().hasScenario(test)){
            return "predicate + scenario"
        }

        else if (!Action().hasAction(test) && !Scenario().hasScenario(test)){
            return "action + scenario"
        }

        return "N/A"
    }
}