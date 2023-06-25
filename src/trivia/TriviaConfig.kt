
package com.mercerenies.turtletroll.trivia

data class TriviaConfig(
  val minutesBetweenQuestions: Int,
  val minutesToAnswer: Int,
) {

  companion object {
    val DEFAULT = TriviaConfig(
      minutesBetweenQuestions = 10,
      minutesToAnswer = 2,
    )
  }

  val initialState: TriviaState =
    TriviaState.Idle(minutesBetweenQuestions - 1)

}
