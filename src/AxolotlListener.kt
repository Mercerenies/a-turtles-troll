
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.Axolotl
import org.bukkit.entity.Player
import org.bukkit.Bukkit
import org.bukkit.Location


class AxolotlListener() : AbstractFeature(), Listener {

  companion object {

    val DISTANCE_SQUARED_LIMIT = 1024.0 // 32 blocks (squared)

    fun findNearestPlayer(targetLoc: Location): Player? {
      var best: Player? = null
      var bestDistSq = DISTANCE_SQUARED_LIMIT
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

  override val name = "axolotl"

  override val description = "When an axolotl dies, the nearest player also dies"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Axolotl) {
      val nearestPlayer = findNearestPlayer(entity.location)

      if (nearestPlayer == null) {
        Bukkit.broadcastMessage("RIP an axolotl, who died in peace")
      } else {
        Bukkit.broadcastMessage("RIP an axolotl, who took ${nearestPlayer.getDisplayName()} with them")
        nearestPlayer.damage(9999.0, entity)
      }

    }
  }

}
