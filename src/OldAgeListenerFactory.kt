
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Bukkit

class OldAgeListenerFactory(
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
      OldAgeListener(
        deathRegistry = deathRegistry,
        ageLimit = state.config.getInt(OldAgeListener.AGE_LIMIT_CONFIG_PATH),
      )
    )
  }

}
