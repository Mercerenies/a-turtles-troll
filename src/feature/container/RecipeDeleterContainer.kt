
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.recipe.RecipeDeleter

class RecipeDeleterContainer(
  private val recipeDeleter: RecipeDeleter,
) : AbstractFeatureContainer() {

  override val features: Iterable<Feature>
    get() = listOf(recipeDeleter)

  override val recipeDeleters: Iterable<RecipeDeleter>
    get() = listOf(recipeDeleter)

}
