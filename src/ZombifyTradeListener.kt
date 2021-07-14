
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.MerchantInventory
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.entity.Player
import org.bukkit.entity.ZombieVillager

import kotlin.collections.HashMap

class ZombifyTradeListener() : AbstractFeature(), Listener {
  private val playerUI = HashMap<Player, Villager>()

  override val name = "zombietrade"

  override val description = "Villagers turn into zombies after being traded with"

  @EventHandler
  fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
    if (!isEnabled()) {
      return
    }
    val target = event.getRightClicked()
    if (target is Villager) {
      playerUI[event.player] = target
    }
  }

  @EventHandler
  fun onInventoryClose(event: InventoryCloseEvent) {
    if (!isEnabled()) {
      return
    }
    val inv = event.inventory
    if (inv is MerchantInventory) {
      val target = playerUI[event.player]
      if (target != null) {
        val zombie = target.world.spawn(target.location, ZombieVillager::class.java)
        zombie.villagerType = target.villagerType
        zombie.villagerProfession = target.profession
        target.remove()
        playerUI.remove(event.player)
      }
    }
  }

}
