
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
import com.mercerenies.turtletroll.happening.boundToFeature
import com.mercerenies.turtletroll.util.runTaskTimer
import com.mercerenies.turtletroll.falling.BlockDropper

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitTask

import net.kyori.adventure.text.Component

import java.util.function.Consumer

class HeavyRainFeature(
  private val plugin: Plugin,
  private val ticksBetweenAnvils: Int,
  private val anvilCount: Int,
  private val minDropHeight: Int,
  private val maxDropHeight: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer {
      val feature = HeavyRainFeature(
        plugin = state.plugin,
        ticksBetweenAnvils = state.config.getInt("heavyrain.ticks_between_anvils"),
        anvilCount = state.config.getInt("heavyrain.anvil_count"),
        minDropHeight = state.config.getInt("anvil.min_drop_height"),
        maxDropHeight = state.config.getInt("anvil.max_drop_height"),
      )
      return FeatureBuilder()
        .addFeature(feature)
        .addRandomEvent(feature.randomEvent)
        .build()
    }

  }

  private inner class HeavyRainEvent() : NotifiedRandomEvent(plugin) {
    override val name = "heavyrain"
    override val baseWeight = 0.5
    override val deltaWeight = 0.25

    override val messages = listOf(
      Component.text("Ever eaten an anvil?"),
      Component.text("They're anvilicious!"),
    )

    override fun canFire(state: RandomEventState): Boolean =
      true

    override fun onAfterDelay(state: RandomEventState) {
      Bukkit.getScheduler().runTaskTimer(plugin, AnvilDropConsumer(anvilCount), 1L, ticksBetweenAnvils.toLong())
    }
  }

  private inner class AnvilDropConsumer(
    private var remaining: Int,
  ) : Consumer<BukkitTask> {

    private val blockDropper = object : BlockDropper() {
      override val minDropHeight = this@HeavyRainFeature.minDropHeight
      override val maxDropHeight = this@HeavyRainFeature.maxDropHeight
      override fun getBlockToDrop() = Material.ANVIL
    }

    override fun accept(task: BukkitTask) {
      if (remaining <= 0) {
        task.cancel()
        return
      }
      remaining--
      for (player in Bukkit.getOnlinePlayers()) {
        blockDropper.doDrop(player)
      }
    }

  }

  override val name: String = "heavyrain"

  override val description: String = "Anvils will occasionally rain on all players"

  val randomEvent: RandomEvent =
    HeavyRainEvent()
      .withTitle("Heavy Rain!")
      .withCooldown(20)
      .boundToFeature(this)

}
