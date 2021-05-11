
package com.mercerenies.turtletroll

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.MerchantInventory
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.entity.Player

import kotlin.collections.HashMap

class ZombifyTradeListener() : Listener {
  private val playerUI = HashMap<Player, Villager>()

  @EventHandler
  fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
    val target = event.getRightClicked()
    if (target is Villager) {
      playerUI[event.player] = target
    }
  }

  @EventHandler
  fun onInventoryClose(event: InventoryCloseEvent) {
    val inv = event.inventory
    if (inv is MerchantInventory) {
      val target = playerUI[event.player]
      if (target != null) {
        println("Tripped")
        target.world.spawnEntity(target.location, EntityType.ZOMBIE_VILLAGER)
        target.remove()
        playerUI.remove(event.player)
      }
    }
  }

}
