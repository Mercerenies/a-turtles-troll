
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.entity.Player
import org.bukkit.entity.Silverfish
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Color
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.Sound
import org.bukkit.util.EulerAngle

import kotlin.collections.HashMap
import kotlin.random.Random

class SilverfishBurnRunnable(
  plugin: Plugin,
) : RunnableFeature(plugin) {

  override val name = "silverfishburn"

  override val description = "Silverfish burn in daylight"

  override val taskPeriod = 8L

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val overworld = Worlds.getOverworld()
    if (overworld != null) {
      overworld.getEntitiesByClass(Silverfish::class.java).forEach { silverfish ->
        if (silverfish.location.block.getLightFromSky() >= 15) {
          val systemTime = Worlds.getSystemTime()
          if ((systemTime > 0) && (systemTime < 12000)) {
            silverfish.setFireTicks(Constants.TICKS_PER_SECOND * 5)
          }
        }
      }
    }
  }

}
