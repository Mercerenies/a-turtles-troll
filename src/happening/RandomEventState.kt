
package com.mercerenies.turtletroll.happening

// The state about the events engine, passed to RandomEvent.canFire and RandomEvent.fire
interface RandomEventState {

  val currentTurn: Int

}
