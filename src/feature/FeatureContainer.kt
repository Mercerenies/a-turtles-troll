
package com.mercerenies.turtletroll.feature

import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand

import org.bukkit.event.Listener

interface FeatureContainer {

  val listeners: Iterable<Listener>

  val features: Iterable<Feature>

  val runnables: Iterable<RunnableFeature>

  val recipes: Iterable<RecipeFeature>

  val recipeDeleters: Iterable<RecipeDeleter>

  val commands: Iterable<Pair<String, PermittedCommand<Command>>>

}
