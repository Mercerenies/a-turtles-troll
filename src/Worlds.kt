
package com.mercerenies.turtletroll

import org.bukkit.Bukkit
import org.bukkit.World

object Worlds {

  fun getOverworld(): World? =
    Bukkit.getServer().getWorlds().find { it.environment == World.Environment.NORMAL }

  // For lack of a better place to put this right now...
  fun getSystemTime(): Long {
    for (world in Bukkit.getServer().getWorlds()) {
      if (world.environment == World.Environment.NORMAL) {
        return world.getTime()
      }
    }
    return 0L // That's not good :(
  }

}
