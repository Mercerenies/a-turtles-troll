
package com.mercerenies.turtletroll.ghastlava

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.BlockIgnorer

import org.bukkit.World
import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.entity.Ghast
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable


class GhastLavaListener(
  val plugin: Plugin,
  val ignorer: BlockIgnorer = BlockIgnorer.Null,
) : AbstractFeature(), Listener {

  companion object {

    private fun findGround(initialLocation: Location): Location? {
      var loc = initialLocation
      var i = 0 // Just to prevent infinite loops
      while ((loc.block.type == Material.AIR) && (i < 10)) {
        i += 1
        loc = loc.clone().add(0.0, -1.0, 0.0)
      }
      if (loc.block.type == Material.AIR) {
        return null
      } else {
        return loc
      }
    }

  }

  override val name = "ghastlava"

  override val description = "Ghast fireballs spawn lava when they hit"

  private inner class DelayedLava(val location: Location) : BukkitRunnable() {
    override fun run() {
      location.block.type = Material.LAVA
      ignorer.ignore(location.block)
    }
  }

  @EventHandler
  fun onProjectileHit(event: ProjectileHitEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.entity.location.world?.environment == World.Environment.THE_END) {
      // The End is crazy enough already
      return
    }
    val entity = event.getEntity()
    val source = entity.getShooter()
    if (source is Ghast) {
      // Find the ground
      val loc = findGround(entity.location)
      if (loc != null) {
        DelayedLava(loc).runTaskLater(plugin, 1L)
      }
    }
  }

}
