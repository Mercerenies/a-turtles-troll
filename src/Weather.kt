
package com.mercerenies.turtletroll

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block

enum class Weather {
  CLEAR, RAIN, SNOW;

  companion object {

    fun weatherTypeAtTemperature(temperature: Double): Weather =
      if (temperature < 0.15) {
        SNOW
      } else if (temperature < 0.95) {
        RAIN
      } else {
        CLEAR
      }

    fun weatherTypeAt(block: Block): Weather =
      weatherTypeAtTemperature(block.getTemperature())

    // This returns the weather that *can* occur at the position based
    // on the biome and temperature. If you're looking for the
    // *current* weather, call getCurrentWeatherAt.
    fun weatherTypeAt(location: Location): Weather =
      weatherTypeAt(location.block)

    fun isThundering(world: World): Boolean =
      world.isThundering()

    fun getCurrentWeatherAt(location: Location): Weather {
      val world = location.world!!
      if (world.isClearWeather()) {
        return CLEAR
      } else {
        return weatherTypeAt(location)
      }
    }

  }

  val isClear: Boolean
    get() = (this == CLEAR)

  val isPrecipitating: Boolean
    get() = (this != CLEAR)

}
