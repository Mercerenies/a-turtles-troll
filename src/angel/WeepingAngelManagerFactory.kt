
package com.mercerenies.turtletroll.angel

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.AngelRecipeFeature
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry

import org.bukkit.Bukkit

import kotlin.collections.HashMap
import kotlin.random.Random

class WeepingAngelManagerFactory(
  private val deathFeatureId: String,
) : FeatureContainerFactory<FeatureContainer> {

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

  override fun create(state: BuilderState): FeatureContainer {
    var deathRegistry = state.getSharedData(deathFeatureId, CustomDeathMessageRegistry::class)
    if (deathRegistry == null) {
      // Log a warning and use a default value
      Bukkit.getLogger().warning("Could not find death registry, got null")
      deathRegistry = CustomDeathMessageRegistry.Unit
    }
    return WeepingAngelContainer(
      angelManager = WeepingAngelManager(state.plugin, deathRegistry),
      angelRecipe = AngelRecipeFeature(state.plugin),
    )
  }

}
