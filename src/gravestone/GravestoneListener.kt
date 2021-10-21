
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.entity.Player
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.EntityType
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin
import org.bukkit.block.Block

class GravestoneListener(val plugin: Plugin) : AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20L
    val DELAY_SECONDS = 2L
  }

  private class SpawnGravestone(val block: Block, val cause: Inscriptions) : BukkitRunnable() {
    override fun run() {
      GravestoneSpawner.spawnGravestone(block, cause)
    }
  }

  override val name = "gravestone"

  override val description = "Gravestones appear when a player dies"

  @EventHandler(priority=EventPriority.HIGH)
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.entity.location.world?.environment == World.Environment.THE_END) {
      // The End is crazy enough already
      return
    }

    val block = event.entity.location.block
    val cause = CauseOfDeath.inscription(event)
    SpawnGravestone(block, cause).runTaskLater(plugin, TICKS_PER_SECOND * DELAY_SECONDS)

  }

}
