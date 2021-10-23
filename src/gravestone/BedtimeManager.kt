
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.ext.*

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.Listener
import org.bukkit.command.CommandSender


class BedtimeManager(plugin: Plugin) : RunnableFeature(plugin), Listener {

  companion object {
    val DAWN_TIME = 0L
    val DUSK_TIME = 12000L

    val EASY = listOf(
      DeathCondition.True, DeathCondition.MustBeMimic, DeathCondition.MustBeVector,
      DeathCondition.MustBeAngel, DeathCondition.FireDamage, DeathCondition.Falling,
      DeathCondition.MustBeBee, DeathCondition.MustBeSilverfish,
    )

    val MEDIUM = listOf(
      DeathCondition.Explosion, DeathCondition.MustBeZombie, DeathCondition.MustBeGhast,
      DeathCondition.MustBeRavager,
    )

    val HARD = listOf(
      DeathCondition.Drowning, DeathCondition.MustBeEnderman, DeathCondition.MustBeIronGolem,
      DeathCondition.MustBeBlaze,
    )

    val CONDITION_LIST = listOf(
      Weight(EASY, 5.0),
      Weight(MEDIUM, 3.0),
      Weight(HARD, 1.0),
    )

    val ANGRY_MESSAGE = "The gods are angry; no one shall sleep tonight!"

    val SATISFIED_MESSAGE = "The gods are appeased today; everyone is free to sleep."

    val DISABLED_MESSAGE = "This feature is currently disabled; the gods are not interfering with your sleep."

    fun requestMessage(condition: DeathCondition): String =
      "Today, the gods would like to see someone die ${condition.description}"

    fun appeasedMessage(player: Player): String =
      "${player.displayName} has appeased the gods! You may sleep tonight."

    fun getSystemTime(): Long {
      for (world in Bukkit.getServer().getWorlds()) {
        if (world.environment == World.Environment.NORMAL) {
          return world.getTime()
        }
      }
      return 0L // That's not good :(
    }

    private fun chooseCondition(): DeathCondition =
      // Choose difficulty first, then choose a death condition in that difficulty tier
      sample(CONDITION_LIST).sample()!!

  }

  private enum class State {
    Daytime,
    Nighttime,
  }

  override val name: String = "bedtime"

  override val description: String = "The gods must be appeased with a condition in order to allow players to sleep"

  override val taskPeriod = Constants.TICKS_PER_SECOND * 5L

  override val taskDelay = Constants.TICKS_PER_SECOND * 5L

  private var state: State = State.Nighttime

  private var isAppeased: Boolean = true

  private var condition: DeathCondition = DeathCondition.True

  val BedtimeCommand = object : TerminalCommand() {

    private fun getMessageToSend(): String =
      if (!isEnabled()) {
        DISABLED_MESSAGE
      } else if (isAppeased) {
        SATISFIED_MESSAGE
      } else {
        when (state) {
          State.Daytime -> {
            requestMessage(condition)
          }
          State.Nighttime -> {
            ANGRY_MESSAGE
          }
        }
      }

    override fun onCommand(sender: CommandSender): Boolean {
      val message = getMessageToSend()
      sender.sendMessage(message)
      return true
    }

  }

  override fun enable() {
    super.enable()
    isAppeased = true
    state = State.Nighttime
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }

    val time = getSystemTime()

    when (state) {

      State.Nighttime -> {
        if ((time > DAWN_TIME) && (time < DUSK_TIME)) {
          state = State.Daytime
          isAppeased = false
          condition = chooseCondition()
          Bukkit.broadcastMessage(requestMessage(condition))
        }
      }

      State.Daytime -> {
        if ((time < DAWN_TIME) || (time > DUSK_TIME)) {
          state = State.Nighttime
          if (!isAppeased) {
            Bukkit.broadcastMessage(ANGRY_MESSAGE)
          }
        }
      }

    }

  }

  @EventHandler(priority=EventPriority.HIGH)
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }

    if ((state == State.Daytime) && (!isAppeased)) {
      val cause = CauseOfDeath.identify(event)
      if (condition.test(cause)) {
        Bukkit.broadcastMessage(appeasedMessage(event.entity))
        isAppeased = true
      }
    }

  }

  @EventHandler(priority=EventPriority.HIGH)
  fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
    if (!isEnabled()) {
      return
    }

    if (!event.isCancelled()) {
      if (event.bedEnterResult == PlayerBedEnterEvent.BedEnterResult.OK) {
        // Check that the gods are happy
        if (!isAppeased) {
          event.setCancelled(true)
          event.player.sendMessage(ANGRY_MESSAGE)
        }
      }
    }

  }

}
