
package com.mercerenies.turtletroll.trivia

import org.bukkit.Bukkit
import org.bukkit.SoundCategory

interface TriviaStateTransition {

  class Simple(
    override val nextState: TriviaState
  ) : TriviaStateTransition {

    override fun perform(engine: TriviaEngine) {}

  }

  object AskQuestion : TriviaStateTransition {

    val TRIVIA_START_SOUND = "custom.event.trivia"

    override val nextState: TriviaState =
      TriviaState.Asking(0)

    override fun perform(engine: TriviaEngine) {
      engine.askNewQuestion()
      notifyAllPlayers()
    }

    private fun notifyAllPlayers() {
      for (player in Bukkit.getOnlinePlayers()) {
        player.playSound(player.location, TRIVIA_START_SOUND, SoundCategory.NEUTRAL, 1.0f, 1.0f)
      }
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
