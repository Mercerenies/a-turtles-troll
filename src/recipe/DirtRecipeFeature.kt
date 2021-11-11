
package com.mercerenies.turtletroll.recipe


import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ItemStack

class DirtRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  override val name = "dirtrecipe"

  override val description = "Nine dirt can be crafted into a stick; nine sticks can be crafted into dirt"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> {
    val recipe1 = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val recipe = ShapedRecipe(key, ItemStack(Material.STICK, 1))
      recipe.shape("AAA",
                   "AAA",
                   "AAA")
      recipe.setIngredient('A', Material.DIRT)
      recipe
    }
    val recipe2 = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val recipe = ShapedRecipe(key, ItemStack(Material.DIRT, 1))
      recipe.shape("AAA",
                   "AAA",
                   "AAA")
      recipe.setIngredient('A', Material.STICK)
      recipe
    }
    return listOf(recipe1, recipe2)
  }

}
