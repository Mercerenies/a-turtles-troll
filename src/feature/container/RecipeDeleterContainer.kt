
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand

import org.bukkit.event.Listener

class RecipeDeleterContainer(
  private val recipeDeleter: RecipeDeleter,
) : AbstractFeatureContainer() {

  override val features: Iterable<Feature>
    get() = listOf(recipeDeleter)

  override val recipeDeleters: Iterable<RecipeDeleter>
    get() = listOf(recipeDeleter)

}
