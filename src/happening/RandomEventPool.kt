
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

  val eventNames: List<String>
    get() = entries.map { it.event.name }

  fun chooseAndFireEvent(state: RandomEventState): RandomEvent {
    val options = entries.filter { it.event.canFire(state) }.map { it.toWeight() }
    val chosenEntry = sample(options)
    fireEvent(chosenEntry, state)
    return chosenEntry.event
  }

  fun fireEventByName(name: String, state: RandomEventState): RandomEvent {
    val chosenEntry = entries.find { it.event.name == name }
    if (chosenEntry == null) {
      throw NoSuchElementException("No event named $name")
    }
    fireEvent(chosenEntry, state)
    return chosenEntry.event
  }

  private fun fireEvent(eventEntry: RandomEventEntry, state: RandomEventState) {
    // Update the weight of all events.
    for (entry in entries) {
      entry.updateWeight()
    }
    // Fire the chosen event and reset its weight.
    eventEntry.fireAndReset(state)
  }

}
