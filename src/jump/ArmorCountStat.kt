
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.util.*

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Inventory

abstract class ArmorCountStat() : EncumbranceStat {

  companion object {

    val LEATHER_ARMOR_ITEMS = setOf(
      Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
      Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
    )

    fun getAllArmor(player: Player): List<ItemStack> =
      player.equipment.armorContents.toList().filter {
        it != null && it.type != Material.AIR
      }

  }

  class Leather(
    override val multiplier: Double,
  ) : ArmorCountStat() {

    override val explanationWord: String = "leather armor"

    override fun matchesArmor(itemStack: ItemStack): Boolean =
      LEATHER_ARMOR_ITEMS.contains(itemStack.type)

  }

  class NonLeather(
    override val multiplier: Double,
  ) : ArmorCountStat() {

    override val explanationWord: String = "non-leather armor"

    override fun matchesArmor(itemStack: ItemStack): Boolean =
      !LEATHER_ARMOR_ITEMS.contains(itemStack.type)

  }

  abstract val multiplier: Double
  abstract val explanationWord: String

  abstract fun matchesArmor(itemStack: ItemStack): Boolean

  private fun countOccupiedSlots(player: Player): Int =
    getAllArmor(player).filter(this::matchesArmor).size

  override fun calculateEncumbrance(player: Player): EncumbranceContribution {
    val occupiedSlots = countOccupiedSlots(player)
    return EncumbranceContribution(
      amount = occupiedSlots * multiplier,
      reason = "from ${explanationWord}",
    )
  }

}
