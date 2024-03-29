
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.drop.nearby.NetherrackBoomAction

object NetherrackBoomFeatureFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    private val netherrackBoomAction = NetherrackBoomAction(
      radius = state.config.getInt("netherrack.radius"),
    ).asFeature(
      "netherrack",
      "Common nether materials cause a cascading effect, breaking nearby blocks when broken",
    )

    override val features = listOf(netherrackBoomAction)

    override val preRules = listOf(netherrackBoomAction)

  }

}
