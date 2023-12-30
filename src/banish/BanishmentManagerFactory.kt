
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

object BanishmentManagerFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer {
    val probability = state.config.getDouble("banishment.probability")
    val manager = BanishmentManager(state.plugin, probability)
    return FeatureBuilder()
      .addFeature(manager)
      .addListener(manager)
      .addDebugCommands(manager.debugCommands)
      .build()
  }

}
