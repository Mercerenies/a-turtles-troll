
package com.mercerenies.turtletroll.cookie

import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

import kotlin.random.Random

object OneCreeperSpawnMobsEffect : SpawnMobsEffect() {

  override fun getMobTypes(): List<EntityType> =
    listOf(EntityType.CREEPER)

  override fun getMessageFor(mobTypes: List<EntityType>): String =
    "That cookie had a creeper inside it!"

}
