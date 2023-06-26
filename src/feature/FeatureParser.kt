
package com.mercerenies.turtletroll.feature

// TODO Is this unused?
object FeatureParser {

  sealed interface ParseResult

  data class On(val featureName: String) : ParseResult
  data class Off(val featureName: String) : ParseResult
  object List : ParseResult {
    override fun toString(): String = "FeatureParser.List"
  }

  fun parse(args: kotlin.collections.List<String>): ParseResult? {
    if (args.isEmpty()) {
      return null
    }
    when (args[0]) {
      "on" -> {
        return if (args.size == 2) {
          On(args[1])
        } else {
          null
        }
      }
      "off" -> {
        return if (args.size == 2) {
          Off(args[1])
        } else {
          null
        }
      }
      "list" -> {
        return if (args.size == 1) {
          List
        } else {
          null
        }
      }
      else -> {
        return null
      }
    }
  }

}
