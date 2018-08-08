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

package com.google.androidstudiopoet.input

open class ModuleConfig {
    lateinit var moduleName: String
    var java: CodeConfig? = null
    var kotlin: CodeConfig? = null
    var useKotlin: Boolean = false
    var extraLines: List<String>? = null
    var dependencies: List<DependencyConfig>? = null
    var plugins: List<PluginConfig>? = null
    var generateTests: Boolean = true
}
