
package com.mercerenies.turtletroll.demand.event

import com.mercerenies.turtletroll.demand.DeathCondition
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample

class WeightedListEventSelector(
  val conditionList: List<Weight<DeathCondition>>,
) : EventSelector {

  init {
    if (conditionList.isEmpty()) {
      throw IllegalArgumentException("WeightedListConditionSelector received empty weights list")
    }
  }

  override fun chooseCondition(): DeathCondition =
    sample(conditionList)

}
