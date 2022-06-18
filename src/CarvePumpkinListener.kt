
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.entity.Item
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CarvePumpkinListener() : AbstractFeature(), Listener {

  companion object {}

  override val name = "carvepumpkin"

  override val description = "..."

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }
    if ((event.item?.type == Material.SHEARS) && (event.action == Action.RIGHT_CLICK_BLOCK) && (event.clickedBlock?.type == Material.PUMPKIN)) {
      val location = event.clickedBlock!!.location
      event.setCancelled(true)
      event.clickedBlock!!.type = Material.CARVED_PUMPKIN
      val droppedItem = location.world!!.spawn(location, Item::class.java)
      droppedItem.itemStack = ItemStack(Material.MELON_SEEDS, 4)
    }
  }

}
