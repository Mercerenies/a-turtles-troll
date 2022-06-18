
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature

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
