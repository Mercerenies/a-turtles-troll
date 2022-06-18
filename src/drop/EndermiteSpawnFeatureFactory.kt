
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

object EndermiteSpawnFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    private val endermiteSpawnAction = EndermiteSpawnAction.asFeature(
      "endermites",
      "End stones will always spawn endermites when broken",
    )

    override val features = listOf(endermiteSpawnAction)

    override val preRules = listOf(endermiteSpawnAction)

  }

}
