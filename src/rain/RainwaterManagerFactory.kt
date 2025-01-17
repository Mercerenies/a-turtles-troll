
package com.mercerenies.turtletroll.rain

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry

import org.bukkit.Bukkit

class RainwaterManagerFactory(
  private val deathFeatureId: String,
) : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer {
    var deathRegistry = state.getSharedData(deathFeatureId, CustomDeathMessageRegistry::class)
    if (deathRegistry == null) {
      // Log a warning and use a default value
      Bukkit.getLogger().warning("Could not find death registry, got null")
      deathRegistry = CustomDeathMessageRegistry.Unit
    }
    val manager = RainwaterManager(state.plugin, deathRegistry)
    return FeatureBuilder()
      .addListener(manager)
      .addFeature(manager)
      .addGameModification(manager)
      .build()
  }

}
