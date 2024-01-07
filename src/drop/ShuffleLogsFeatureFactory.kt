
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.BlockTypes
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

object ShuffleLogsFeatureFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    // TODO ShuffleDropsAction for stairs/slabs made of wood as well
    private val shuffleLogsAction = ShuffleDropsAction(BlockTypes.LOGS, Positivity.NEUTRAL).asFeature("", "")
    private val shufflePlanksAction = ShuffleDropsAction(BlockTypes.PLANKS, Positivity.NEUTRAL).asFeature("", "")

    private val shuffleFeature = CompositeFeature(
      "shufflelogs",
      "Wooden drops are shuffled",
      listOf(shuffleLogsAction, shufflePlanksAction),
    )

    override val features = listOf(shuffleFeature)

    override val postRules = listOf(shuffleLogsAction, shufflePlanksAction)

  }

}
