
package com.mercerenies.turtletroll.demand.bowser

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.demand.DailyDemandEvent
import com.mercerenies.turtletroll.demand.GodsState

import net.kyori.adventure.text.Component

abstract class BowserEvent : DailyDemandEvent {

  abstract fun getDayStartMessage(): Component

  open override fun onDayStart(godsState: GodsState) {
    Messages.broadcastMessage(getDayStartMessage())
  }

  open override fun onDayEnd(godsState: GodsState) {
    // Bowser never punishes you for failing his demand. He only
    // rewards you for succeeding. So if you haven't met it by
    // nightfall, just set the state to "appeased" so no punishments
    // occur.
    godsState.setGodsAppeased(true)
  }

}
