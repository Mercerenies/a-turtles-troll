
package com.mercerenies.turtletroll.rain

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry

import org.bukkit.Bukkit

class RainwaterManagerFactory(
  private val deathFeatureId: String,
) : FeatureContainerFactory<FeatureContainer> {

  private class Container(
    private val manager: RainwaterManager,
  ) : AbstractFeatureContainer() {

    override val listeners =
      listOf(manager)

    override val features =
      listOf(manager)

    override val runnables =
      listOf(manager)

    override val packetListeners =
      listOf(manager.oxygenMeterPacketListener)

  }

  override fun create(state: BuilderState): FeatureContainer {
    var deathRegistry = state.getSharedData(deathFeatureId, CustomDeathMessageRegistry::class)
    if (deathRegistry == null) {
      // Log a warning and use a default value
      Bukkit.getLogger().warning("Could not find death registry, got null")
      deathRegistry = CustomDeathMessageRegistry.Unit
    }
    val manager = RainwaterManager(state.plugin, deathRegistry)
    return Container(manager)
  }

}
