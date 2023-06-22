
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand

import org.bukkit.event.Listener

import com.comphenix.protocol.events.PacketListener

// Adapts a DropFeatureContainer to be a FeatureContainer.
class DropFeatureContainerAdaptor(
  private val impl: DropFeatureContainer,
) : FeatureContainer {

  override val listeners: Iterable<Listener>
    get() = impl.listeners

  override val features: Iterable<Feature>
    get() = impl.features

  override val runnables: Iterable<RunnableFeature>
    get() = impl.runnables

  override val packetListeners: Iterable<PacketListener>
    get() = impl.packetListeners

  override val recipes: Iterable<RecipeFeature>
    get() = impl.recipes

  override val recipeDeleters: Iterable<RecipeDeleter>
    get() = impl.recipeDeleters

  override val commands: Iterable<Pair<String, PermittedCommand<Command>>>
    get() = impl.commands

}
