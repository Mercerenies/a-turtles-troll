
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*;

import org.bukkit.Server
import org.bukkit.Material
import org.bukkit.inventory.Recipe

import kotlin.collections.ArrayList

class RecipeDeleter(vararg val materials: Material) {
  var storedRecipes: List<Recipe> = ArrayList()
    private set

  fun removeRecipes(server: Server) {
    storedRecipes = server.recipeIterator().retainAll {
      !materials.contains(it.result.type)
    }
  }

  fun addRecipes(server: Server) {
    for (recipe in storedRecipes) {
      server.addRecipe(recipe)
    }
    storedRecipes = ArrayList()
  }

}
