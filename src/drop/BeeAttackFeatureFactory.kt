
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.drop.nearby.BeeAttackAction

object BeeAttackFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    private val beeAttackAction = BeeAttackAction().asFeature(
      "bees",
      "Breaking wood will sometimes result in a bee attack",
    )

    override val features = listOf(beeAttackAction)

    override val actions = listOf(Weight(beeAttackAction, 0.2))

  }

}
