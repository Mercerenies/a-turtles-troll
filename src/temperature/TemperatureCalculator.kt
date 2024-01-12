
package com.mercerenies.turtletroll.temperature

import org.bukkit.entity.Player
import org.bukkit.Location
import org.bukkit.World

open class TemperatureCalculator(
  val coldConditions: List<BiomeSafetyCondition>,
  val hotConditions: List<BiomeSafetyCondition>,
) {

  companion object {

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

  enum class Result {
    SAFE, TOO_COLD, TOO_HOT,
  }

  // Temperatures strictly below this are too cold to traverse unless
  // you have clothes or other protection.
  open val minSafeTemperature: Double = 0.2

  // Temperatures strictly above this are too hot to traverse unless
  // you are wearing NO armor, or you have other protection.
  open val maxSafeTemperature: Double = 0.8

  fun isPlayerSafeFromCold(player: Player): Boolean =
    coldConditions.any { it.isSafe(player) }

  fun isPlayerSafeFromHot(player: Player): Boolean =
    hotConditions.any { it.isSafe(player) }

  fun evaluatePlayerTemperature(player: Player): Result {
    val temp = getTemperature(player)
    if ((temp < minSafeTemperature) && (!isPlayerSafeFromCold(player))) {
      return Result.TOO_COLD
    } else if ((temp > maxSafeTemperature) && (!isPlayerSafeFromHot(player))) {
      return Result.TOO_HOT
    } else {
      return Result.SAFE
    }
  }

}
