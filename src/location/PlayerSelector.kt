
package com.mercerenies.turtletroll.location

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.util.UUID

// Various helper functions for identifying players
object PlayerSelector {

  fun findNearestPlayer(targetLoc: Location, maxDistSq: Double = Double.MAX_VALUE): Player? {
    var best: Player? = null
    var bestDistSq = maxDistSq
    for (player in Bukkit.getOnlinePlayers()) {
      val playerLoc = player.location
      if (playerLoc.world != targetLoc.world) {
        continue
      }
      val distanceSquared = playerLoc.distanceSquared(targetLoc)
      if (distanceSquared < bestDistSq) {
        best = player
        bestDistSq = distanceSquared
      }
    }
    return best
  }

  fun chooseRandomPlayer(): Player? =
    Bukkit.getOnlinePlayers().toList().sample()

  fun makePlayerMap(): Map<UUID, Player> =
    Bukkit.getOnlinePlayers().associateBy { it.uniqueId }

}
