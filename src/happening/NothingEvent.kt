
package com.mercerenies.turtletroll.happening

// The event that does nothing. This is the event that trivially fires
// whenever nothing else does.
class NothingEvent(
  override val baseWeight: Double,
) : RandomEvent {
  override val name: String = "nothing"
  override val deltaWeight: Double = 0.0

  override fun canFire(state: RandomEventState) = true

  override fun fire(state: RandomEventState) {
    // Nothing happens :)
  }

}
