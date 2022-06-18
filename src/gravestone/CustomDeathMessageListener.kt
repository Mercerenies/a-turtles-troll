
package com.mercerenies.turtletroll.gravestone

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.ext.*

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.plugin.Plugin

// This one is *not* a feature; it's always enabled. It's a central
// piece of infrastructure provided to several plugin features which
// allows them to set custom death messages.
class CustomDeathMessageListener(
  val plugin: Plugin,
) : Listener, CustomDeathMessageRegistry {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val DEATH_MESSAGE_KEY = "com.mercerenies.turtletroll.gravestone.CustomDeathMessageListener.DEATH_MESSAGE_KEY"

    override fun create(state: BuilderState): FeatureContainer {
      val listener = CustomDeathMessageListener(state.plugin)
      state.registerSharedData(DEATH_MESSAGE_KEY, listener)
      return object : AbstractFeatureContainer() {
        // Note: *Not* a feature. Just a listener. ListenerContainer
        // does not fit our needs here since it provides a feature as
        // well as a listener.
        override val listeners = listOf(listener)
      }
    }

  }

  private var customDeathMessage: CustomDeathMessage? = null

  override fun<R> withCustomDeathMessage(message: CustomDeathMessage, block: () -> R): R {
    val prior = customDeathMessage
    try {
      customDeathMessage = message
      return block()
    } finally {
      customDeathMessage = prior
    }
  }

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    val custom = customDeathMessage
    if (custom != null) {
      event.setDeathMessage(custom.message)
    }
  }

}
