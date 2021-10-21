
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.Creeper
import org.bukkit.inventory.ItemStack
import org.bukkit.Material


class ChargedCreeperListener() : AbstractFeature(), Listener {

  override val name = "chargedcreeper"

  override val description = "Killing a charged creeper drops a diamond"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Creeper) {
      if (entity.isPowered()) {
        event.getDrops().add(ItemStack(Material.DIAMOND))
      }
    }
  }

}
