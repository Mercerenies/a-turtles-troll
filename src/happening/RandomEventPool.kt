
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.sample

class RandomEventPool(
  events: Iterable<RandomEvent>,
) {

  private val entries: List<RandomEventEntry> =
    events
      .map { RandomEventEntry(it) }
      .toList()

  init {
    if (entries.isEmpty()) {
      throw IllegalArgumentException("No events given")
    }
  }

  fun chooseAndFireEvent(state: RandomEventState) {
    val options = entries.filter { it.event.canFire(state) }.map { it.toWeight() }
    val chosenEntry = sample(options)

    // Update the weight of all events.
    for (entry in entries) {
      entry.updateWeight()
    }

    // Fire the chosen event and reset its weight.
    chosenEntry.fireAndReset(state)
  }

}
