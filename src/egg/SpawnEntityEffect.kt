
package com.mercerenies.turtletroll.egg

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Ageable
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

import kotlin.reflect.KClass
import kotlin.random.Random

class SpawnEntityEffect<T : Entity>(
  val entityType: KClass<T>,
  val onResultingEntity: (T) -> Unit = {},
) : EggHatchEffect {

  override fun onEggHatch(loc: Location) {
    val entity = loc.world!!.spawn(loc, entityType.java)
    onResultingEntity(entity)
  }

  fun andThen(onResultingEntity2: (T) -> Unit): SpawnEntityEffect<T> =
    SpawnEntityEffect(entityType) { ent ->
      onResultingEntity(ent)
      onResultingEntity2(ent)
    }

}

fun<T : Ageable> SpawnEntityEffect<T>.maybeBaby(chance: Double): SpawnEntityEffect<T> =
  this.andThen { entity ->
    if (Random.nextDouble() < chance) {
      entity.setBaby()
    }
  }

fun<T : LivingEntity> SpawnEntityEffect<T>.withHelmet(helmet: Material, chance: Double): SpawnEntityEffect<T> =
  this.andThen { entity ->
    if (Random.nextDouble() < chance) {
      entity.equipment?.helmet = ItemStack(helmet)
    }
  }
