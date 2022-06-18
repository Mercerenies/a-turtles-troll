
package com.mercerenies.turtletroll.ghastlava

import com.mercerenies.turtletroll.BlockIgnorer
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Bukkit

class GhastLavaListenerFactory(
  private val blockIgnorerSupplier: (BuilderState) -> BlockIgnorer?,
) : FeatureContainerFactory<FeatureContainer> {

  constructor(blockIgnorer: BlockIgnorer) :
    this({ _ -> blockIgnorer })

  constructor(blockIgnorerId: String) :
    this({ state -> state.getSharedData(blockIgnorerId, BlockIgnorer::class) })

  override fun create(state: BuilderState): FeatureContainer {
    var ignorer = blockIgnorerSupplier(state)
    if (ignorer == null) {
      // Log a warning and use a default value
      Bukkit.getLogger().warning("Could not find block ignorer for GhastLavaListenerFactory, got null")
      ignorer = BlockIgnorer.Null
    }
    return ListenerContainer(GhastLavaListener(state.plugin, ignorer))
  }

}
