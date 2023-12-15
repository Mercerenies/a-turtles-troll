
package com.mercerenies.turtletroll.demand.event

import com.mercerenies.turtletroll.demand.DailyDemandEvent
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample

class WeightedListEventSelector(
  val conditionList: List<Weight<() -> DailyDemandEvent>>,
) : EventSelector {

  companion object {

    fun uniform(events: List<() -> DailyDemandEvent>) =
      WeightedListEventSelector(
        events.map { Weight(it, 1.0) },
      )

  }

  init {
    if (conditionList.isEmpty()) {
      throw IllegalArgumentException("WeightedListConditionSelector received empty weights list")
    }
  }

  override fun chooseCondition(): DailyDemandEvent {
    val factory = sample(conditionList)
    return factory()
  }

}
