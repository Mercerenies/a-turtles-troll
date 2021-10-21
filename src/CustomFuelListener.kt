
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceBurnEvent
import org.bukkit.Material


// Not working yet :(
class CustomFuelListener() : AbstractFeature(), Listener {

  override val name = "customfuel"

  override val description = "Armor stands can be burned as furnace fuel"

  @EventHandler
  fun onFurnaceBurn(event: FurnaceBurnEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.getFuel().type == Material.ARMOR_STAND) {
      event.setBurning(true)
      event.setBurnTime(800) // Half the burn time of coal
    }
  }

}
