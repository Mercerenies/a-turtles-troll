
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.entity.Player
import org.bukkit.entity.EntityType
import org.bukkit.block.Block
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class EndDirtListener() : AbstractFeature(), Listener {

  override val name: String = "enddirt"

  override val description: String = "Dirt placed in the End turns to a Shulker"

  @EventHandler
  fun onBlockPlace(event: BlockPlaceEvent) {
    if (!isEnabled()) {
      return
    }
    val targetBlock = event.getBlockPlaced()
    val world = targetBlock.world
    if (world.environment == World.Environment.THE_END) {
      if (targetBlock.type == Material.DIRT) {
        targetBlock.type = Material.AIR
        world.spawnEntity(targetBlock.location, EntityType.SHULKER)
      }
    }
  }

}
