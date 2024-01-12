
package com.mercerenies.turtletroll.temperature

import org.bukkit.entity.Player
import org.bukkit.Location
import org.bukkit.World

object TemperatureCalculator {

  private fun getTemperatureOverrideForWorld(env: World.Environment): Double? =
    when (env) {
      World.Environment.NETHER -> 0.8
      World.Environment.THE_END -> 0.3
      else -> null
    }

  fun getTemperature(location: Location): Double {
    val temperatureOverride = location.world?.environment?.let(::getTemperatureOverrideForWorld)
    val blockTemperature = location.block.temperature
    return temperatureOverride ?: blockTemperature
  }

  fun getTemperature(player: Player): Double =
    getTemperature(player.location)

}
