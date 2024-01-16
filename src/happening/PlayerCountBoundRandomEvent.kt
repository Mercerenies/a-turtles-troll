
package com.mercerenies.turtletroll.happening

import org.bukkit.Bukkit

// Random event that only fires if there are at least minPlayerCount
// players online. Usually constructed with the
// RandomEvent.onlyIfPlayersOnline extension method.
class PlayerCountBoundRandomEvent(
  val event: RandomEvent,
  val minPlayerCount: Int,
) : RandomEvent by event {

  override fun canFire(state: RandomEventState): Boolean =
    event.canFire(state) && (Bukkit.getOnlinePlayers().size >= minPlayerCount)

}
