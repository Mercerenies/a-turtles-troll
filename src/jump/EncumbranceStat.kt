
package com.mercerenies.turtletroll.jump

import org.bukkit.entity.Player

fun interface EncumbranceStat {

  // This function shall return a number from 0 to 1, indicating the
  // percentage chance that a jump shall fail.
  fun calculateEncumbrance(player: Player): EncumbranceContribution

}
