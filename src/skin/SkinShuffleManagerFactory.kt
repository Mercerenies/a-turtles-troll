
package com.mercerenies.turtletroll.skin

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

object SkinShuffleManagerFactory : FeatureContainerFactory<FeatureContainer> {

  private class Container(
    private val manager: SkinShuffleManager,
  ) : AbstractFeatureContainer() {

    override val features =
      listOf(manager)

    override val packetListeners =
      listOf(manager.playerInfoPacketListener)

  }

  override fun create(state: BuilderState): FeatureContainer {
    val manager = SkinShuffleManager(state.plugin)
    return Container(manager)
  }

}
