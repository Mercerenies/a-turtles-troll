
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.recipe.RecipeDeleter
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.PermittedCommand

import org.bukkit.event.Listener

class ListenerContainer<T>(
  private val listener: T,
) : AbstractFeatureContainer()
where T : Listener, T : Feature {

  override val listeners: Iterable<Listener>
    get() = listOf(listener)

  override val features: Iterable<Feature>
    get() = listOf(listener)

}
