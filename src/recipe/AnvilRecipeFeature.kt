
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
    val RECIPE_KEY = "anvil_smelt"
    val TICKS_PER_SECOND = 20
  }

  override val name = "anvilrecipe"

  override val description = "Anvils can be smelted"

  val namespacedKey = NamespacedKey(plugin, RECIPE_KEY)

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
    val recipe = FurnaceRecipe(namespacedKey, ItemStack(Material.IRON_NUGGET), Material.ANVIL, 0.2f, TICKS_PER_SECOND * 10)
    Bukkit.addRecipe(recipe)
  }

  fun removeRecipes() {
    Bukkit.removeRecipe(namespacedKey)
  }

}
