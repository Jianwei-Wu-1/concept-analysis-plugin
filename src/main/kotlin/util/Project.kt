package util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import edu.udel.util.isTestClass

fun Project.testClasses(): Collection<PsiClass> {

    val scope = GlobalSearchScope.projectScope(this)

    return AllClassesSearch.search(scope, this)
        .findAll()
        .filter { it.isTestClass() }

}