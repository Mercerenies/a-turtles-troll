
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.Weight

// An entry in the random events pool, together with its (current)
// weight.
class RandomEventEntry(
  val event: RandomEvent,
) {

  private var _currentWeight: Double = event.baseWeight

  val currentWeight: Double
    get() = _currentWeight

  fun resetWeight() {
    _currentWeight = event.baseWeight
  }

  fun fireAndReset(state: RandomEventState) {
    event.fire(state)
    resetWeight()
  }

  fun updateWeight() {
    _currentWeight += event.deltaWeight
  }

  fun toWeight(): Weight<RandomEventEntry> =
    Weight(this, currentWeight)

}
