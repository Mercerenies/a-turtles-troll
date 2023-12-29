
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

object EndermiteSpawnFeatureFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    private val endermiteSpawnAction = EndermiteSpawnAction.asFeature(
      "endermites",
      "End stones will always spawn endermites when broken",
    )

    override val features = listOf(endermiteSpawnAction)

    override val preRules = listOf(endermiteSpawnAction)

  }

}
