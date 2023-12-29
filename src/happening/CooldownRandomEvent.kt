
package com.mercerenies.turtletroll.happening

class CooldownRandomEvent(
  val event: RandomEvent,
  val cooldownTime: Int,
) : RandomEvent by event {

  private var lastTimeFired: Int? = 0

  private fun isWithinCooldownPeriod(currentTurn: Int): Boolean {
    val lastFire = lastTimeFired
    return (lastFire != null) && (lastFire + cooldownTime <= currentTurn)
  }

  override fun canFire(state: RandomEventState): Boolean =
    event.canFire(state) && !isWithinCooldownPeriod(state.currentTurn)

  override fun fire(state: RandomEventState) {
    event.fire(state)
    lastTimeFired = state.currentTurn
  }

}
