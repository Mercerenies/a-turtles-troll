
package com.mercerenies.turtletroll.trivia

import org.bukkit.entity.Player

data class TriviaResult(
  val correctAnswerers: List<Player>,
  val incorrectAnswerers: List<Player>,
) {
  companion object {
    val EMPTY: TriviaResult = TriviaResult(listOf(), listOf())
  }
}
