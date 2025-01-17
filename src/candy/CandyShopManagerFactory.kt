
package com.mercerenies.turtletroll.candy

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions

object CandyShopManagerFactory : FeatureContainerFactory<FeatureContainer> {
  override fun create(state: BuilderState): FeatureContainer {
    val manager = CandyShopManager(state.plugin)
    return FeatureBuilder()
      .addFeature(manager)
      .addListener(manager)
      .addGameModification(manager)
      .addCommand("coin" to manager.queryCommand.withPermission(Permissions.COIN))
      .build()
  }
}
