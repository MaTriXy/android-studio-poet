/*
 *  Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.androidstudiopoet

import com.google.androidstudiopoet.models.ProjectBlueprint
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

class JsonConfigGenerator(val fileWriter: FileWriter) {

    fun generate(projectBlueprint: ProjectBlueprint) {
        val where = projectBlueprint.projectRoot.joinPath("as_poet_config.json")
        fileWriter.writeToFile(projectBlueprint.jsonText, where)
    }
}
