
package com.mercerenies.turtletroll.temperature

import org.bukkit.entity.Player

fun interface BiomeSafetyCondition {

  companion object {

    fun safe(explanation: String) =
      Result(true, explanation)

    fun unsafe(explanation: String) =
      Result(false, explanation)

  }

  data class Result(
    val safe: Boolean,
    val explanation: String,
  ) {
    fun isSafe() = safe
  }

  fun isSafe(player: Player): Boolean =
    evaluate(player).isSafe()

  fun evaluate(player: Player): Result

}
