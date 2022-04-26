
package com.mercerenies.turtletroll.overgrowth

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.AngelRecipeFeature
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.Weight

import org.bukkit.plugin.Plugin
import org.bukkit.Material

import kotlin.collections.HashMap
import kotlin.random.Random

class OvergrowthListenerFactory(
  private val overgrowthBlock: () -> Material,
) : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer =
    ListenerContainer(OvergrowthListener(state.plugin, overgrowthBlock))

}
