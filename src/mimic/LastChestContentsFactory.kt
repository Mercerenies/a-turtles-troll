
package com.mercerenies.turtletroll.mimic

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

// Shows a random chest opened recently by a player.
object LastChestContentsFactory : MimicContentsFactory {

  override fun makeInventory(holder: InventoryHolder, store: ChestContentsStore): Inventory {
    val player = store.keys.randomOrNull()
    if (player == null) {
      // There is nothing in the contents store, so just return an
      // empty inventory.
      return emptyInventory(holder)
    }
    val contents = store.getContents(player)!!
    val inventory = Bukkit.createInventory(holder, contents.size)
    inventory.contents = contents
    return inventory
  }

  private fun emptyInventory(holder: InventoryHolder): Inventory =
    Bukkit.createInventory(holder, MimicContentsFactory.SMALL_CHEST_SIZE)

}
