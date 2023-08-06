
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

object BanishmentManagerFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer =
    Container(BanishmentManager(state.plugin))

  private class Container(
    private val manager: BanishmentManager,
  ) : AbstractFeatureContainer() {

    override val features = listOf(manager)

    override val listeners = listOf(manager)

    override val commands = manager.commands

  }

}
