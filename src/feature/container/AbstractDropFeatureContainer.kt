
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.Schedulable
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.Weight

import org.bukkit.event.Listener

import com.comphenix.protocol.events.PacketListener

// Abstract parent version of DropFeatureContainer which has trivially
// empty implementations for all methods
open class AbstractDropFeatureContainer() : DropFeatureContainer {

  open override val listeners: Iterable<Listener>
    get() = listOf()

  open override val features: Iterable<Feature>
    get() = listOf()

  open override val runnables: Iterable<Schedulable>
    get() = listOf()

  open override val packetListeners: Iterable<PacketListener>
    get() = listOf()

  open override val recipes: Iterable<RecipeFeature>
    get() = listOf()

  open override val recipeDeleters: Iterable<RecipeDeleter>
    get() = listOf()

  open override val commands: Iterable<Pair<String, PermittedCommand<Command>>>
    get() = listOf()

  open override val debugCommands: Iterable<Pair<String, Command>>
    get() = listOf()

  open override val preRules: List<BlockBreakAction>
    get() = listOf()

  open override val actions: List<Weight<BlockBreakAction>>
    get() = listOf()

  open override val postRules: List<BlockBreakAction>
    get() = listOf()

}
