
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.vehicle.VehicleMoveEvent
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.entity.Blaze
import org.bukkit.entity.EntityType
import org.bukkit.entity.Boat
import org.bukkit.plugin.Plugin
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.GameRule
import org.bukkit.Bukkit

import kotlin.math.max
import kotlin.math.abs

class BoatBombListener(val plugin: Plugin) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val RECENT_VELOCITY_META = "com.mercerenies.turtletroll.BoatBombListener.RECENT_VELOCITY_META"

    val EPSILON = 0.001

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(BoatBombListener(state.plugin))

  }

  override val name = "boatbomb"

  override val description = "If a boat hits the ground with sufficient velocity, it explodes"

  @EventHandler
  fun onVehicleMove(event: VehicleMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val vehicle = event.vehicle
    if (vehicle is Boat) {

      // So, apparently, if a vehicle has a rider, then the vehicle
      // has 0 velocity and only the *rider* has velocity, which is
      // weird but okay. So we take the maximum of the vehicle's
      // velocity and that of all of its riders.
      val riders = vehicle.getPassengers()
      val currentVelocity = max(riders.map { abs(it.velocity.y) }.maxOrNull() ?: 0.0, abs(vehicle.velocity.y))

      val latestVelocity = vehicle.getMetadata(RECENT_VELOCITY_META).getOrNull(0)?.asDouble() ?: 0.0
      if ((currentVelocity <= EPSILON) && (latestVelocity >= 1.0)) {
        val world = vehicle.world
        if (world.getGameRuleValue(GameRule.MOB_GRIEFING) ?: true) {
          world.createExplosion(vehicle.location, 5.0F, true, true)
        } else {
          world.createExplosion(vehicle.location, 5.0F, true, false)
        }
      }

      vehicle.setMetadata(RECENT_VELOCITY_META, FixedMetadataValue(plugin, currentVelocity))

    }
  }

}
