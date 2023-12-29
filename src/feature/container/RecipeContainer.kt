
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.recipe.RecipeFeature

class RecipeContainer(
  private val recipe: RecipeFeature,
) : AbstractFeatureContainer() {

  override val features: Iterable<Feature>
    get() = listOf(recipe)

  override val gameModifications: Iterable<RecipeFeature>
    get() = listOf(recipe)

}
