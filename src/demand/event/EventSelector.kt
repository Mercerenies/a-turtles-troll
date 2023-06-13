
package com.mercerenies.turtletroll.demand.event

import com.mercerenies.turtletroll.demand.DailyDemandEvent

interface EventSelector {

  fun chooseCondition(): DailyDemandEvent

  fun onGodsAppeased() {}

  fun onGodsAngered() {}

}
