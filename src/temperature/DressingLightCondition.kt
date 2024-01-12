
package com.mercerenies.turtletroll.temperature

import org.bukkit.entity.Player
import org.bukkit.Material

object DressingLightCondition : BiomeSafetyCondition {

  private val HEAVY_ARMOR = setOf(
    Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
    Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
    Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
    Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
    Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE,
    Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
  )

  override fun evaluate(player: Player): BiomeSafetyCondition.Result =
    if (!isWearingAnythingHeavy(player)) {
      BiomeSafetyCondition.safe("You are dressed light")
    } else {
      BiomeSafetyCondition.unsafe("You are wearing heavy armor (iron, gold, etc.)")
    }

  private fun isWearingAnythingHeavy(player: Player): Boolean {
    val inv = player.inventory
    val armorSlots = listOf(inv.helmet, inv.chestplate, inv.leggings, inv.boots)
    return armorSlots.any { it != null && HEAVY_ARMOR.contains(it.type) }
  }

}
