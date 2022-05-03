
package com.mercerenies.turtletroll.cake

import org.bukkit.Location
import org.bukkit.entity.Player

interface CakeEffect {

  // Positivity should be between -1 and 1 (inclusive) and indicates
  // how good/bad the effect is.
  val positivity: Double

  fun cancelsDefault(): Boolean

  fun onEat(loc: Location, player: Player)

}
