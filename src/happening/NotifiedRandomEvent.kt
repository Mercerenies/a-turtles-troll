
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.util.runTaskLater

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

import net.kyori.adventure.text.Component

// A RandomEvent that sends a broadcast message to all players in
// advance of the actual event that fires.
abstract class NotifiedRandomEvent(
  private val plugin: Plugin,
) : RandomEvent {

  abstract val messages: List<Component>

  open val delayTime: Long
    get() = 15L * Constants.TICKS_PER_SECOND

  abstract fun onAfterDelay(state: RandomEventState): Unit

  override fun fire(state: RandomEventState) {
    for (message in messages) {
      Messages.broadcastMessage(message)
    }
    Bukkit.getScheduler().runTaskLater(plugin, delayTime) {
      onAfterDelay(state)
    }
  }

}
