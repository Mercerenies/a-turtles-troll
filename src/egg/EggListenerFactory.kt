
package com.mercerenies.turtletroll.egg

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

import kotlin.collections.HashMap
import kotlin.random.Random

abstract class EggListenerFactory() : FeatureContainerFactory<FeatureContainer> {

  // EggListenerFactory is an abstract class that can be parameterized
  // by the desired egg spawns. The companion object is a concrete
  // subclass with a suitable default list of spawns.
  companion object : EggListenerFactory() {

    override fun getEffects(plugin: Plugin) =
      EggHatch.defaultEffects(plugin)

  }

  abstract fun getEffects(plugin: Plugin): List<Weight<EggHatchEffect>>

  override fun create(state: BuilderState): FeatureContainer =
    ListenerContainer(EggListener(getEffects(state.plugin)))

}
