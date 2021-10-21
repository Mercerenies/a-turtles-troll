
package com.mercerenies.turtletroll.recipe

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.ItemStack

class AngelRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  override val name = "angelrecipe"

  override val description = "Angels can be crafted into wood"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> {
    val recipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val recipe = ShapelessRecipe(key, ItemStack(Material.JUNGLE_SLAB, 2))
      recipe.addIngredient(Material.ARMOR_STAND)
      recipe
    }
    return listOf(recipe)
  }

}
