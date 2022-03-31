
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.ext.*

import org.bukkit.World
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin
import org.bukkit.block.Block

class GravestoneListener(val plugin: Plugin) : AbstractFeature(), Listener {

  companion object {
    val DELAY_SECONDS = 2L

    fun chooseGravestoneSpawner(): GravestoneSpawner =
      listOf(ClassicGravestoneSpawner, BeaconGravestoneSpawner).sample()!!

  }

  private class SpawnGravestone(val block: Block, val cause: Inscriptions) : BukkitRunnable() {
    override fun run() {
      val rotation = Rotation.NONE
      val spawner = chooseGravestoneSpawner()
      spawner.spawnGravestone(block, cause, rotation)
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
    SpawnGravestone(block, cause).runTaskLater(plugin, Constants.TICKS_PER_SECOND * DELAY_SECONDS)

  }

}
