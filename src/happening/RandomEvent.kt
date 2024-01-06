
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.feature.HasEnabledStatus

import net.kyori.adventure.text.Component

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

fun RandomEvent.withTitle(title: Component, subtitle: Component = Component.text("")) =
  TitledRandomEvent(this, title, subtitle)

fun RandomEvent.withTitle(title: String, subtitle: String = "") =
  withTitle(Component.text(title), Component.text(subtitle))

fun RandomEvent.withCooldown(cooldownTime: Int) =
  CooldownRandomEvent(this, cooldownTime)

fun RandomEvent.boundToFeature(feature: HasEnabledStatus) =
  FeatureBoundRandomEvent(this, feature)
