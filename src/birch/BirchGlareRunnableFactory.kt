
package com.mercerenies.turtletroll.birch

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RunnableContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry

import org.bukkit.Bukkit

class BirchGlareRunnableFactory(
  private val deathFeatureId: String,
) : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer {
    var deathRegistry = state.getSharedData(deathFeatureId, CustomDeathMessageRegistry::class)
    if (deathRegistry == null) {
      // Log a warning and use a default value
      Bukkit.getLogger().warning("Could not find death registry, got null")
      deathRegistry = CustomDeathMessageRegistry.Unit
    }
    val runnable = BirchGlareRunnable(
      plugin = state.plugin,
      deathRegistry = deathRegistry,
    )
    return RunnableContainer(runnable)
  }

}
