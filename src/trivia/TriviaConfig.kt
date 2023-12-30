
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.config.CheckedConfigOptions

data class TriviaConfig(
  val minutesToAnswer: Int,
) {

  companion object {

    val DEFAULT = TriviaConfig(
      minutesToAnswer = 2,
    )

    fun fromGlobalConfig(config: CheckedConfigOptions): TriviaConfig =
      TriviaConfig(
        minutesToAnswer = config.getInt("minecrafttrivia.minutes_to_answer"),
      )

  }

}
