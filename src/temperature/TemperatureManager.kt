
package com.mercerenies.turtletroll.temperature

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.ObjectiveContainer
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Vanilla
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.util.linearRescale
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions

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

    val COLD_CONDITIONS = listOf(
      WearingBasicOutfitCondition,
      HoldingItemSafetyCondition("a warm object", HOT_ITEMS),
    )

    val HOT_CONDITIONS = listOf(
      DressingLightCondition,
      HoldingItemSafetyCondition("a cool object", COLD_ITEMS),
      UmbrellaHatCondition,
      PrecipitatingSafetyCondition,
    )

  }

  private class TemperatureObjectiveContainer(
    private val calculator: TemperatureCalculator,
  ) : ObjectiveContainer(SCOREBOARD_NAME, "Temperature") {

    fun update() {
      for (player in Bukkit.getOnlinePlayers()) {
        val scaledTemp = getScaledTemperature(player)
        this.objective.getScore(player.name).score = scaledTemp.toInt()
      }
    }

    private fun getScaledTemperature(player: Player): Double {
      val temp = TemperatureCalculator.getTemperature(player)
      // We rescale the cold and hot limits to -10 and 10 here, just
      // so we have some point of reference for the scale.
      var scaledTemp = linearRescale(calculator.minSafeTemperature, calculator.maxSafeTemperature, -10.0, 10.0, temp)
      if (calculator.isPlayerSafeFromCold(player)) {
        scaledTemp = max(scaledTemp, -9.0)
      }
      if (calculator.isPlayerSafeFromHot(player)) {
        scaledTemp = min(scaledTemp, 9.0)
      }
      return scaledTemp
    }

  }

  private var container: TemperatureObjectiveContainer? = null

  private val calculator: TemperatureCalculator =
    TemperatureCalculator(
      coldConditions = COLD_CONDITIONS,
      hotConditions = HOT_CONDITIONS,
    )

  val temperatureCommand = TemperatureCommand(this, calculator).withPermission(Permissions.TEMPERATURE)

  override val name: String = "temperature"

  override val description: String = "Unclothed players freeze in cold biomes; clothed players burn in hot biomes"

  override val taskPeriod = Constants.TICKS_PER_SECOND * 4L

  private fun loadScoreboard() {
    val container = TemperatureObjectiveContainer(calculator)
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
      when (calculator.evaluatePlayerTemperature(player)) {
        TemperatureCalculator.Result.SAFE -> {
          // No action.
        }
        TemperatureCalculator.Result.TOO_COLD -> {
          player.freezeTicks = player.maxFreezeTicks
          deathRegistry.withCustomDeathMessage(getCustomDeathMessage(player)) {
            player.damage(4.0, null)
          }
        }
        TemperatureCalculator.Result.TOO_HOT -> {
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
