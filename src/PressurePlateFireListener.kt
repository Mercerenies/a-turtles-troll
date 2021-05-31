
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.Action
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.entity.Player
import org.bukkit.block.Block

import kotlin.random.Random

class PressurePlateFireListener(
  val chance: Double = 0.05,
  val random: Random = Random.Default,
) : AbstractFeature(), Listener {

  companion object {
    val PLATES = setOf(
      Material.ACACIA_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE,
      Material.CRIMSON_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE,
      Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE,
      Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE,
      Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE,
      Material.STONE_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE,
    )
  }

  override val name: String = "platefire"

  override val description: String = "Stepping on pressure plates has a chance of igniting the player"

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.getAction() != Action.PHYSICAL) {
      return
    }
    val block = event.getClickedBlock();
    if ((block != null) && (PLATES.contains(block.type))) {
      if (random.nextDouble(1.0) < chance) {
        event.setCancelled(true)
        block.type = Material.FIRE
      }
    }
  }

}
