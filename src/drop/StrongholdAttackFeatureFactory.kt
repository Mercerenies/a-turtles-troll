
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.drop.nearby.SilverfishAttackBomb
import com.mercerenies.turtletroll.drop.nearby.StrongholdSilverfishAttackAction

object StrongholdAttackFeatureFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    private val bomb = SilverfishAttackBomb(
      probability = state.config.getDouble("stronghold.bomb_probability"),
      radius = state.config.getInt("stronghold.bomb_radius"),
      releaseChance = state.config.getDouble("stronghold.release_chance"),
      infestationChance = state.config.getDouble("stronghold.infestation_chance"),
    )

    private val strongholdAttackAction = StrongholdSilverfishAttackAction(bomb).asFeature(
      "stronghold",
      "Breaking stone bricks will always result in a silverfish attack",
    )

    override val features = listOf(strongholdAttackAction)

    override val preRules = listOf(strongholdAttackAction)

  }

}
