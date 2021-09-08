
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.feature.AbstractFeature
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack

class AnvilRecipeFeature(val plugin: Plugin) : AbstractFeature() {

  companion object {
    val RECIPE_KEY0 = "anvil_smelt"
    val RECIPE_KEY1 = "anvil_smelt1"
    val RECIPE_KEY2 = "anvil_smelt2"
    val TICKS_PER_SECOND = 20
  }

  override val name = "anvilrecipe"

  override val description = "Anvils can be smelted"

  val namespacedKey0 = NamespacedKey(plugin, RECIPE_KEY0)
  val namespacedKey1 = NamespacedKey(plugin, RECIPE_KEY1)
  val namespacedKey2 = NamespacedKey(plugin, RECIPE_KEY2)

  override fun enable() {
    if (!isEnabled()) {
      addRecipes()
    }
    super.enable()
  }

  override fun disable() {
    if (isEnabled()) {
      removeRecipes()
    }
    super.disable()
  }

  fun addRecipes() {

    val recipe0 = FurnaceRecipe(namespacedKey0, ItemStack(Material.IRON_NUGGET), Material.ANVIL, 0.2f, TICKS_PER_SECOND * 10)
    Bukkit.addRecipe(recipe0)

    val recipe1 = FurnaceRecipe(namespacedKey1, ItemStack(Material.IRON_NUGGET), Material.CHIPPED_ANVIL, 0.2f, TICKS_PER_SECOND * 10)
    Bukkit.addRecipe(recipe1)

    val recipe2 = FurnaceRecipe(namespacedKey2, ItemStack(Material.IRON_NUGGET), Material.DAMAGED_ANVIL, 0.2f, TICKS_PER_SECOND * 10)
    Bukkit.addRecipe(recipe2)

  }

  fun removeRecipes() {
    Bukkit.removeRecipe(namespacedKey0)
    Bukkit.removeRecipe(namespacedKey1)
    Bukkit.removeRecipe(namespacedKey2)
  }

}
