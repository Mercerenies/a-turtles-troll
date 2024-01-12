
package com.mercerenies.turtletroll.temperature

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Weather
import com.mercerenies.turtletroll.ObjectiveContainer
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Vanilla
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.util.linearRescale

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.Material

import net.kyori.adventure.text.Component

import kotlin.math.min
import kotlin.math.max

class TemperatureManager(
  plugin: Plugin,
  private val deathRegistry: CustomDeathMessageRegistry,
) : RunnableFeature(plugin), Listener {

  companion object {

    val SCOREBOARD_NAME = "com.mercerenies.turtletroll.TemperatureManager.SCOREBOARD_NAME"

    val COLD_TEMPERATURE = 0.2
    val HOT_TEMPERATURE = 0.8

    val DAMAGE_TIME = Constants.TICKS_PER_SECOND * 4L

    val COLD_ITEMS = setOf(
      Material.BLUE_ICE, Material.FROSTED_ICE, Material.ICE, Material.PACKED_ICE,
      Material.POWDER_SNOW, Material.POWDER_SNOW_BUCKET, Material.POWDER_SNOW_CAULDRON,
      Material.SNOW, Material.SNOW_BLOCK, Material.SNOWBALL,
    )

    val HOT_ITEMS = setOf(
      Material.CAMPFIRE, Material.FIRE, Material.SOUL_CAMPFIRE, Material.SOUL_FIRE,
      Material.LAVA, Material.LAVA_BUCKET, Material.LAVA_CAULDRON,
    )

    fun getArmorCount(player: Player): Int {
      val inv = player.inventory
      var count = 0
      // Note: Because so many other things interact with the helmet
      // slot, we don't consider the helmet slot at all when counting
      // armor here.
      if (inv.boots != null) {
        count += 1
      }
      if (inv.chestplate != null) {
        count += 1
      }
      if (inv.leggings != null) {
        count += 1
      }
      return count
    }

    fun isPlayerSafeFromHot(player: Player): Boolean =
      COLD_ITEMS.contains(player.inventory.itemInMainHand.type) ||
        COLD_ITEMS.contains(player.inventory.itemInOffHand.type) ||
        Weather.getCurrentWeatherAt(player.location).isPrecipitating

    fun isPlayerSafeFromCold(player: Player): Boolean =
      HOT_ITEMS.contains(player.inventory.itemInMainHand.type) ||
        HOT_ITEMS.contains(player.inventory.itemInOffHand.type)

  }

  private class TemperatureObjectiveContainer() : ObjectiveContainer(SCOREBOARD_NAME, "Temperature") {

    fun update() {
      for (player in Bukkit.getOnlinePlayers()) {
        var temp = TemperatureCalculator.getTemperature(player)
        // We define COLD_TEMPERATURE to be -10 and HOT_TEMPERATURE to
        // be 10, just so we have some point of reference for the
        // scale.
        var scaledTemp = linearRescale(COLD_TEMPERATURE, HOT_TEMPERATURE, -10.0, 10.0, temp)
        if (isPlayerSafeFromCold(player)) {
          scaledTemp = max(scaledTemp, -9.0)
        }
        if (isPlayerSafeFromHot(player)) {
          scaledTemp = min(scaledTemp, 9.0)
        }
        this.objective.getScore(player.name).score = scaledTemp.toInt()
      }
    }

  }

  private var container: TemperatureObjectiveContainer? = null

  override val name: String = "temperature"

  override val description: String = "Unclothed players freeze in cold biomes; clothed players burn in hot biomes"

  override val taskPeriod = Constants.TICKS_PER_SECOND * 4L

  private fun loadScoreboard() {
    val container = TemperatureObjectiveContainer()
    this.container = container
    container.update()
  }

  private fun getCustomDeathMessage(player: Player): CustomDeathMessage =
    CustomDeathMessage(
      Vanilla(EntityDamageEvent.DamageCause.FREEZE),
      Component.text("").append(player.displayName()).append(" froze to death."),
    )

  override fun run() {
    if (!isEnabled()) {
      return
    }
    this.container?.update()
    for (player in Bukkit.getOnlinePlayers()) {
      if (player.location.world!!.environment != World.Environment.NORMAL) {
        continue
      }
      val temp = TemperatureCalculator.getTemperature(player)
      val armorCount = getArmorCount(player)
      if (temp < COLD_TEMPERATURE) {
        if ((armorCount <= 0) && (!isPlayerSafeFromCold(player))) {
          player.freezeTicks = player.maxFreezeTicks
          deathRegistry.withCustomDeathMessage(getCustomDeathMessage(player)) {
            player.damage(4.0, null)
          }
        }
      } else if (temp > HOT_TEMPERATURE) {
        if ((armorCount > 0) && (!isPlayerSafeFromHot(player))) {
          player.fireTicks = max(player.fireTicks, DAMAGE_TIME.toInt())
        }
      }
    }
  }

  @EventHandler
  @Suppress("UNUSED_PARAMETER")
  fun onServerLoad(_event: ServerLoadEvent) {
    loadScoreboard()
  }

}
