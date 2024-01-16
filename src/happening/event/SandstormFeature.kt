
package com.mercerenies.turtletroll.happening.event

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.happening.RandomEvent
import com.mercerenies.turtletroll.happening.withCooldown
import com.mercerenies.turtletroll.happening.withTitle
import com.mercerenies.turtletroll.happening.boundToFeature
import com.mercerenies.turtletroll.happening.onlyIfPlayersOnline
import com.mercerenies.turtletroll.falling.SandAttackBlockDropper

import org.bukkit.plugin.Plugin
import org.bukkit.event.Listener

import net.kyori.adventure.text.Component

class SandstormFeature(
  private val plugin: Plugin,
  private val ticksBetweenDrops: Int,
  private val dropCount: Int,
  private val maxDropHeight: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer {
      val feature = SandstormFeature(
        plugin = state.plugin,
        ticksBetweenDrops = state.config.getInt("sandstorm.ticks_between_drops"),
        dropCount = state.config.getInt("sandstorm.drop_count"),
        maxDropHeight = state.config.getInt("sandattack.max_drop_height"),
      )
      return FeatureBuilder()
        .addFeature(feature)
        .addRandomEvent(feature.randomEvent)
        .build()
    }

  }

  private inner class SandstormEvent() : BlockRainEvent(plugin) {
    override val name = this@SandstormFeature.name
    override val baseWeight = 1.0
    override val deltaWeight = 0.1
    override val ticksBetweenDrops = this@SandstormFeature.ticksBetweenDrops.toLong()
    override val dropCount = this@SandstormFeature.dropCount

    // Note: Don't do red sand for SandstormEvent; it makes the effect
    // look nice and uniform this way :)
    override val blockDropper = SandAttackBlockDropper(
      maxDropHeight = maxDropHeight,
      redSandChance = 0.0,
    )

    override val messages = listOf(
      Component.text("Mr. Sandman, bring me a dream"),
      Component.text("Make him the cutest that I've ever seen"),
    )
  }

  override val name: String = "sandstorm"

  override val description: String = "Sand will occasionally rain down on all players"

  val randomEvent: RandomEvent =
    SandstormEvent()
      .withTitle("Sandstorm!")
      .withCooldown(8)
      .onlyIfPlayersOnline()
      .boundToFeature(this)

}
