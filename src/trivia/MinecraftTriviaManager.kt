
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.command.OptionalUnaryCommand

import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class MinecraftTriviaManager(
  plugin: Plugin,
  val config: TriviaConfig,
  private val questionSupplier: TriviaQuestionSupplier,
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

  val triviaAskCommand: Command = object : OptionalUnaryCommand() {

    override fun onCommand(sender: CommandSender, arg: String?): Boolean {
      val transition = TriviaStateTransition.AskQuestion
      if (arg == null) {
        // Ask a random question, like normal
        runTransition(transition)
        return true
      } else {
        try {
          transition.performSpecificQuestion(engine, arg)
        } catch (exc: NoSuchElementException) {
          Messages.sendMessage(sender, Component.text("Invalid question ID: $arg", NamedTextColor.RED))
          Bukkit.getLogger().warning("User $sender attempted to ask for an invalid question ID: $arg")
          return false
        }
        state = transition.nextState
        return true
      }
    }

    override fun onTabComplete(sender: CommandSender, arg: String?): List<String>? =
      arg?.let {
        questionSupplier.ids.filter { id -> id.startsWith(it) }
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
