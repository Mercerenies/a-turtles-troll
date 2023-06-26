
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.drop.nearby.SilverfishAttackAction
import com.mercerenies.turtletroll.drop.nearby.SilverfishAttackBomb

object SilverfishAttackFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    private val bomb = SilverfishAttackBomb(
      probability = state.config.getDouble("silverfish.bomb_probability"),
      radius = state.config.getInt("silverfish.bomb_radius"),
      releaseChance = state.config.getDouble("silverfish.release_chance"),
      infestationChance = state.config.getDouble("silverfish.infestation_chance"),
    )

    private val silverfishAttackAction = SilverfishAttackAction(bomb).asFeature(
      "silverfish",
      "Breaking stone blocks will sometimes result in a silverfish attack",
    )

    override val features = listOf(silverfishAttackAction)

    override val actions = listOf(Weight(silverfishAttackAction, state.config.getDouble("silverfish.probability")))

  }

}
