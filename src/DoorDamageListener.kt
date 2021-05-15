
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.entity.Player
import org.bukkit.block.Block
import org.bukkit.block.`data`.Bisected

import kotlin.collections.HashMap

class DoorDamageListener(
  val maxUses: Int = 16,
) : AbstractFeature(), Listener {

  companion object {

    val DOUBLE_DOORS = setOf(
      Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.CRIMSON_DOOR,
      Material.DARK_OAK_DOOR, Material.IRON_DOOR, Material.JUNGLE_DOOR,
      Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.WARPED_DOOR,
    )

    val DOORS = setOf(
      Material.ACACIA_DOOR, Material.ACACIA_TRAPDOOR, Material.BIRCH_DOOR,
      Material.BIRCH_TRAPDOOR, Material.CRIMSON_DOOR, Material.CRIMSON_TRAPDOOR,
      Material.DARK_OAK_DOOR, Material.DARK_OAK_TRAPDOOR, Material.IRON_DOOR,
      Material.IRON_TRAPDOOR, Material.JUNGLE_DOOR, Material.JUNGLE_TRAPDOOR,
      Material.OAK_DOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_DOOR,
      Material.SPRUCE_TRAPDOOR, Material.WARPED_DOOR, Material.WARPED_TRAPDOOR,
    )

    fun adjustDoorBlock(block: Block): Location {
      val blockData = block.blockData
      return if ((DOUBLE_DOORS.contains(block.type)) && (blockData is Bisected) && (blockData.getHalf() == Bisected.Half.TOP)) {
        block.location.clone().add(0.0, -1.0, 0.0)
      } else {
        block.location.clone()
      }
    }

    private fun destroyBlock(block: Block) {
      block.type = Material.AIR
    }

  }

  private val doorMemory = HashMap<Location, Int>()

  override val name = "doordrop"

  override val description = "Doors break after some number of uses"

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getClickedBlock()
    if ((block != null) && (DOORS.contains(block.type))) {
      val loc = adjustDoorBlock(block)
      decrementDoor(event, loc)
    }
  }

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {
    // Note: This is NOT a BlockBreakAction; it triggers on its own
    // (this class is smart enough to understand how to properly
    // destroy doors, unlike CancelDropAction.
    if (!isEnabled()) {
      return
    }
    if (DOORS.contains(event.block.type)) {
      event.setCancelled(true)
      var loc = adjustDoorBlock(event.block)
      destroyBlock(loc.block)
    }
  }

  private fun decrementDoor(event: PlayerInteractEvent, loc: Location) {
    val uses = doorMemory[loc] ?: maxUses
    println(loc)
    if (uses == 1) {
      doorMemory.remove(loc)
      event.setCancelled(true)
      destroyBlock(loc.block)
    } else {
      doorMemory[loc] = uses - 1
    }
  }

}
