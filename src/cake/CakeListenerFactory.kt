
package com.mercerenies.turtletroll.cake

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.Weight

import org.bukkit.plugin.Plugin

abstract class CakeListenerFactory() : FeatureContainerFactory<FeatureContainer> {

  // CakeListenerFactory is an abstract class that can be
  // parameterized by the desired cake effects. The companion object
  // is a concrete subclass with a suitable default list of cake
  // effects.
  companion object : CakeListenerFactory() {

    override fun getEffects(plugin: Plugin) =
      CakeEat.defaultEffects(plugin)

  }

  abstract fun getEffects(plugin: Plugin): List<Weight<CakeEffect>>

  override fun create(state: BuilderState): FeatureContainer =
    ListenerContainer(CakeListener(state.plugin, getEffects(state.plugin)))

}
