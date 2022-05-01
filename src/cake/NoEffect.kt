
package com.mercerenies.turtletroll.cake

import org.bukkit.Location
import org.bukkit.entity.Player

object NoEffect : CakeEffect {

  override val positivity: Double = 0.5

  override fun cancelsDefault(): Boolean = false

  override fun onEat(loc: Location, mealType: MealType, player: Player) {
    player.sendMessage("That ${mealType.mealName} tastes pretty good.")
  }

}
