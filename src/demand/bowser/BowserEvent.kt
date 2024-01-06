
package com.mercerenies.turtletroll.demand.bowser

import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.demand.DailyDemandEvent
import com.mercerenies.turtletroll.demand.GodsState

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

abstract class BowserEvent : DailyDemandEvent {

  companion object {
    val BOWSER_HEADER = "<Bowser>"
    val GENERIC_DAY_START_MESSAGE = "${BOWSER_HEADER} Gwahahaha! It's me, losers! I've got a job for you all today, and you'd better listen up."
    val GENERIC_FAILURE_MESSAGE = "${BOWSER_HEADER} Nightfall already. You losers couldn't even do a simple task! Maybe next time."
    val BOWSER_DEMAND_TITLE = Component.text("Bowser Time!", NamedTextColor.YELLOW)

    fun bowserMessage(component: Component): Component =
      Component.text(BOWSER_HEADER + " ").append(component)

    fun bowserMessage(text: String): Component =
      bowserMessage(Component.text(text))

  }

  abstract fun getDayStartMessage(): Component

  open fun getFailureMessage(): Component =
    Component.text(GENERIC_FAILURE_MESSAGE)

  open override fun onDayStart(godsState: GodsState) {
    Messages.broadcastTitle(BOWSER_DEMAND_TITLE)
    Messages.broadcastMessage(GENERIC_DAY_START_MESSAGE)
    Messages.broadcastMessage(getDayStartMessage())
  }

  open override fun onDayEnd(godsState: GodsState) {
    // Bowser never punishes you for failing his demand. He only
    // rewards you for succeeding. So if you haven't met it by
    // nightfall, just set the state to "appeased" so no punishments
    // occur.
    if (!godsState.isAppeased()) {
      Messages.broadcastMessage(getFailureMessage())
      godsState.setGodsAppeasedSilently(true)
    }
  }

}
