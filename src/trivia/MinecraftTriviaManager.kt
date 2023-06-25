
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier

import org.bukkit.plugin.Plugin

class MinecraftTriviaManager(
  plugin: Plugin,
  val config: TriviaConfig,
  questionSupplier: TriviaQuestionSupplier,
) : RunnableFeature(plugin) {

  override val name = "minecrafttrivia"

  override val description = "The game will regularly ask trivia questions and reward those who get it right"

  // Run once per minute
  override val taskPeriod = Constants.TICKS_PER_SECOND * 60L

  private var state: TriviaState = config.initialState

  private val engine: TriviaEngine = TriviaEngine(questionSupplier)

  val answerCommand: TriviaAnswerCommand =
    TriviaAnswerCommand(this, engine)

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val transition = state.advance(config)
    transition.perform(engine)
    state = transition.nextState
  }

}
