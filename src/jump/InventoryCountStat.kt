
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.util.*

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class InventoryCountStat(
  val multiplier: Double,
) : EncumbranceStat {

  private fun countOccupiedSlots(inventory: Inventory): Int =
    // HACK getStorageContents() nullability annotation cannot
    // be understood by Kotlin.
    inventory.getStorageContents()!!.filter { it != null }.size

  override fun calculateEncumbrance(player: Player): EncumbranceContribution {
    val occupiedSlots = countOccupiedSlots(player.inventory)
    val occupiedSlotsWord = amounts(occupiedSlots, "occupied inventory slot")
    return EncumbranceContribution(
      amount = occupiedSlots * multiplier,
      reason = "from ${occupiedSlotsWord}",
    )
  }

}
