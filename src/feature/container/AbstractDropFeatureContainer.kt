
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.Weight

import org.bukkit.event.Listener

// Abstract parent version of DropFeatureContainer which has trivially
// empty implementations for all methods
open class AbstractDropFeatureContainer() : DropFeatureContainer {

  open override val listeners: Iterable<Listener>
    get() = listOf()

  open override val features: Iterable<Feature>
    get() = listOf()

  open override val runnables: Iterable<RunnableFeature>
    get() = listOf()

  open override val recipes: Iterable<RecipeFeature>
    get() = listOf()

  open override val recipeDeleters: Iterable<RecipeDeleter>
    get() = listOf()

  open override val commands: Iterable<Pair<String, PermittedCommand<Command>>>
    get() = listOf()

  open override val preRules: List<BlockBreakAction>
    get() = listOf()

  open override val actions: List<Weight<BlockBreakAction>>
    get() = listOf()

  open override val postRules: List<BlockBreakAction>
    get() = listOf()

}
