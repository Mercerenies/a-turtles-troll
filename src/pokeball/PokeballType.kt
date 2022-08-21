
package com.mercerenies.turtletroll.pokeball

import org.bukkit.entity.Mob

interface PokeballType {

  companion object {

    private val VALUES: List<PokeballType> =
      listOf(NormalPokeball)

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

}

object NormalPokeball : PokeballType {

  override fun ballBonus(target: Mob): Double =
    1.0

}

fun PokeballType.toInt(): Int =
  PokeballType.toInt(this)
