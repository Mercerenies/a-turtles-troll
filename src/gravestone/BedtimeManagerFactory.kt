
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.gravestone.condition.BedtimeConditionSelector
import com.mercerenies.turtletroll.gravestone.condition.DifficultyTierConditionSelector
import com.mercerenies.turtletroll.gravestone.condition.DifficultyClass
import com.mercerenies.turtletroll.gravestone.condition.WeightedDifficultyConditionSelector
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.container.FeatureContainer

class BedtimeManagerFactory(
  private val conditionSelectorFactory: () -> BedtimeConditionSelector,
) : FeatureContainerFactory<FeatureContainer> {

  companion object {
    val GODS_FEATURE_KEY = "com.mercerenies.turtletroll.gravestone.BedtimeManagerFactory.GODS_FEATURE_KEY"

    fun basicDifficultySelector(): BedtimeConditionSelector =
      WeightedDifficultyConditionSelector(
        listOf(
          Weight(DifficultyClass.EASY, 5.0),
          Weight(DifficultyClass.MEDIUM, 3.0),
          Weight(DifficultyClass.HARD, 1.0),
        )
      )

    fun tieredDifficultySelector(): BedtimeConditionSelector =
      DifficultyTierConditionSelector(
        listOf(
          DifficultyClass.EASY,
          DifficultyClass.EASY,
          DifficultyClass.MEDIUM,
          DifficultyClass.MEDIUM,
          DifficultyClass.HARD,
        )
      )

  }

  private class Container(
    private val manager: BedtimeManager,
  ) : AbstractFeatureContainer() {

    override val listeners =
      listOf(manager)

    override val features =
      listOf(manager)

    override val runnables =
      listOf(manager)

    override val commands =
      listOf(manager.command)

  }

  override fun create(state: BuilderState): FeatureContainer {
    val conditionSelector = conditionSelectorFactory()
    val manager = BedtimeManager(state.plugin, conditionSelector)
    state.registerSharedData(GODS_FEATURE_KEY, manager)
    return Container(manager)
  }

}
