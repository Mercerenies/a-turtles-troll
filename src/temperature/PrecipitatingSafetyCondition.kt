
package com.mercerenies.turtletroll.temperature

import com.mercerenies.turtletroll.Weather

import org.bukkit.entity.Player

object PrecipitatingSafetyCondition : BiomeSafetyCondition {

  override fun evaluate(player: Player): BiomeSafetyCondition.Result =
    if (Weather.getCurrentWeatherAt(player.location).isPrecipitating) {
      BiomeSafetyCondition.safe("It is raining")
    } else {
      BiomeSafetyCondition.unsafe("It is not raining")
    }

}
