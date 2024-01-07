
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
import com.mercerenies.turtletroll.falling.BlockDropper

import org.bukkit.plugin.Plugin
import org.bukkit.Material
import org.bukkit.event.Listener

import net.kyori.adventure.text.Component

class HeavyRainFeature(
  private val plugin: Plugin,
  private val ticksBetweenDrops: Int,
  private val dropCount: Int,
  private val minDropHeight: Int,
  private val maxDropHeight: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer {
      val feature = HeavyRainFeature(
        plugin = state.plugin,
        ticksBetweenDrops = state.config.getInt("heavyrain.ticks_between_drops"),
        dropCount = state.config.getInt("heavyrain.drop_count"),
        minDropHeight = state.config.getInt("anvil.min_drop_height"),
        maxDropHeight = state.config.getInt("anvil.max_drop_height"),
      )
      return FeatureBuilder()
        .addFeature(feature)
        .addRandomEvent(feature.randomEvent)
        .build()
    }

  }

  private inner class HeavyRainEvent() : BlockRainEvent(plugin) {
    override val name = this@HeavyRainFeature.name
    override val baseWeight = 0.5
    override val deltaWeight = 0.25
    override val ticksBetweenDrops = this@HeavyRainFeature.ticksBetweenDrops.toLong()
    override val dropCount = this@HeavyRainFeature.dropCount

    override val blockDropper = object : BlockDropper() {
      override val minDropHeight = this@HeavyRainFeature.minDropHeight
      override val maxDropHeight = this@HeavyRainFeature.maxDropHeight
      override fun getBlockToDrop() = Material.ANVIL
    }

    override val messages = listOf(
      Component.text("Ever eaten an anvil?"),
      Component.text("They're anvilicious!"),
    )
  }

  override val name: String = "heavyrain"

  override val description: String = "Anvils will occasionally rain on all players"

  val randomEvent: RandomEvent =
    HeavyRainEvent()
      .withTitle("Heavy Rain!")
      .withCooldown(20)
      .boundToFeature(this)

}
