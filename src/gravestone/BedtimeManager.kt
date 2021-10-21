
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.entity.Player
import org.bukkit.entity.PufferFish
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.Listener

import kotlin.random.Random

class BedtimeManager(val plugin: Plugin) : RunnableFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20L
    val DAWN_TIME = 0L
    val DUSK_TIME = 12000L

    fun getSystemTime(): Long {
      for (world in Bukkit.getServer().getWorlds()) {
        if (world.environment == World.Environment.NORMAL) {
          return world.getTime()
        }
      }
      return 0L // That's not good :(
    }

  }

  private enum class State {
    Daytime,
    Nighttime,
  }

  override val name: String = "bedtime"

  override val description: String = "The gods must be appeased with a condition in order to allow players to sleep"

  private var state: State = State.Nighttime

  private var isAppeased: Boolean = true

  private var condition: DeathCondition = DeathCondition.True

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
          condition = DeathCondition.True
          Bukkit.broadcastMessage("Start of day :)")
          ////
        }
      }

      State.Daytime -> {
        if ((time < DAWN_TIME) || (time > DUSK_TIME)) {
          state = State.Nighttime
          if (!isAppeased) {
            Bukkit.broadcastMessage("The gods are angry; no one shall sleep tonight!")
          }
        }
      }

    }

  }

  fun register() {
    this.runTaskTimer(plugin, TICKS_PER_SECOND * 5L, TICKS_PER_SECOND * 5L)
  }

  @EventHandler(priority=EventPriority.HIGH)
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }

    if ((state == State.Daytime) && (!isAppeased)) {
      val cause = CauseOfDeath.identify(event)
      if (condition.test(cause)) {
        Bukkit.broadcastMessage("${event.entity.displayName} has appeased the gods! You may sleep tonight.")
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
          event.player.sendMessage("The gods are angry; no one shall sleep tonight!")
        }
      }
    }

  }

}
