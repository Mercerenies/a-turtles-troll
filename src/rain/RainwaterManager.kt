
package com.mercerenies.turtletroll.rain

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent

import com.comphenix.protocol.ProtocolLibrary

import kotlin.collections.HashMap
import kotlin.math.min

class RainwaterManager(
  plugin: Plugin,
  private val deathRegistry: CustomDeathMessageRegistry,
) : RunnableFeature(plugin), Listener {

  private val oxygenMeters: HashMap<Player, RainOxygenMeter> = HashMap()

  private fun getOxygenMeter(player: Player): RainOxygenMeter =
    oxygenMeters.getOrPut(player) { RainOxygenMeter(player, deathRegistry) }

  override val name: String = "rainwater"

  override val description: String = "Players can drown in the rain"

  override val taskPeriod = Constants.TICKS_PER_SECOND / 4L - 1L

  private var tick: Int = 0

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val onlinePlayers = Bukkit.getOnlinePlayers()
    tick = (tick + 1) % 4
    for (player in onlinePlayers) {
      val oxygenMeter = getOxygenMeter(player)
      if (tick == 0) {
        // Only do this every four times we run (approximately once
        // per second). But force update Minecraft every time we run.
        oxygenMeter.runTick()
      }
      val airFraction = oxygenMeter.getAirFraction()
      val remainingAir = min((airFraction * player.maximumAir).toInt(), player.remainingAir)
      player.remainingAir = remainingAir
    }
  }

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Player) {
      val oxygenMeter = getOxygenMeter(entity)
      oxygenMeter.fullyReplenish()
    }
  }

}
