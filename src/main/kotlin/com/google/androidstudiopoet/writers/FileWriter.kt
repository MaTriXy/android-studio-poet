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

package com.google.androidstudiopoet.writers

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FileWriter {
    fun writeToFile(content: String, path: String) {
        var writer: BufferedWriter? = null
        try {
            val file = File(path)
            file.createNewFile()

            writer = BufferedWriter(FileWriter(file))

            writer.write(content)

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun mkdir(path: String) {
        val file = File(path)
        if (file.parentFile != null && !file.parentFile.exists()) {
            mkdir(file.parent)
        }
        File(path).mkdir()
    }

    fun delete(path: String) {
        val file = File(path)
        file.listFiles()?.forEach { delete(it.absolutePath) }
        file.delete()
    }
}