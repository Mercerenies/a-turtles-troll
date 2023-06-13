
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

object MossRevengeFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    private val mossRevengeAction = MossRevengeAction.asFeature(
      "mossrevenge",
      "Moss blocks will sometimes spawn slimes when broken",
    )

    override val features = listOf(mossRevengeAction)

    override val actions = listOf(Weight(mossRevengeAction, 0.2))

  }

}
