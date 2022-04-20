
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.util.linearRescale

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.World

import kotlin.math.max

class TemperatureManager(plugin: Plugin) : RunnableFeature(plugin), Listener {

  companion object {

    val SCOREBOARD_NAME = "com.mercerenies.turtletroll.TemperatureManager.SCOREBOARD_NAME"

    val COLD_TEMPERATURE = 0.2
    val HOT_TEMPERATURE = 0.8

    val DAMAGE_TIME = Constants.TICKS_PER_SECOND * 4L

    fun getArmorCount(player: Player): Int {
      val inv = player.inventory
      var count = 0
      if (inv.boots != null) {
        count += 1
      }
      if (inv.helmet != null) {
        count += 1
      }
      if (inv.chestplate != null) {
        count += 1
      }
      if (inv.leggings != null) {
        count += 1
      }
      return count
    }

  }

  private class TemperatureObjectiveContainer() : ObjectiveContainer(SCOREBOARD_NAME, "Temperature") {

    fun update() {
      for (player in Bukkit.getOnlinePlayers()) {
        val temp = player.location.block.getTemperature()
        // We define COLD_TEMPERATURE to be 0C and HOT_TEMPERATURE to
        // be 100C, just so we have some point of reference for the
        // scale.
        val scaledTemp = linearRescale(COLD_TEMPERATURE, HOT_TEMPERATURE, 0.0, 100.0, temp)
        this.objective.getScore(player.name).score = scaledTemp.toInt()
      }
    }

  }

  private var deathTick: Boolean = false // For getting a custom death message

  private var container: TemperatureObjectiveContainer? = null

  override val name: String = "temperature"

  override val description: String = "Unclothed players freeze in cold biomes; clothed players burn in hot biomes"

  override val taskPeriod = Constants.TICKS_PER_SECOND * 4L

  private fun loadScoreboard() {
    val container = TemperatureObjectiveContainer()
    this.container = container
    container.update()
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }
    this.container?.update()
    for (player in Bukkit.getOnlinePlayers()) {
      if (player.location.world!!.environment != World.Environment.NORMAL) {
        continue
      }
      val temp = player.location.block.getTemperature()
      val armorCount = getArmorCount(player)
      if (temp < COLD_TEMPERATURE) {
        if (armorCount <= 0) {
          player.freezeTicks = player.maxFreezeTicks
          deathTick = true
          try {
            player.damage(4.0, null)
          } finally {
            deathTick = false
          }
        }
      } else if (temp > HOT_TEMPERATURE) {
        if (armorCount > 0) {
          player.fireTicks = max(player.fireTicks, DAMAGE_TIME.toInt())
        }
      }
    }
  }

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }
    if (deathTick) {
      event.setDeathMessage("${event.entity.getDisplayName()} froze to death.")
    }
  }

  @EventHandler
  @Suppress("UNUSED_PARAMETER")
  fun onServerLoad(_event: ServerLoadEvent) {
    loadScoreboard()
  }

}
