
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.BuilderState

object AllFeatureFactories {

  val allFactories: List<FeatureContainerFactory<FeatureContainer>> =
    listOf(
      PetPhantomManager, ContagiousMossManager, DragonBombManager, PufferfishRainManager,
      LlamaHunterManager, TemperatureManager, WitchSummonManager,
    )

  fun createComposite(builderState: BuilderState): FeatureContainer =
    FeatureContainerFactory.createComposite(this.allFactories, builderState)

}
