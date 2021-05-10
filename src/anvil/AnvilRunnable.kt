
package com.mercerenies.turtletroll.anvil

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material

import kotlin.collections.HashMap

class AnvilRunnable : BukkitRunnable() {
  private var memory = HashMap<Player, Location>()

  companion object {
    val TICKS_PER_SECOND = 20
    val MAX_ANVIL_HEIGHT = 15
  }

  override fun run() {
    val players = Bukkit.getServer().getOnlinePlayers()
    for (player in players) {
      val loc = player.location
      var prevLoc = memory[player]
      if (prevLoc != null) {
        if ((loc.x == prevLoc.x) && (loc.y == prevLoc.y) && (loc.z == prevLoc.z) && (loc.world == prevLoc.world)) {
          doAnvil(player)
        }
      }
      memory[player] = loc
    }
  }

  fun doAnvil(player: Player) {
    val loc = player.location
    loc.y += 1
    var maxDistLeft = MAX_ANVIL_HEIGHT
    while ((maxDistLeft > 0) && (loc.getBlock().isEmpty())) {
      maxDistLeft -= 1
      loc.y += 1
    }
    loc.y -= 1
    loc.getBlock().type = Material.ANVIL
  }

  fun register(plugin: Plugin) {
    this.runTaskTimer(plugin, 1L, TICKS_PER_SECOND * 10L)
  }

}
