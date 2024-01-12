
package com.mercerenies.turtletroll.temperature

import com.mercerenies.turtletroll.Hats

import org.bukkit.entity.Player

object UmbrellaHatCondition : BiomeSafetyCondition {

  override fun evaluate(player: Player): BiomeSafetyCondition.Result =
    if (player.inventory.helmet?.let(Hats::getCustomHatName) == Hats.UMBRELLA_HAT_NAME) {
      BiomeSafetyCondition.safe("You are wearing an umbrella hat")
    } else {
      BiomeSafetyCondition.unsafe("You are NOT wearing an umbrella hat")
    }

}
