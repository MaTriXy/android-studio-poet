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

package com.google.androidstudiopoet.generators.android_modules.resources

import com.google.androidstudiopoet.models.ClassBlueprint
import com.google.androidstudiopoet.models.ImageViewBlueprint
import com.google.androidstudiopoet.models.LayoutBlueprint
import com.google.androidstudiopoet.models.TextViewBlueprint
import com.google.androidstudiopoet.utils.fold
import com.google.androidstudiopoet.writers.FileWriter

private const val XML_FILE_PREFIX = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

class LayoutResourcesGenerator(val fileWriter: FileWriter) {

    fun generate(blueprint: LayoutBlueprint) {

        var layoutText = XML_FILE_PREFIX
        if (blueprint.hasLayoutTag) {
            layoutText = generateLayoutTag(blueprint)
        } else {
            layoutText += generateScrollView(blueprint)
        }


        fileWriter.writeToFile(layoutText, blueprint.filePath)
    }

    private fun generateLayoutTag(blueprint: LayoutBlueprint): String {
        return """<layout xmlns:android="http://schemas.android.com/apk/res/android">
    ${generateDataTag(blueprint)}
    ${generateScrollView(blueprint)}
</layout>
            """
    }

    private fun generateDataTag(blueprint: LayoutBlueprint): String {
        return """<data>
${generateVariableTags(blueprint.classesToBind)}
</data>
            """
    }

    private fun generateVariableTags(blueprints: List<ClassBlueprint>): String {
        return blueprints
                .map { "<variable name=\"${it.className.decapitalize()}\" type=\"${it.fullClassName}\"/>\n" }
                .fold()
    }

    private fun generateScrollView(blueprint: LayoutBlueprint): String {
        val textViews = generateTextViews(blueprint.textViewsBlueprints)
        val imageViews = generateImageViews(blueprint.imageViewsBlueprints)
        val includeLayoutTags = generateIncludeLayoutTags(blueprint.layoutsToInclude)

        return getScrollViews(textViews, imageViews, includeLayoutTags, !blueprint.hasLayoutTag)
    }

    private fun getScrollViews(textViews: String, imageViews: String, includeLayoutTags: String,
                               isNotInsideLayoutTag: Boolean): String {
        return """
    <ScrollView
            ${if (isNotInsideLayoutTag) "xmlns:android=\"http://schemas.android.com/apk/res/android\"" else ""}
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">
            $textViews
            $imageViews
            $includeLayoutTags
        </LinearLayout>
    </ScrollView>"""
    }

    private fun generateTextViews(textViewBlueprints: List<TextViewBlueprint>): String {
        return textViewBlueprints.map {
            """<TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/${it.stringName}"
        ${if (it.hasAction) "android:onClick=\"@{${it.onClickAction}}\"" else ""}
/>"""
        }.fold()
    }

    private fun generateImageViews(imageViewBlueprints: List<ImageViewBlueprint>): String {
        return imageViewBlueprints.map {
            """<ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/${it.imageName}"
        ${if (it.hasAction) "android:onClick=\"@{${it.onClickAction}}\"" else ""}
/>
            """
        }.fold()
    }

    private fun generateIncludeLayoutTags(layoutsToInclude: List<String>): String {
        return layoutsToInclude.map {
            //language=XML
            """<include
        layout="@layout/$it"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
/>
            """
        }.fold()
    }
}