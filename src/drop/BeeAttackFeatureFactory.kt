
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.drop.nearby.BeeAttackAction

object BeeAttackFeatureFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    private val radius = state.config.getInt("bees.radius")
    private val probability = state.config.getDouble("bees.probability")
    private val friendlyAxe = state.config.getBoolean("bees.friendly_axe")

    private val beeAttackAction = BeeAttackAction(radius = radius, friendlyAxe = friendlyAxe).asFeature(
      "bees",
      "Breaking wood will sometimes result in a bee attack",
    )

    override val features = listOf(beeAttackAction)

    override val actions = listOf(Weight(beeAttackAction, probability))

  }

}
