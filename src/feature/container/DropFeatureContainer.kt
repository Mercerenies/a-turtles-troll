
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

// A feature container which also contains drop information.
interface DropFeatureContainer : BaseFeatureContainer {

  val preRules: Iterable<BlockBreakAction>

  val actions: Iterable<Weight<BlockBreakAction>>

  val postRules: Iterable<BlockBreakAction>

}
