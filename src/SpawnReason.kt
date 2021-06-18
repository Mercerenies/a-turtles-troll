
package com.mercerenies.turtletroll

import org.bukkit.event.entity.CreatureSpawnEvent

object SpawnReason {

  val NATURAL = setOf(
    CreatureSpawnEvent.SpawnReason.DEFAULT,
    CreatureSpawnEvent.SpawnReason.NATURAL,
  )

  fun isNatural(reason: CreatureSpawnEvent.SpawnReason): Boolean =
    NATURAL.contains(reason)

  fun isNatural(event: CreatureSpawnEvent): Boolean =
    isNatural(event.spawnReason)

}
