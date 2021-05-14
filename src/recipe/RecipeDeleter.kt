
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.ext.*;
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Server
import org.bukkit.Material
import org.bukkit.inventory.Recipe

import kotlin.collections.ArrayList

open class RecipeDeleter(
  private val server: Server,
  val materials: List<Material>
) : AbstractFeature() {

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
