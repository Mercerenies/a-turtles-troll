
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.feature.AbstractFeature
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.ItemStack

class AngelRecipeFeature(val plugin: Plugin) : AbstractFeature() {

  companion object {
    val RECIPE_KEY0 = "angel_craft"
    val TICKS_PER_SECOND = 20
  }

  override val name = "angelrecipe"

  override val description = "Angels can be crafted into wood"

  val namespacedKey0 = NamespacedKey(plugin, RECIPE_KEY0)

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

    val recipe0 = ShapelessRecipe(namespacedKey0, ItemStack(Material.JUNGLE_SLAB, 2))
    recipe0.addIngredient(Material.ARMOR_STAND);
    Bukkit.addRecipe(recipe0)

  }

  fun removeRecipes() {
    Bukkit.removeRecipe(namespacedKey0)
  }

}
