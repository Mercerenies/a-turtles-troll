
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.happening.RandomEvent
import com.mercerenies.turtletroll.happening.NotifiedRandomEvent
import com.mercerenies.turtletroll.happening.RandomEventState
import com.mercerenies.turtletroll.happening.withCooldown
import com.mercerenies.turtletroll.happening.boundToFeature

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

import net.kyori.adventure.text.Component

class SpatialRendFeature(
  private val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val DELAY_TIME = 300L // ticks

    override fun create(state: BuilderState): FeatureContainer {
      val feature = SpatialRendFeature(state.plugin)
      return object : AbstractFeatureContainer() {
        override val features = listOf(feature)
        override val randomEvents = listOf(feature.randomEvent)
      }
    }

  }

  private inner class SpatialRendEvent() : NotifiedRandomEvent(plugin) {
    override val name = "spatialrend"
    override val baseWeight = 0.5
    override val deltaWeight = 0.3

    override val messages = listOf(Component.text("Prepare to be transported!"))

    override fun canFire(state: RandomEventState): Boolean =
      true

    override fun onAfterDelay(state: RandomEventState) {
      val onlinePlayers: List<Player> = Bukkit.getOnlinePlayers().shuffled()
      val positions = onlinePlayers.map { it.location }
      for (i in onlinePlayers.indices) {
        val player = onlinePlayers[i]
        val destination = positions[(i + 1) % positions.size]
        player.teleport(destination, PlayerTeleportEvent.TeleportCause.PLUGIN)
      }
    }
  }

  override val name: String = "spatialrend"

  override val description: String = "Players will teleport to each other's positions at random"

  val randomEvent: RandomEvent =
    SpatialRendEvent().withCooldown(24).boundToFeature(this)

}
