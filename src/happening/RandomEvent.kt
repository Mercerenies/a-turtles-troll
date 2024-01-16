
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.feature.HasEnabledStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

interface RandomEvent {

  companion object {
    val DEFAULT_TITLE_COLOR = NamedTextColor.LIGHT_PURPLE
  }

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
  withTitle(
    Component.text(title, RandomEvent.DEFAULT_TITLE_COLOR),
    Component.text(subtitle, RandomEvent.DEFAULT_TITLE_COLOR),
  )

fun RandomEvent.withCooldown(cooldownTime: Int) =
  CooldownRandomEvent(this, cooldownTime)

fun RandomEvent.boundToFeature(feature: HasEnabledStatus) =
  FeatureBoundRandomEvent(this, feature)

fun RandomEvent.onlyIfPlayersOnline(minPlayerCount: Int = 1) =
  PlayerCountBoundRandomEvent(this, minPlayerCount)
