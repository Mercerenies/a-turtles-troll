
package com.mercerenies.turtletroll.mimic

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Inventory

object MimicIdentifier {

  val DIRT = ItemStack(Material.DIRT)

  fun chestPattern(): Array<ItemStack?> =
    arrayOf(
      DIRT, null, DIRT, null, DIRT, DIRT, DIRT, null, DIRT,
      DIRT, DIRT, DIRT, null, DIRT, null, DIRT, null, DIRT,
      DIRT, null, DIRT, null, DIRT, DIRT, DIRT, null, null,
    )

  fun spawnMimic(block: Block): Chest {
    block.setType(Material.CHEST)
    val chest = block.getState() as Chest
    val inventory = chest.getBlockInventory()
    inventory.contents = chestPattern()
    return chest
  }

  fun isMimic(block: Block?): Boolean {
    if (block == null) {
      return false
    }
    val state = block.state
    if (state !is Chest) {
      return false
    }
    val contents = state.getBlockInventory().contents
    return (contents contentEquals chestPattern())
  }

  fun belongsToMimic(inventory: Inventory): Boolean =
    isMimic(inventory.location?.block)

}
