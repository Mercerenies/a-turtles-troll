
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand

import org.bukkit.event.Listener

// Abstract parent version of FeatureContainer which has trivially
// empty implementations for all methods
open class AbstractFeatureContainer() : FeatureContainer {

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

}
