
package com.mercerenies.turtletroll.rain

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Angel
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.CooldownMemory

import org.bukkit.entity.Player
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Color
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent

import kotlin.collections.HashMap

class RainwaterManager(
  plugin: Plugin,
  private val deathRegistry: CustomDeathMessageRegistry,
) : RunnableFeature(plugin), Listener {

  private val oxygenMeters: HashMap<Player, RainOxygenMeter> = HashMap()

  private fun getOxygenMeter(player: Player): RainOxygenMeter =
    oxygenMeters.getOrPut(player) { RainOxygenMeter(player, deathRegistry) }

  override val name: String = "rainwater"

  override val description: String = "Players can drown in the rain"

  override val taskPeriod = Constants.TICKS_PER_SECOND + 1L

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val onlinePlayers = Bukkit.getOnlinePlayers()
    for (player in onlinePlayers) {
      val oxygenMeter = getOxygenMeter(player)
      oxygenMeter.runTick()
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
