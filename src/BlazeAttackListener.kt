
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.entity.Blaze
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import kotlin.random.Random
import kotlin.collections.HashSet

class BlazeAttackListener(val plugin: Plugin) : Listener {
  val memory = HashSet<Blaze>()

  companion object {
    val TICKS_PER_SECOND = 20
    val COOLDOWN_TIME = TICKS_PER_SECOND * 10
  }

  private inner class BlazeCooldown(val blaze: Blaze) : BukkitRunnable() {
    override fun run() {
      memory.remove(blaze)
    }
  }

  @EventHandler
  fun onProjectileLaunch(event: ProjectileLaunchEvent) {
    val projectile = event.entity
    val source = projectile.shooter
    if (source is Blaze) {
      if (!memory.contains(source)) {
        event.setCancelled(true)
        projectile.location.world!!.spawnEntity(projectile.location, EntityType.EVOKER)
        memory.add(source)
        BlazeCooldown(source).runTaskLater(plugin, COOLDOWN_TIME.toLong())
      }
    }
  }

}
