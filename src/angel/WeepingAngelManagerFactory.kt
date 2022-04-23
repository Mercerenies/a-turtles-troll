
package com.mercerenies.turtletroll.angel

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.AngelRecipeFeature
import com.mercerenies.turtletroll.SpawnReason

import kotlin.collections.HashMap
import kotlin.random.Random

object WeepingAngelManagerFactory : FeatureContainerFactory<FeatureContainer> {

  private class WeepingAngelContainer(
    private val angelManager: WeepingAngelManager,
    private val angelRecipe: AngelRecipeFeature,
  ) : AbstractFeatureContainer() {

    private val compositeFeature =
      CompositeFeature(
        angelManager.name,
        angelManager.description,
        listOf(angelManager, angelRecipe),
      )

    override val listeners =
      listOf(angelManager)

    override val features =
      listOf(compositeFeature)

    override val runnables =
      listOf(angelManager)

    override val recipes =
      listOf(angelRecipe)

  }

  override fun create(state: BuilderState): FeatureContainer =
    WeepingAngelContainer(
      angelManager = WeepingAngelManager(state.plugin),
      angelRecipe = AngelRecipeFeature(state.plugin),
    )

}
