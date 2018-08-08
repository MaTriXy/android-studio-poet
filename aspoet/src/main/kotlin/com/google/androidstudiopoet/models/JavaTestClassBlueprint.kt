package com.google.androidstudiopoet.models

import org.junit.Test

class JavaTestClassBlueprint(packageName: String, private val classNumber: Int, private val methodsPerClass: Int,
                             private val where: String) :
        ClassBlueprint(packageName, "Foo${classNumber}Test") {

    override fun getFieldBlueprints(): List<FieldBlueprint> = listOf()

    override fun getMethodBlueprints(): List<MethodBlueprint> {
        return (0 until methodsPerClass)
                .map { i ->
                    val statements = listOf("new Foo$classNumber().foo$i()")
                    MethodBlueprint("testFoo$i", statements, listOf(Test::class.java.name))
                }
    }

    override fun getClassPath(): String = "$where/$packageName/$className.java"

    override fun getMethodToCallFromOutside(): MethodToCall? = null
}