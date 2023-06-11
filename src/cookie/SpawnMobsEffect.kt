
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.Messages

import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

import kotlin.random.Random

abstract class SpawnMobsEffect : CookieEffect {

  abstract fun getMobTypes(): List<EntityType>

  abstract fun getMessageFor(mobTypes: List<EntityType>): String

  open fun onSpawnedMob(entity: Entity) {}

  open override fun cancelsDefault(): Boolean = false

  override fun onEat(action: CookieEatenAction) {
    val player = action.player
    val world = player.world
    val mobTypes = getMobTypes()
    val message = getMessageFor(mobTypes)
    val targetLocation = player.getLocation().add(0.0, 1.0, 0.0)
    for (mobType in mobTypes) {
      val thisLocation = targetLocation.clone().add(Random.nextDouble(-1.0, 1.0), Random.nextDouble(0.0, 1.0), Random.nextDouble(-1.0, 1.0))
      val entity = world.spawnEntity(thisLocation, mobType)
      this.onSpawnedMob(entity)
    }
    Messages.sendMessage(player, message)
  }

}
