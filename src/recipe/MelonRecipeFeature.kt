
package com.mercerenies.turtletroll.recipe

import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.ItemStack

class MelonRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  override val name = "melonrecipe"

  override val description = "Melon slices can be crafted into pumpkin seeds"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> {
    val recipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val recipe = ShapelessRecipe(key, ItemStack(Material.PUMPKIN_SEEDS, 1))
      recipe.addIngredient(Material.MELON_SLICE)
      recipe
    }
    return listOf(recipe)
  }

}
