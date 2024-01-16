
package com.mercerenies.turtletroll.happening.event

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.happening.RandomEvent
import com.mercerenies.turtletroll.happening.NotifiedRandomEvent
import com.mercerenies.turtletroll.happening.RandomEventState
import com.mercerenies.turtletroll.happening.withCooldown
import com.mercerenies.turtletroll.happening.withTitle
import com.mercerenies.turtletroll.happening.onlyIfPlayersOnline
import com.mercerenies.turtletroll.happening.boundToFeature

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

import net.kyori.adventure.text.Component

class SpatialRendFeature(
  private val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer {
      val feature = SpatialRendFeature(state.plugin)
      return FeatureBuilder()
        .addFeature(feature)
        .addRandomEvent(feature.randomEvent)
        .build()
    }

  }

  private class SpatialRendEvent(plugin: Plugin) : NotifiedRandomEvent(plugin) {
    override val name = "spatialrend"
    override val baseWeight = 0.4
    override val deltaWeight = 0.2

    override val messages = listOf(
      Component.text("In a twist of fate, a whimsical bend"),
      Component.text("Spatial Rend, your place to lend"),
      Component.text("Players swapping, a playful tease"),
      Component.text("Warping around, wherever I please!"),
    )

    override fun canFire(state: RandomEventState): Boolean =
      true

    override fun onAfterDelay(state: RandomEventState) {
      val onlinePlayers: List<Player> = Bukkit.getOnlinePlayers().shuffled()
      val positions = onlinePlayers.map { it.location }
      for (i in onlinePlayers.indices) {
        val player = onlinePlayers[i]
        val destination = positions[(i + 1) % positions.size]
        player.teleport(destination, PlayerTeleportEvent.TeleportCause.PLUGIN)
        player.world.playSound(player.location, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 0.0f)
      }
    }
  }

  override val name: String = "spatialrend"

  override val description: String = "Players will teleport to each other's positions at random"

  val randomEvent: RandomEvent =
    SpatialRendEvent(plugin)
      .withTitle("Spatial Rend!")
      .withCooldown(24)
      .onlyIfPlayersOnline(minPlayerCount = 2)
      .boundToFeature(this)

}
