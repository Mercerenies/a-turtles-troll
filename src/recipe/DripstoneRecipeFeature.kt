
package com.mercerenies.turtletroll.recipe


import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ItemStack

class DripstoneRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  companion object {
    val TICKS_PER_SECOND = 20
  }

  override val name = "dripstonerecipe"

  override val description = "Dripstone can be used in place of flint when making arrows"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> {
    val recipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val recipe = ShapedRecipe(key, ItemStack(Material.ARROW, 4))
      recipe.shape("A",
                   "B",
                   "C")
      recipe.setIngredient('A', Material.POINTED_DRIPSTONE)
      recipe.setIngredient('B', Material.STICK)
      recipe.setIngredient('C', Material.FEATHER)
      recipe
    }
    return listOf(recipe)
  }

}
