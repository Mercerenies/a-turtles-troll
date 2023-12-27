
package com.mercerenies.turtletroll.mimic

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

fun interface MimicContentsFactory {

  companion object {

    val VALID_CHEST_SIZES = listOf(0, 9, 18, 27, 36, 45, 54)
    val SMALL_CHEST_SIZE = 27
    val LARGE_CHEST_SIZE = 54

    fun several(vararg factories: MimicContentsFactory): MimicContentsFactory {
      if (factories.isEmpty()) {
        throw IllegalArgumentException("At least one factory must be provided")
      }
      return MimicContentsFactory { holder, store -> factories.random().makeInventory(holder, store) }
    }

    fun several(factories: List<MimicContentsFactory>): MimicContentsFactory =
      several(*factories.toTypedArray())

  }

  fun makeInventory(holder: InventoryHolder, store: ChestContentsStore): Inventory

}
