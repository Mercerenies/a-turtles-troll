
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.setBasicCustomModelData

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.Material
import org.bukkit.NamespacedKey

import net.kyori.adventure.text.Component

class TacticalDirtManager(_plugin: Plugin) : RecipeFeature(_plugin) {
  companion object : FeatureContainerFactory<FeatureContainer> {
    override fun create(state: BuilderState): FeatureContainer {
      val manager = TacticalDirtManager(state.plugin)
      return FeatureBuilder()
          .addFeature(manager)
          .addGameModification(manager)
          .build()
    }

    private val CUSTOM_MODEL_DATA = 1
  }

  override val name = "tacticaldirt"

  override val description = "Crafting dirt and seeds makes Tactical Dirt."

  private val tacticalDirtRecipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
    val outItem = ItemStack(Material.BAKED_POTATO, 1)
    val meta = outItem.itemMeta
    meta.displayName(Component.text("Nutrient Dirt"))
    meta.setBasicCustomModelData(CUSTOM_MODEL_DATA)
    outItem.itemMeta = meta
    val recipe = ShapelessRecipe(key, outItem)
    recipe.addIngredient(1, Material.DIRT)
    recipe.addIngredient(1, Material.WHEAT_SEEDS)
    recipe
  }

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> =
      listOf(tacticalDirtRecipe)

  /////
}
