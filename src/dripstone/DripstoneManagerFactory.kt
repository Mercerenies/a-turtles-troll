
package com.mercerenies.turtletroll.dripstone

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.DripstoneRecipeFeature

object DripstoneManagerFactory : FeatureContainerFactory<FeatureContainer> {

  private class DripstoneContainer(
    private val dripstoneManager: DripstoneManager,
    private val dripstoneRecipe: DripstoneRecipeFeature,
  ) : AbstractFeatureContainer() {

    private val compositeFeature =
      CompositeFeature(
        dripstoneManager.name,
        dripstoneManager.description,
        listOf(dripstoneManager, dripstoneRecipe),
      )

    override val listeners =
      listOf(dripstoneManager)

    override val features =
      listOf(compositeFeature)

    override val runnables =
      listOf(dripstoneManager)

    override val gameModifications =
      listOf(dripstoneRecipe)

  }

  override fun create(state: BuilderState): FeatureContainer =
    DripstoneContainer(
      dripstoneManager = DripstoneManager(state.plugin),
      dripstoneRecipe = DripstoneRecipeFeature(state.plugin),
    )

}
