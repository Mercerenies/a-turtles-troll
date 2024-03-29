
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry

import org.bukkit.Bukkit

class RedstoneWorldListenerFactory(
  private val deathFeatureId: String,
) : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer {
    var deathRegistry = state.getSharedData(deathFeatureId, CustomDeathMessageRegistry::class)
    if (deathRegistry == null) {
      // Log a warning and use a default value
      Bukkit.getLogger().warning("Could not find death registry, got null")
      deathRegistry = CustomDeathMessageRegistry.Unit
    }
    return ListenerContainer(
      RedstoneWorldListener(
        deathRegistry = deathRegistry,
        dropChance = state.config.getDouble("redstoneworld.drop_probability"),
        minItems = state.config.getInt("redstoneworld.min_items"),
        maxItems = state.config.getInt("redstoneworld.max_items"),
        minDamage = state.config.getDouble("redstoneworld.min_damage"),
        maxDamage = state.config.getDouble("redstoneworld.max_damage"),
      )
    )
  }

}
