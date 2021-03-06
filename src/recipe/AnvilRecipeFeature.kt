
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.Constants

import org.bukkit.plugin.Plugin
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack

class AnvilRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  override val name = "anvilrecipe"

  override val description = "Anvils can be smelted"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> {
    val recipe0 = UnkeyedRecipe<Recipe> { FurnaceRecipe(it, ItemStack(Material.IRON_NUGGET), Material.ANVIL, 0.2f, Constants.TICKS_PER_SECOND * 10) }
    val recipe1 = UnkeyedRecipe<Recipe> { FurnaceRecipe(it, ItemStack(Material.IRON_NUGGET), Material.CHIPPED_ANVIL, 0.2f, Constants.TICKS_PER_SECOND * 10) }
    val recipe2 = UnkeyedRecipe<Recipe> { FurnaceRecipe(it, ItemStack(Material.IRON_NUGGET), Material.DAMAGED_ANVIL, 0.2f, Constants.TICKS_PER_SECOND * 10) }
    return listOf(recipe0, recipe1, recipe2)
  }

}
