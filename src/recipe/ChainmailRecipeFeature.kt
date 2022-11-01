
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.feature.container.RecipeContainer
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ItemStack

class ChainmailRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  companion object : FeatureContainerFactory<FeatureContainer> {
    override fun create(state: BuilderState): FeatureContainer =
      RecipeContainer(ChainmailRecipeFeature(state.plugin))
  }

  override val name = "chainmailrecipe"

  override val description = "Chainmail armor can be crafted from lava buckets"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> =
    listOf(helmetRecipe, chestplateRecipe, leggingsRecipe, bootsRecipe)

  private val helmetRecipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
    val recipe = ShapedRecipe(key, ItemStack(Material.CHAINMAIL_HELMET, 1))
    recipe.shape(
      "AAA",
      "A A",
    )
    recipe.setIngredient('A', Material.LAVA_BUCKET)
    recipe
  }

  private val chestplateRecipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
    val recipe = ShapedRecipe(key, ItemStack(Material.CHAINMAIL_CHESTPLATE, 1))
    recipe.shape(
      "A A",
      "AAA",
      "AAA",
    )
    recipe.setIngredient('A', Material.LAVA_BUCKET)
    recipe
  }

  private val leggingsRecipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
    val recipe = ShapedRecipe(key, ItemStack(Material.CHAINMAIL_LEGGINGS, 1))
    recipe.shape(
      "AAA",
      "A A",
      "A A",
    )
    recipe.setIngredient('A', Material.LAVA_BUCKET)
    recipe
  }

  private val bootsRecipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
    val recipe = ShapedRecipe(key, ItemStack(Material.CHAINMAIL_BOOTS, 1))
    recipe.shape(
      "A A",
      "A A",
    )
    recipe.setIngredient('A', Material.LAVA_BUCKET)
    recipe
  }

}
