
package com.mercerenies.turtletroll.demand.event

import com.mercerenies.turtletroll.demand.DeathCondition

interface EventSelector {

  fun chooseCondition(): DeathCondition

  fun onGodsAppeased() {}

  fun onGodsAngered() {}

}
