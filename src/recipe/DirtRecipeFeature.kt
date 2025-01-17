
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RecipeContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ItemStack

class DirtRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      RecipeContainer(DirtRecipeFeature(state.plugin))

  }

  override val name = "dirtrecipe"

  override val description = "Nine sticks can be crafted into dirt"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> {
    val recipe1 = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val recipe = ShapedRecipe(key, ItemStack(Material.DIRT, 1))
      recipe.shape(
        "AAA",
        "AAA",
        "AAA",
      )
      recipe.setIngredient('A', Material.STICK)
      recipe
    }
    return listOf(recipe1)
  }

}
