
package com.mercerenies.turtletroll.cookie

import org.bukkit.entity.EntityType

object TwoSilverfishSpawnMobsEffect : SpawnMobsEffect() {

  override fun getMobTypes(): List<EntityType> =
    listOf(EntityType.SILVERFISH, EntityType.SILVERFISH)

  override fun getMessageFor(mobTypes: List<EntityType>): String =
    "That cookie had two silverfish inside it!"

}
