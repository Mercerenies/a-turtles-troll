
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.event.Listener

class ManagerContainer<T>(
  private val manager: T,
) : AbstractFeatureContainer()
where T : RunnableFeature, T : Listener {

  override val listeners: Iterable<Listener>
    get() = listOf(manager)

  override val features: Iterable<Feature>
    get() = listOf(manager)

  override val gameModifications: Iterable<RunnableFeature>
    get() = listOf(manager)

}
