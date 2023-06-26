
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.config.CheckedConfigOptions

data class TriviaConfig(
  val minutesBetweenQuestions: Int,
  val minutesToAnswer: Int,
) {

  companion object {

    val DEFAULT = TriviaConfig(
      minutesBetweenQuestions = 10,
      minutesToAnswer = 2,
    )

    fun fromGlobalConfig(config: CheckedConfigOptions): TriviaConfig =
      TriviaConfig(
        minutesBetweenQuestions = config.getInt("minecrafttrivia.minutes_between_questions"),
        minutesToAnswer = config.getInt("minecrafttrivia.minutes_to_answer"),
      )

  }

  val initialState: TriviaState =
    TriviaState.Idle(minutesBetweenQuestions - 1)

}
