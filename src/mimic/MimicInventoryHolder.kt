
package com.mercerenies.turtletroll.mimic

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class MimicInventoryHolder(
  private val factory: MimicContentsFactory,
) : InventoryHolder {

  private val inventoryImplementation: Inventory by lazy {
    factory.makeInventory(this)
  }

  override fun getInventory(): Inventory =
    inventoryImplementation

}
