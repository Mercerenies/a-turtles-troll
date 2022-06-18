
package com.mercerenies.turtletroll.overgrowth

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material

class OvergrowthListenerFactory(
  private val overgrowthBlock: () -> Material,
) : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer =
    ListenerContainer(OvergrowthListener(state.plugin, overgrowthBlock))

}
