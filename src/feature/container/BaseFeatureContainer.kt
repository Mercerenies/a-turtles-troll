
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand

import org.bukkit.event.Listener

import com.comphenix.protocol.events.PacketListener

// Common super-interface for FeatureContainer and
// DropFeatureContainer. I don't want one to inherit from the other
// since I don't want to accidentally discard information about the
// drops, but they do share a common super-interface. If you feel
// inclined to write this as the argument type in a function, consider
// using one of its immediate sub-interfaces instead.
interface BaseFeatureContainer {

  val listeners: Iterable<Listener>

  val features: Iterable<Feature>

  val runnables: Iterable<RunnableFeature>

  val packetListeners: Iterable<PacketListener>

  val recipes: Iterable<RecipeFeature>

  val recipeDeleters: Iterable<RecipeDeleter>

  val commands: Iterable<Pair<String, PermittedCommand<Command>>>

  // As `commands`, but goes in the `/turtle dbg` namespace rather
  // than `/turtle`. All commands in `debugCommands` automatically get
  // the "DEBUG" permission level.
  val debugCommands: Iterable<Pair<String, Command>>

}
