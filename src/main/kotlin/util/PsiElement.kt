package edu.udel.util

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

inline fun <reified T: PsiElement> PsiElement.find(): Collection<T> =
    PsiTreeUtil.collectElementsOfType(this, T::class.java)