
package com.mercerenies.turtletroll.gravestone.condition

import com.mercerenies.turtletroll.gravestone.DeathCondition
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample

class WeightedListConditionSelector(
  val conditionList: List<Weight<DeathCondition>>,
) : BedtimeConditionSelector {

  init {
    if (conditionList.isEmpty()) {
      throw IllegalArgumentException("WeightedListConditionSelector received empty weights list")
    }
  }

  override fun chooseCondition(): DeathCondition =
    sample(conditionList)

}
