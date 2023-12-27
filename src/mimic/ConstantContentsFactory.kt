
package com.mercerenies.turtletroll.mimic

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class ConstantContentsFactory(
  private val contents: Array<ItemStack?>,
) : MimicContentsFactory {

  init {
    if (!MimicContentsFactory.VALID_CHEST_SIZES.contains(contents.size)) {
      throw IllegalArgumentException("Invalid chest size: ${contents.size}")
    }
  }

  override fun makeInventory(holder: InventoryHolder, store: ChestContentsStore): Inventory {
    val inventory = Bukkit.createInventory(holder, contents.size)
    inventory.contents = contents
    return inventory
  }

}
