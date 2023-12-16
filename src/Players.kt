
package com.mercerenies.turtletroll

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.util.UUID

object Players {

  fun makePlayerMap(): Map<UUID, Player> =
    Bukkit.getOnlinePlayers().associateBy { it.uniqueId }

}
