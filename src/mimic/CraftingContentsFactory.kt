
package com.mercerenies.turtletroll.mimic

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.event.inventory.InventoryType

object CraftingContentsFactory : MimicContentsFactory {

  override fun makeInventory(holder: InventoryHolder, store: ChestContentsStore): Inventory =
    Bukkit.createInventory(holder, InventoryType.WORKBENCH)

}
