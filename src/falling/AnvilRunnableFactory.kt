
package com.mercerenies.turtletroll.falling

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.feature.GameModification
import com.mercerenies.turtletroll.recipe.AnvilRecipeFeature

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

    override val gameModifications: List<GameModification> =
      listOf(anvilRunnable, anvilRecipe)

  }

  override fun create(state: BuilderState): FeatureContainer {
    val runnable = AnvilRunnable(
      plugin = state.plugin,
      minDropHeight = state.config.getInt("anvil.min_drop_height"),
      maxDropHeight = state.config.getInt("anvil.max_drop_height"),
      taskPeriodSecs = state.config.getInt("anvil.period"),
    )
    return AnvilContainer(
      anvilRunnable = runnable,
      anvilRecipe = AnvilRecipeFeature(state.plugin),
    )
  }

}
