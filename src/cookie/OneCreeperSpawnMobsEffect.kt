
package com.mercerenies.turtletroll.cookie

import org.bukkit.entity.EntityType

object OneCreeperSpawnMobsEffect : SpawnMobsEffect() {

  override fun getMobTypes(): List<EntityType> =
    listOf(EntityType.CREEPER)

  override fun getMessageFor(mobTypes: List<EntityType>): String =
    "That cookie had a creeper inside it!"

}
