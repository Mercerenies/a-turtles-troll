
package com.mercerenies.turtletroll.demand

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.demand.condition.ConditionSelector
import com.mercerenies.turtletroll.demand.condition.DifficultyTierConditionSelector
import com.mercerenies.turtletroll.demand.condition.DifficultyClass
import com.mercerenies.turtletroll.demand.condition.WeightedDifficultyConditionSelector
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.container.FeatureContainer

class DailyDemandManagerFactory(
  private val conditionSelectorFactory: () -> ConditionSelector,
) : FeatureContainerFactory<FeatureContainer> {

  companion object {
    val GODS_FEATURE_KEY = "com.mercerenies.turtletroll.demand.DailyDemandManagerFactory.GODS_FEATURE_KEY"

    fun basicDifficultySelector(): ConditionSelector =
      WeightedDifficultyConditionSelector(
        listOf(
          Weight(DifficultyClass.EASY, 5.0),
          Weight(DifficultyClass.MEDIUM, 3.0),
          Weight(DifficultyClass.HARD, 1.0),
        )
      )

    fun tieredDifficultySelector(): ConditionSelector =
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
    private val manager: DailyDemandManager,
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
    val manager = DailyDemandManager(state.plugin, conditionSelector)
    state.registerSharedData(GODS_FEATURE_KEY, manager)
    return Container(manager)
  }

}
