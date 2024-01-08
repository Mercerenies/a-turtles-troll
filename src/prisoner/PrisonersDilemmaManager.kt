
package com.mercerenies.turtletroll.prisoner

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.tryCancel
import com.mercerenies.turtletroll.util.runTaskLater
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.happening.RandomEvent
import com.mercerenies.turtletroll.happening.RandomEventState
import com.mercerenies.turtletroll.happening.withTitle
import com.mercerenies.turtletroll.happening.withCooldown
import com.mercerenies.turtletroll.happening.boundToFeature

import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class PrisonersDilemmaManager(
  private val plugin: Plugin,
  private val rewardSupplier: PrisonerRewardSupplier,
  private val responseTimeMinutes: Int,
) : AbstractFeature() {

  companion object {

    private fun chooseVictims(): Victims {
      if (Bukkit.getOnlinePlayers().size < 2) {
        throw IllegalStateException("Not enough players to run PrisonersDilemma")
      }
      val targets = Bukkit.getOnlinePlayers().shuffled().take(2)
      return Victims(targets[0], targets[1])
    }

  }

  data class Victims(
    val player1: Player,
    val player2: Player,
  )

  override val name = "prisonersdilemma"

  override val description = "The game will put two players in a prisoner's dilemma at random"

  private var activeDilemma: PrisonersDilemma? = null

  private var judgmentTask: BukkitTask? = null

  val splitCommand: Command = DilemmaResponseCommand(PlayerResponse.SPLIT) { activeDilemma }
  val stealCommand: Command = DilemmaResponseCommand(PlayerResponse.STEAL) { activeDilemma }

  val dilemmaJudgeCommand: Command = object : TerminalCommand() {
    override fun onCommand(sender: CommandSender): Boolean {
      doJudgment()
      return true
    }
  }

  val randomEvent: RandomEvent =
    PrisonersDilemmaEvent()
      .withTitle("Prisoner's Dilemma!")
      .withCooldown(6)
      .boundToFeature(this)

  private inner class PrisonersDilemmaEvent() : RandomEvent {
    override val name = "prisonersdilemma"
    override val baseWeight = 0.9
    override val deltaWeight = 0.1

    override fun canFire(state: RandomEventState): Boolean =
      (activeDilemma == null) && (Bukkit.getOnlinePlayers().size >= 2)

    override fun fire(state: RandomEventState) {
      setupNewDilemma()
    }

  }

  private fun scheduleJudgment() {
    val delay = (responseTimeMinutes * Constants.TICKS_PER_MINUTE).toLong()
    judgmentTask = Bukkit.getScheduler().runTaskLater(plugin, delay) {
      doJudgment()
    }
  }

  private fun doJudgment() {
    activeDilemma?.judgeResponses()
    activeDilemma = null
    // Cancel any judgment tasks, if they exist.
    judgmentTask?.tryCancel()
    judgmentTask = null
  }

  private fun setupNewDilemma() {
    val victims = try {
      chooseVictims()
    } catch (e: IllegalStateException) {
      // This only happens if a player logs out on basically the exact
      // wrong frame, putting the player count below 2. In this case,
      // just don't run the event.
      //return ////
      Victims(Bukkit.getOnlinePlayers().first(), Bukkit.getOnlinePlayers().first())
    }
    val newDilemma = PrisonersDilemma(victims, rewardSupplier.chooseReward())
    activeDilemma = newDilemma
    newDilemma.notifyAllPlayers()
    scheduleJudgment()
  }

}
