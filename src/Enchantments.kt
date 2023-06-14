
package com.mercerenies.turtletroll

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Enchantments {

  data class EnchantmentData(
    val enchantment: Enchantment,
    val level: Int,
  )

  fun ItemStack.addEnchantment(enchantmentData: EnchantmentData): Unit {
    addEnchantment(enchantmentData.enchantment, enchantmentData.level)
  }

}
