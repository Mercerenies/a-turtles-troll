
package com.mercerenies.turtletroll.storage

import com.mercerenies.turtletroll.NameSource
import com.mercerenies.turtletroll.ext.*

import net.kyori.adventure.text.Component

import kotlin.io.forEachLine

import java.io.Reader
import java.io.InputStreamReader

// Precondition: The file at the specified path must be nonempty.
class FortunesFile(dataPath: String) : NameSource {

  companion object {

    private fun loadFromReader(reader: Reader): List<String> {
      val list: MutableList<String> = mutableListOf()
      var curr: String = ""
      reader.forEachLine { line ->
        if (line == "%") {
          list.add(curr)
          curr = ""
        } else {
          curr += "${line}\n"
        }
      }
      return list
    }

    private fun loadResource(dataPath: String): List<String> {
      FortunesFile::class.java.getResourceAsStream(dataPath).use { stream ->
        if (stream == null) {
          throw IllegalArgumentException("No such resource at ${dataPath}")
        }
        return InputStreamReader(stream, "UTF-8").use { reader ->
          loadFromReader(reader)
        }
      }
    }

  }

  private val dataSource: List<String> = loadResource(dataPath)

  override fun sampleName(): Component =
    Component.text(dataSource.sample()!!.trim())

}
