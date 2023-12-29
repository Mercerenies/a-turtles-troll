
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

object MossRevengeFeatureFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    private val mossRevengeAction = MossRevengeAction(
      slimeSize = state.config.getInt("mossrevenge.slime_size"),
    ).asFeature(
      "mossrevenge",
      "Moss blocks will sometimes spawn slimes when broken",
    )

    private val probability: Double = state.config.getDouble("mossrevenge.probability")

    override val features = listOf(mossRevengeAction)

    override val actions = listOf(Weight(mossRevengeAction, probability))

  }

}
