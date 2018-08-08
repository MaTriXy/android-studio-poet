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

package com.google.androidstudiopoet.testutils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import java.awt.image.BufferedImage

fun <T> Collection<T>.assertEmpty() {
    assertTrue(this.isEmpty())
}

fun <T> Collection<T>.assertSize(expectedSize: Int) {
    assertEquals(expectedSize, size)
}

fun <T> Collection<T>.assertContains(item: T) {
    assertTrue(this.contains(item))
}

fun <T> Collection<T>.assertNotContains(item: T) {
    assertFalse(this.contains(item))
}

fun Any.assertEquals(expected: Any) {
    assertEquals(expected, this)
}

inline fun <T, R> assertOn(receiver: T, block: T.() -> R): R = receiver.block()

fun Boolean.assertTrue() {
    assertTrue(this)
}

fun Boolean.assertFalse() {
    assertFalse(this)
}

fun Any?.assertNull() {
    assertEquals(null, this)
}

fun BufferedImage.assertEqualImage(bufferedImage: BufferedImage) {
    this.height.assertEquals(bufferedImage.height)
    this.width.assertEquals(bufferedImage.width)
    for (y in 0..(this.height - 1)) {
        for  (x in 0..(this.width - 1)){
            this.getRGB(x, y).assertEquals(bufferedImage.getRGB(x, y))
        }
    }
}