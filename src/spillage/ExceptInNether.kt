
package com.mercerenies.turtletroll.spillage

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.block.BlockDropItemEvent

// Runs the inner handler, but then resets the block type to air if
// we're in the Nether.
class ExceptInNether(
  private val inner: SpillageHandler,
) : SpillageHandler {

  override fun matches(item: Item) = inner.matches(item)

  override fun run(item: Item) {
    inner.run(item)
    if (item.world.environment == World.Environment.NETHER) {
      item.location.block.type = Material.AIR
    }
  }

}

fun SpillageHandler.exceptInNether(): SpillageHandler =
  ExceptInNether(this)
