/*
 *  Copyright 2017 Google Inc.
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

package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.JsonConfigGenerator
import com.google.androidstudiopoet.generators.android_modules.AndroidModuleGenerator
import com.google.androidstudiopoet.generators.project.GradleSettingsGenerator
import com.google.androidstudiopoet.generators.project.GradlewGenerator
import com.google.androidstudiopoet.generators.project.ProjectBuildGradleGenerator
import com.google.androidstudiopoet.models.ModuleBlueprint
import com.google.androidstudiopoet.models.ProjectBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis

class SourceModuleGenerator(private val moduleBuildGradleGenerator: ModuleBuildGradleGenerator,
                            private val gradleSettingsGenerator: GradleSettingsGenerator,
                            private val projectBuildGradleGenerator: ProjectBuildGradleGenerator,
                            private val androidModuleGenerator: AndroidModuleGenerator,
                            private val packagesGenerator: PackagesGenerator,
                            private val dependencyGraphGenerator: DependencyGraphGenerator,
                            private val jsonConfigGenerator: JsonConfigGenerator,
                            private val fileWriter: FileWriter) {

    fun generate(projectBlueprint: ProjectBlueprint) = runBlocking {

        fileWriter.delete(projectBlueprint.projectRoot)
        fileWriter.mkdir(projectBlueprint.projectRoot)

        GradlewGenerator.generateGradleW(projectBlueprint.projectRoot, projectBlueprint)
        projectBuildGradleGenerator.generate(projectBlueprint.buildGradleBlueprint)
        gradleSettingsGenerator.generate(projectBlueprint.projectName, projectBlueprint.allModulesNames, projectBlueprint.projectRoot)

        print("Writing modules...")
        val timeSpent = measureTimeMillis {
            // TODO: use topological sort (as generated in hasCircularDependencies) to reduce the need of cached methodToCall
            val allJobs = mutableListOf<Job>()
            projectBlueprint.moduleBlueprints.asReversed().forEach { blueprint ->
                val job = launch {
                    writeModule(blueprint)
                }
                allJobs.add(job)
            }
            var randomCount: Long = 0
            projectBlueprint.androidModuleBlueprints.asReversed().forEach { blueprint ->
                val random = Random(randomCount++)
                val job = launch {
                    androidModuleGenerator.generate(blueprint, random)
                }
                allJobs.add(job)
            }

            // Wait for jobs to finish and write progress
            var nextPercentage = 0
            var jobsDone = 0
            for (job in allJobs) {
                job.join()
                jobsDone++
                val currentPercentage = (100 * jobsDone) / allJobs.size
                if (currentPercentage >= nextPercentage) {
                    nextPercentage = currentPercentage + 1
                    print(String.format("\rWriting modules... %3d%%", currentPercentage))
                }
            }
        }
        println("\rWriting modules... done in $timeSpent ms")
        dependencyGraphGenerator.generate(projectBlueprint)
        jsonConfigGenerator.generate(projectBlueprint)
    }

    private fun writeModule(moduleBlueprint: ModuleBlueprint) {
        val moduleRootFile = File(moduleBlueprint.moduleRoot)
        moduleRootFile.mkdir()

        writeLibsFolder(moduleRootFile)
        moduleBuildGradleGenerator.generate(moduleBlueprint.buildGradleBlueprint)

        packagesGenerator.writePackages(moduleBlueprint.packagesBlueprint)
    }

    private fun writeLibsFolder(moduleRootFile: File) {
        // write libs
        val libRoot = moduleRootFile.toString() + "/libs/"
        File(libRoot).mkdir()
    }
}
