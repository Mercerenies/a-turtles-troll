
package com.mercerenies.turtletroll.trivia

interface TriviaStateTransition {

  class Simple(
    override val nextState: TriviaState
  ) : TriviaStateTransition {

    override fun perform() {}

  }

  object AskQuestion : TriviaStateTransition {

    override val nextState: TriviaState =
      TriviaState.Asking(0)

    override fun perform() {
      // TODO
    }

  }

  object JudgeQuestion : TriviaStateTransition {

    override val nextState: TriviaState =
      TriviaState.Idle(0)

    override fun perform() {
      // TODO
    }

  }

  fun perform(): Unit

  val nextState: TriviaState

}
