
package com.mercerenies.turtletroll.gravestone

import net.kyori.adventure.text.Component

interface Inscriptions {
  // Receives a number from 0 to 3 inclusive
  fun getLine(index: Int): Component
}
