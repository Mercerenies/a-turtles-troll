
package com.mercerenies.turtletroll.demand.condition

import com.mercerenies.turtletroll.demand.DeathCondition
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.clamp

// This condition selector always starts at the lowest difficulty and
// then ramps up the more times you successfully appease the gods. If
// you ever fail, the difficulty drops instead.
open class DifficultyTierConditionSelector(
  val difficultyClasses: List<DifficultyClass>,
) : ConditionSelector {

  init {
    if (difficultyClasses.isEmpty()) {
      throw IllegalArgumentException("DifficultyTierConditionSelector received empty classes list")
    }
  }

  var currentDifficulty: Int = 0
    set(value) {
      field = clamp(value, 0, difficultyClasses.size - 1)
    }

  open val difficultyChangeOnAppease: Int = 1
  open val difficultyChangeOnAnger: Int = -9999 // Default = go back to the lowest difficulty

  override fun chooseCondition(): DeathCondition {
    return difficultyClasses[currentDifficulty].conditions.sample()!!
  }

  open override fun onGodsAppeased() {
    currentDifficulty += difficultyChangeOnAppease
  }

  open override fun onGodsAngered() {
    currentDifficulty += difficultyChangeOnAnger
  }

}
