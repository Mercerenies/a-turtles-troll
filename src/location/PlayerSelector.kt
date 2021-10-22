
package com.mercerenies.turtletroll.location

import com.mercerenies.turtletroll.mimic.MimicIdentifier
import com.mercerenies.turtletroll.cake.CakeListener

import org.bukkit.block.Block
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.entity.Player

import kotlin.random.Random

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

}
