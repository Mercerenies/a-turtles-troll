
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

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
    val entity = event.getEntity()
    val source = entity.getShooter()
    if (source is Ghast) {
      DelayedLava(entity.location).runTaskLater(plugin, 1L)
    }
  }

}
