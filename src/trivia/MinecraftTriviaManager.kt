
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.TerminalCommand

import org.bukkit.command.CommandSender
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
  override val taskDelay = Constants.TICKS_PER_SECOND * 60L

  private var state: TriviaState = config.initialState

  private val engine: TriviaEngine = TriviaEngine(questionSupplier)

  val answerCommand: Command =
    TriviaAnswerCommand(this, engine)

  val triviaAskCommand: Command = object : TerminalCommand() {
    override fun onCommand(sender: CommandSender): Boolean {
      runTransition(TriviaStateTransition.AskQuestion)
      return true
    }
  }

  val triviaJudgeCommand: Command = object : TerminalCommand() {
    override fun onCommand(sender: CommandSender): Boolean {
      runTransition(TriviaStateTransition.JudgeQuestion)
      return true
    }
  }

  private fun runTransition(transition: TriviaStateTransition) {
    transition.perform(engine)
    state = transition.nextState
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val transition = state.advance(config)
    runTransition(transition)
  }

}
