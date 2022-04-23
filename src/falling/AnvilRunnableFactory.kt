
package com.mercerenies.turtletroll.falling

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.AnvilRecipeFeature
import com.mercerenies.turtletroll.SpawnReason

import kotlin.collections.HashMap
import kotlin.random.Random

object AnvilRunnableFactory : FeatureContainerFactory<FeatureContainer> {

  private class AnvilContainer(
    private val anvilRunnable: AnvilRunnable,
    private val anvilRecipe: AnvilRecipeFeature,
  ) : AbstractFeatureContainer() {

    private val compositeFeature =
      CompositeFeature(
        anvilRunnable.name,
        anvilRunnable.description,
        listOf(anvilRunnable, anvilRecipe),
      )

    override val features =
      listOf(compositeFeature)

    override val runnables =
      listOf(anvilRunnable)

    override val recipes =
      listOf(anvilRecipe)

  }

  override fun create(state: BuilderState): FeatureContainer =
    AnvilContainer(
      anvilRunnable = AnvilRunnable(state.plugin),
      anvilRecipe = AnvilRecipeFeature(state.plugin),
    )

}
