
package com.mercerenies.turtletroll.trivia

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.tryCancel
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.trivia.question.TriviaQuestionSupplier
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.command.OptionalUnaryCommand
import com.mercerenies.turtletroll.happening.RandomEvent
import com.mercerenies.turtletroll.happening.RandomEventState
import com.mercerenies.turtletroll.happening.withCooldown
import com.mercerenies.turtletroll.happening.boundToFeature

import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.SoundCategory
import org.bukkit.scheduler.BukkitRunnable

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class MinecraftTriviaManager(
  private val plugin: Plugin,
  val config: TriviaConfig,
  private val questionSupplier: TriviaQuestionSupplier,
) : AbstractFeature() {

  companion object {
    val TRIVIA_START_SOUND = "custom.event.trivia"
  }

  override val name = "minecrafttrivia"

  override val description = "The game will regularly ask trivia questions and reward those who get it right"

  private val engine: TriviaEngine = TriviaEngine(questionSupplier)

  val answerCommand: Command =
    TriviaAnswerCommand(this, engine)

  val triviaAskCommand: Command = object : OptionalUnaryCommand() {

    override fun onCommand(sender: CommandSender, arg: String?): Boolean {
      if (arg == null) {
        askRandomQuestion()
        return true
      } else {
        try {
          askSpecificQuestion(arg)
        } catch (exc: NoSuchElementException) {
          Messages.sendMessage(sender, Component.text("Invalid question ID: $arg", NamedTextColor.RED))
          Bukkit.getLogger().warning("User $sender attempted to ask for an invalid question ID: $arg")
          return false
        }
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
      cancelExistingJudgeRunnable()
      judgeQuestion()
      return true
    }
  }

  private var judgmentRunnable: TriviaJudgeRunnable? = null

  val triviaRandomEvent: RandomEvent =
    TriviaEvent().withCooldown(4).boundToFeature(this)

  private inner class TriviaEvent() : RandomEvent {
    override val name = "trivia"
    override val baseWeight = 1.0
    override val deltaWeight = 0.1

    override fun canFire(state: RandomEventState): Boolean =
      !engine.hasQuestion()

    override fun fire(state: RandomEventState) {
      askRandomQuestion()
    }

  }

  private inner class TriviaJudgeRunnable() : BukkitRunnable() {
    override fun run() {
      judgeQuestion()
    }
  }

  private fun cancelExistingJudgeRunnable() {
    // If we run the command to judge trivia prematurely via debug
    // commands, then we should cancel the "normal" flow of judging
    // trivia.
    judgmentRunnable?.tryCancel()
  }

  private fun askRandomQuestion() {
    engine.askNewRandomQuestion()
    notifyAllPlayers()
    scheduleJudgment()
  }

  private fun askSpecificQuestion(questionId: String) {
    engine.askQuestion(questionId)
    notifyAllPlayers()
    scheduleJudgment()
  }

  private fun scheduleJudgment() {
    val runnable = TriviaJudgeRunnable()
    judgmentRunnable = runnable
    runnable.runTaskLater(plugin, Constants.TICKS_PER_MINUTE.toLong() * config.minutesToAnswer)
  }

  private fun notifyAllPlayers() {
    for (player in Bukkit.getOnlinePlayers()) {
      player.playSound(player.location, TRIVIA_START_SOUND, SoundCategory.NEUTRAL, 1.0f, 1.0f)
    }
  }

  private fun judgeQuestion() {
    if (!engine.hasQuestion()) {
      // Nothing to judge
      return
    }
    val reward = engine.chooseReward()
    val result = engine.judgeAnswers()
    TriviaResult.assignRewards(result, reward)
  }

}
