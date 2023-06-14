
package com.mercerenies.turtletroll.telegraph

import org.bukkit.entity.Player

import kotlin.collections.HashSet

import java.util.UUID

// A Telegrapher performs some action the first time it happens to a
// player. If it happens again to the same player, it does not perform
// the action.
abstract class Telegrapher() {

  private val triggeredFor = HashSet<UUID>()

  fun trigger(player: Player) {
    // This might be null for offline players (i.e. the player
    // performed an action and then was immediately kicked or left).
    // If that happens, just don't do anything.
    val playerUuid = player.playerProfile.id
    if ((playerUuid != null) && (!triggeredFor.contains(playerUuid))) {
      triggeredFor.add(playerUuid)
      onPerform(player)
    }
  }

  protected abstract fun onPerform(player: Player): Unit

}
