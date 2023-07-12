
package com.mercerenies.turtletroll

import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.Location
import org.bukkit.Bukkit

object EntitySpawner {

  fun<E : LivingEntity> spawnCreature(
    location: Location,
    entityClass: Class<E>,
    spawnReason: CreatureSpawnEvent.SpawnReason = CreatureSpawnEvent.SpawnReason.CUSTOM,
  ): E? {
    val entity = location.world.spawn(location, entityClass)
    val spawnEvent = CreatureSpawnEvent(entity, spawnReason)
    Bukkit.getPluginManager().callEvent(spawnEvent)
    if (spawnEvent.isCancelled()) {
      entity.health = 0.0
      return null
    }
    return entity
  }

}
