package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.fold

data class ActivityBlueprint(val className: String, val layout: LayoutBlueprint, val where: String, val packageName: String,
                             val classToReferFromActivity: ClassBlueprint, val listenerClassesForDataBinding: List<ClassBlueprint>,
                             private val useButterknife: Boolean) {
    val hasDataBinding = listenerClassesForDataBinding.isNotEmpty()
    val dataBindingClassName = "$packageName.databinding.${layout.name.toDataBindingShortClassName()}"


}

private fun String.toDataBindingShortClassName(): String = this.split("_").map { it.capitalize() }.fold() + "Binding"
