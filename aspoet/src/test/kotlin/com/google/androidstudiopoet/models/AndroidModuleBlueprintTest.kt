package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.input.*
import com.google.androidstudiopoet.testutils.*
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class AndroidModuleBlueprintTest {
    private val resourcesConfig0: ResourcesConfig = mock()

    @Test
    fun `blueprint create proper activity names`() {
        val blueprint = getAndroidModuleBlueprint(numOfActivities = 2)

        blueprint.activityNames.assertEquals(listOf("Activity0", "Activity1"))
    }

    @Test
    fun `blueprint creates activity blueprint with java class when java code exists`() {
        whenever(resourcesConfig0.layoutCount).thenReturn(1)

        val androidModuleBlueprint = getAndroidModuleBlueprint()

        assertOn(androidModuleBlueprint) {
            activityBlueprints.assertEquals(listOf(ActivityBlueprint(
                    "Activity0",
                    androidModuleBlueprint.resourcesBlueprint!!.layoutBlueprints[0],
                    androidModuleBlueprint.packagePath,
                    androidModuleBlueprint.packageName,
                    androidModuleBlueprint.packagesBlueprint.javaPackageBlueprints[0].classBlueprints[0],
                    listOf(), false)))
        }
    }

    @Test
    fun `blueprint creates activity blueprint with koltin class when java code doesn't exist`() {
        whenever(resourcesConfig0.layoutCount).thenReturn(1)

        val androidModuleBlueprint = getAndroidModuleBlueprint(
                javaConfig = CodeConfig().apply {
                    packages = 0
                    classesPerPackage = 0
                    methodsPerClass = 0
                }
        )

        assertOn(androidModuleBlueprint) {
            activityBlueprints[0].classToReferFromActivity.assertEquals(
                    androidModuleBlueprint.packagesBlueprint.kotlinPackageBlueprints[0].classBlueprints[0])
        }
    }

    @Test
    fun `hasDataBinding is false when data binding config is null`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = null)

        androidModuleBlueprint.hasDataBinding.assertFalse()
    }

    @Test
    fun `hasDataBinding is false when data binding config has zero listener count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = DataBindingConfig(listenerCount = 0))

        androidModuleBlueprint.hasDataBinding.assertFalse()
    }

    @Test
    fun `hasDataBinding is true when data binding config has positive listener count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = DataBindingConfig(listenerCount = 2))

        androidModuleBlueprint.hasDataBinding.assertTrue()
    }

    @Test
    fun `hasDataBinding is false when data binding config has negative listener count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = DataBindingConfig(listenerCount = -1))

        androidModuleBlueprint.hasDataBinding.assertFalse()
    }

    private fun getAndroidModuleBlueprint(
            name: String = "androidAppModule1",
            numOfActivities: Int = 1,
            resourcesConfig: ResourcesConfig = resourcesConfig0,
            projectRoot: String = "root",
            hasLaunchActivity: Boolean = true,
            useKotlin: Boolean = false,
            dependencies: Set<Dependency> = setOf(),
            productFlavorConfigs: List<FlavorConfig>? = null,
            buildTypeConfigs: List<BuildTypeConfig>? = null,
            javaConfig: CodeConfig? = defaultCodeConfig(),
            kotlinConfig: CodeConfig? = defaultCodeConfig(),
            extraLines: List<String>? = null,
            generateTests: Boolean = true,
            dataBindingConfig: DataBindingConfig? = null,
            androidBuildConfig: AndroidBuildConfig = AndroidBuildConfig(),
            pluginConfigs: List<PluginConfig>? = null
    ) = AndroidModuleBlueprint(name, numOfActivities, resourcesConfig, projectRoot, hasLaunchActivity, useKotlin,
            dependencies, productFlavorConfigs, buildTypeConfigs, javaConfig, kotlinConfig,
            extraLines, generateTests, dataBindingConfig, androidBuildConfig, pluginConfigs)

    private fun defaultCodeConfig() = CodeConfig().apply {
        packages = 1
        classesPerPackage = 1
        methodsPerClass = 1
    }
}