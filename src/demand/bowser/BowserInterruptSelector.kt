
package com.mercerenies.turtletroll.demand.bowser

import com.mercerenies.turtletroll.demand.DailyDemandEvent
import com.mercerenies.turtletroll.demand.event.EventSelector
import com.mercerenies.turtletroll.util.component.*

import kotlin.random.Random

class BowserInterruptSelector(
  val regularEventSelector: EventSelector,
  val bowserEventSelector: EventSelector,
  val bowserEventChance: Double,
) : EventSelector {

  private var lastSelectionWasBowser: Boolean = false

  private val currentEventSelector: EventSelector
    get() =
      if (lastSelectionWasBowser) {
        bowserEventSelector
      } else {
        regularEventSelector
      }

  override fun chooseCondition(): DailyDemandEvent {
    if (Random.nextDouble() < bowserEventChance) {
      lastSelectionWasBowser = true
      return bowserEventSelector.chooseCondition()
    } else {
      lastSelectionWasBowser = false
      return regularEventSelector.chooseCondition()
    }
  }

  override fun onGodsAppeased() {
    currentEventSelector.onGodsAppeased()
  }

  override fun onGodsAngered() {
    currentEventSelector.onGodsAngered()
  }

}
