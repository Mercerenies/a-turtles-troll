
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

  companion object {
    val CONTENTS_FACTORY_OPTIONS: List<MimicContentsFactory> = listOf(
      ConstantContentsFactory(LegacyMimicIdentifier.chestPattern()),
    )
    val CONTENTS_FACTORY = MimicContentsFactory.several(CONTENTS_FACTORY_OPTIONS)
  }

  override fun create(state: BuilderState): FeatureContainer {
    var deathRegistry = state.getSharedData(deathFeatureId, CustomDeathMessageRegistry::class)
    if (deathRegistry == null) {
      // Log a warning and use a default value
      Bukkit.getLogger().warning("Could not find death registry, got null")
      deathRegistry = CustomDeathMessageRegistry.Unit
    }
    val replaceChance = state.config.getDouble("mimics.probability")
    val mimicListener = MimicListener(state.plugin, deathRegistry, replaceChance, CONTENTS_FACTORY)
    return ListenerContainer(mimicListener).withPlayerDebugCommand("mimic") { player ->
      MimicIdentifier(state.plugin).spawnMimic(player.location.block)
      true
    }
  }

}
