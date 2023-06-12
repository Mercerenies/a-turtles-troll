
package com.mercerenies.turtletroll.gravestone

import net.kyori.adventure.text.Component

data class CustomDeathMessage(
  val cause: CauseOfDeath,
  val message: Component,
)
