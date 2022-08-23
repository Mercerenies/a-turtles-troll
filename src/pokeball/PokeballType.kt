
package com.mercerenies.turtletroll.pokeball

import org.bukkit.World
import org.bukkit.Material
import org.bukkit.entity.Mob
import org.bukkit.entity.EntityType

interface PokeballType {

  companion object {

    private val VALUES: List<PokeballType> =
      listOf(NormalPokeball, GreatBall, UltraBall, MasterBall, NetBall, DiveBall, DuskBall)

    fun toInt(type: PokeballType): Int {
      val idx = VALUES.indexOf(type)
      if (idx < 0) {
        return 0
      } else {
        return idx
      }
    }

    fun fromInt(n: Int): PokeballType =
      // Fall back to NormalPokeball if given invalid input
      VALUES.getOrElse(n) { NormalPokeball }

  }

  fun ballBonus(target: Mob): Double

  val customModelId: Int

}

object NormalPokeball : PokeballType {

  override fun ballBonus(target: Mob): Double =
    1.0

  override val customModelId: Int = 1

}

object GreatBall : PokeballType {

  override fun ballBonus(target: Mob): Double =
    1.5

  override val customModelId: Int = 2

}

object UltraBall : PokeballType {

  override fun ballBonus(target: Mob): Double =
    2.0

  override val customModelId: Int = 3

}

object MasterBall : PokeballType {

  override fun ballBonus(target: Mob): Double =
    99999.0

  override val customModelId: Int = 4

}

object NetBall : PokeballType {

  private val BUG_MOBS: Set<EntityType> =
    setOf(
      EntityType.BEE, EntityType.CAVE_SPIDER, EntityType.ENDERMITE, EntityType.SILVERFISH,
      EntityType.SPIDER,
    )

  private val WATER_MOBS: Set<EntityType> =
    setOf(
      // TODO Add frogs and tadpoles in 1.19
      EntityType.AXOLOTL, EntityType.COD, EntityType.DOLPHIN, EntityType.DROWNED,
      EntityType.SQUID, EntityType.GLOW_SQUID, EntityType.COD, EntityType.SALMON,
      EntityType.PUFFERFISH, EntityType.TROPICAL_FISH, EntityType.GUARDIAN,
      EntityType.ELDER_GUARDIAN, EntityType.TURTLE,
    )

  override fun ballBonus(target: Mob): Double =
    if ((WATER_MOBS.contains(target.type)) || (BUG_MOBS.contains(target.type))) {
      3.0
    } else {
      1.0
    }

  override val customModelId: Int = 5

}

object DiveBall : PokeballType {

  override fun ballBonus(target: Mob): Double =
    if (target.location.block.type == Material.WATER) {
      3.5
    } else {
      1.0
    }

  override val customModelId: Int = 6

}

object DuskBall : PokeballType {

  private fun isNight(world: World): Boolean =
    world.environment == World.Environment.NORMAL &&
      ((world.time < 0L) || (world.time > 12000L))

  override fun ballBonus(target: Mob): Double =
    if (isNight(target.world)) {
      3.5
    } else {
      1.0
    }

  override val customModelId: Int = 7

}

fun PokeballType.toInt(): Int =
  PokeballType.toInt(this)
