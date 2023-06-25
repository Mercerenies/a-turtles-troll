
package com.mercerenies.turtletroll.trivia

interface TriviaStateTransition {

  class Simple(
    override val nextState: TriviaState
  ) : TriviaStateTransition {

    override fun perform(engine: TriviaEngine) {}

  }

  object AskQuestion : TriviaStateTransition {

    override val nextState: TriviaState =
      TriviaState.Asking(0)

    override fun perform(engine: TriviaEngine) {
      engine.askNewQuestion()
    }

  }

  object JudgeQuestion : TriviaStateTransition {

    override val nextState: TriviaState =
      TriviaState.Idle(0)

    override fun perform(engine: TriviaEngine) {
      val result = engine.judgeAnswers()
      val reward = engine.chooseReward()
      TriviaResult.assignRewards(result, reward)
    }

  }

  fun perform(engine: TriviaEngine): Unit

  val nextState: TriviaState

}
