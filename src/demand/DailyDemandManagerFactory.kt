
package com.mercerenies.turtletroll.demand

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.config.CheckedConfigOptions
import com.mercerenies.turtletroll.demand.event.EventSelector
import com.mercerenies.turtletroll.demand.event.DifficultyTierEventSelector
import com.mercerenies.turtletroll.demand.event.DifficultyClass
import com.mercerenies.turtletroll.demand.event.WeightedDifficultyEventSelector
import com.mercerenies.turtletroll.demand.bowser.BowserInterruptSelector
import com.mercerenies.turtletroll.demand.bowser.BowserEventsLibrary
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.container.FeatureContainer

class DailyDemandManagerFactory(
  private val eventSelectorFactory: (CheckedConfigOptions) -> EventSelector,
) : FeatureContainerFactory<FeatureContainer> {

  companion object {
    val GODS_FEATURE_KEY = "com.mercerenies.turtletroll.demand.DailyDemandManagerFactory.GODS_FEATURE_KEY"

    fun basicDifficultySelector(): EventSelector =
      WeightedDifficultyEventSelector(
        listOf(
          Weight(DifficultyClass.EASY, 5.0),
          Weight(DifficultyClass.MEDIUM, 3.0),
          Weight(DifficultyClass.HARD, 1.0),
        )
      )

    fun tieredDifficultySelector(): EventSelector =
      DifficultyTierEventSelector(
        listOf(
          DifficultyClass.EASY,
          DifficultyClass.EASY,
          DifficultyClass.MEDIUM,
          DifficultyClass.MEDIUM,
          DifficultyClass.HARD,
        )
      )

    fun bowserEventSelector(config: CheckedConfigOptions): EventSelector {
      val bowserChance = config.getDouble("demand.bowser_chance")
      return BowserInterruptSelector(
        regularEventSelector = tieredDifficultySelector(),
        bowserEventSelector = BowserEventsLibrary.DEFAULT.eventSelector,
        bowserEventChance = bowserChance,
      )
    }

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
    val eventSelector = eventSelectorFactory(state.config)
    val manager = DailyDemandManager(state.plugin, eventSelector)
    state.registerSharedData(GODS_FEATURE_KEY, manager)
    return Container(manager)
  }

}
