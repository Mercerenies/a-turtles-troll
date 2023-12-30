
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.feature.HasEnabledStatus

interface RandomEvent {

  // The event's name. Must be globally unique among all events in the
  // pool.
  val name: String

  // The starting weight given to this event.
  val baseWeight: Double

  // The weight added to this event every time it doesn't fire.
  val deltaWeight: Double

  fun canFire(state: RandomEventState): Boolean

  fun fire(state: RandomEventState): Unit

}

fun RandomEvent.withCooldown(cooldownTime: Int) =
  CooldownRandomEvent(this, cooldownTime)

fun RandomEvent.boundToFeature(feature: HasEnabledStatus) =
  FeatureBoundRandomEvent(this, feature)
