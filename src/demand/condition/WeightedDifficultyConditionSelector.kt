
package com.mercerenies.turtletroll.demand.condition

import com.mercerenies.turtletroll.demand.DeathCondition
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.ext.*

class WeightedDifficultyConditionSelector(
  val difficultyClasses: List<Weight<DifficultyClass>>,
) : ConditionSelector {

  init {
    if (difficultyClasses.isEmpty()) {
      throw IllegalArgumentException("WeightedDifficultyConditionSelector received empty weights list")
    }
    for (weight in difficultyClasses) {
      if (weight.value.conditions.isEmpty()) {
        throw IllegalArgumentException("WeightedDifficultyConditionSelector received empty difficulty class list")
      }
    }
  }

  override fun chooseCondition(): DeathCondition =
    sample(difficultyClasses).conditions.sample()!!

}
