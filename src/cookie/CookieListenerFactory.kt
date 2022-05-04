
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.recipe.AngelRecipeFeature
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit

import kotlin.collections.HashMap
import kotlin.random.Random

abstract class CookieListenerFactory() : FeatureContainerFactory<FeatureContainer> {

  class Default(private val deathFeatureId: String) : CookieListenerFactory() {

    override fun getDeathRegistry(state: BuilderState): CustomDeathMessageRegistry {
      val deathRegistry = state.getSharedData(deathFeatureId, CustomDeathMessageRegistry::class)
      if (deathRegistry == null) {
        // Log a warning and use a default value
        Bukkit.getLogger().warning("Could not find death registry, got null")
        return CustomDeathMessageRegistry.Unit
      } else {
        return deathRegistry
      }
    }

    override fun getEffects(plugin: Plugin) =
      CookieEat.defaultEffects(plugin)

  }

  abstract fun getDeathRegistry(state: BuilderState): CustomDeathMessageRegistry

  abstract fun getEffects(plugin: Plugin): List<Weight<CookieEffect>>

  override fun create(state: BuilderState): FeatureContainer =
    ListenerContainer(CookieListener(state.plugin, getEffects(state.plugin), getDeathRegistry(state)))

}
