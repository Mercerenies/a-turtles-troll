
package com.mercerenies.turtletroll.trivia

// State machine for the trivia engine

sealed interface TriviaState {

  class Idle(override val ticks: Int) : TriviaState {
    override fun advance(config: TriviaConfig): TriviaStateTransition =
      if (ticks > config.minutesBetweenQuestions - 1) {
        TriviaStateTransition.AskQuestion
      } else {
        TriviaStateTransition.Simple(Idle(ticks + 1))
      }
  }

  class Asking(override val ticks: Int) : TriviaState {
    override fun advance(config: TriviaConfig): TriviaStateTransition =
      if (ticks > config.minutesBetweenQuestions - 1) {
        TriviaStateTransition.JudgeQuestion
      } else {
        TriviaStateTransition.Simple(Asking(ticks + 1))
      }
  }

  val ticks: Int

  fun advance(config: TriviaConfig): TriviaStateTransition

}
