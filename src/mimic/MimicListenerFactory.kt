
package com.mercerenies.turtletroll.mimic

import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.feature.container.withPlayerDebugCommand
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Bukkit

class MimicListenerFactory(
  private val deathFeatureId: String,
) : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer {
    var deathRegistry = state.getSharedData(deathFeatureId, CustomDeathMessageRegistry::class)
    if (deathRegistry == null) {
      // Log a warning and use a default value
      Bukkit.getLogger().warning("Could not find death registry, got null")
      deathRegistry = CustomDeathMessageRegistry.Unit
    }
    val replaceChance = state.config.getDouble("mimics.probability")
    val mimicListener = MimicListener(state.plugin, deathRegistry, replaceChance)
    return ListenerContainer(mimicListener).withPlayerDebugCommand("mimic") { player ->
      MimicIdentifier.spawnMimic(player.location.block)
      true
    }
  }

}
