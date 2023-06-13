
package com.mercerenies.turtletroll.demand.condition

import com.mercerenies.turtletroll.demand.DeathCondition

interface ConditionSelector {

  fun chooseCondition(): DeathCondition

  fun onGodsAppeased() {}

  fun onGodsAngered() {}

}
