
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.util.lazyFlatten

import org.bukkit.event.Listener

class CompositeFeatureContainer(
  private val allContainers: Iterable<FeatureContainer>,
) : FeatureContainer {

  override val listeners: Iterable<Listener> =
    allContainers.map { it.listeners }.lazyFlatten()

  override val features: Iterable<Feature> =
    allContainers.map { it.features }.lazyFlatten()

  override val runnables: Iterable<RunnableFeature> =
    allContainers.map { it.runnables }.lazyFlatten()

  override val recipes: Iterable<RecipeFeature> =
    allContainers.map { it.recipes }.lazyFlatten()

  override val recipeDeleters: Iterable<RecipeDeleter> =
    allContainers.map { it.recipeDeleters }.lazyFlatten()

  override val commands: Iterable<Pair<String, PermittedCommand<Command>>> =
    allContainers.map { it.commands }.lazyFlatten()

}
