
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.util.retainAll
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.GameModification

import org.bukkit.Server
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.plugin.Plugin

import kotlin.collections.ArrayList

open class RecipeDeleter(
  private val server: Server,
  val materials: List<Material>
) : AbstractFeature(), GameModification {

  var storedRecipes: List<Recipe> = ArrayList()
    private set

  override val name = "recipes"

  override val description = "Disables specific crafting recipes"

  override fun enable() {
    super.enable()
    removeRecipes()
  }

  override fun disable() {
    super.disable()
    addRecipes()
  }

  override fun onPluginEnable(plugin: Plugin) {
    removeRecipes()
  }

  override fun onPluginDisable(plugin: Plugin) {
    addRecipes()
  }

  fun removeRecipes() {
    storedRecipes = server.recipeIterator().retainAll {
      !materials.contains(it.result.type)
    }
  }

  fun addRecipes() {
    for (recipe in storedRecipes) {
      server.addRecipe(recipe)
    }
    storedRecipes = ArrayList()
  }

}
