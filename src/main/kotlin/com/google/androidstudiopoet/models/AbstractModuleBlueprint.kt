/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.joinPath

abstract class AbstractModuleBlueprint(val name: String,
                                   root: String,
                                   val useKotlin: Boolean,
                                   val dependencies: Set<Dependency>,
                                   javaPackageCount: Int, javaClassCount: Int, javaMethodsPerClass: Int,
                                   kotlinPackageCount: Int, kotlinClassCount: Int, kotlinMethodsPerClass: Int,
                                   val extraLines: List<String>?,
                                   val generateTests : Boolean) {

    val moduleRoot = root.joinPath(name)
    val moduleDependencies = dependencies.filterIsInstance<ModuleDependency>()
    private val methodsToCallWithIn = moduleDependencies.map { it.methodToCall }

    val packagesBlueprint by lazy {
        PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
                kotlinClassCount, kotlinMethodsPerClass, moduleRoot, name, methodsToCallWithIn, generateTests)
    }

    val methodToCallFromOutside by lazy {
        packagesBlueprint.methodToCallFromOutside
    }
}