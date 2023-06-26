
package com.mercerenies.turtletroll.overgrowth

import com.mercerenies.turtletroll.config.CheckedConfigOptions
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material

abstract class OvergrowthListenerFactory() : FeatureContainerFactory<FeatureContainer> {

  class Default() : OvergrowthListenerFactory() {

    override fun overgrowthBlockSupplier(config: CheckedConfigOptions): () -> Material =
      if (config.getBoolean("overgrowth.obsidian")) {
        OvergrowthListener.Companion::alwaysObsidian
      } else {
        OvergrowthListener.Companion::randomWood
      }

  }

  abstract fun overgrowthBlockSupplier(config: CheckedConfigOptions): () -> Material

  override fun create(state: BuilderState): FeatureContainer {
    val overgrowthBlock = overgrowthBlockSupplier(state.config)
    return ListenerContainer(
      OvergrowthListener(
        plugin = state.plugin,
        overgrowthBlock = overgrowthBlock,
        delay = state.config.getInt("overgrowth.delay_seconds").toLong(),
      )
    )
  }

}
