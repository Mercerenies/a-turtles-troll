
package com.mercerenies.turtletroll.pokeball

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Mob
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

import kotlin.random.Random
import kotlin.math.sqrt

// Pokeball capture mechanics, heavily based on the Gen III/IV capture
// mechanics detailed here
// (https://www.dragonflycave.com/mechanics/gen-iii-iv-capturing#ball-bonus),
// but modified where necessary for Minecraft.
//
// Note that, in the interests of authenticity, this is all done with
// integers, so all division is integer division. This is by design.
object Capture {

  // Captures against these mobs will fail, even if a Master Ball is
  // used. It's simply impossible to capture them.
  private val UNCAPTURABLE: Set<EntityType> =
    setOf(EntityType.WITHER, EntityType.ENDER_DRAGON)

  // X in the formula at the above link.
  fun captureRate(ball: PokeballType, mob: Mob): Int {
    val h = mob.getHealth()
    val m = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.getValue() ?: h
    val c = speciesCaptureRate(mob.getType())
    val b = ball.ballBonus(mob)
    val s = 1.0

    return ((((3 * m - 2 * h) * (c * b)) / (3 * m)).toInt() * s).toInt()
  }

  // Y in the formula at the above link.
  fun repeatCaptureRate(ball: PokeballType, mob: Mob): Int =
    1048560 / sqrt(sqrt((16711680 / captureRate(ball, mob)).toDouble())).toInt()

  // Returns whether the capture was successful.
  fun simulateCapture(ball: PokeballType, mob: Mob, random: Random = Random): Boolean {

    if (UNCAPTURABLE.contains(mob.getType())) {
      // Can't capture boss mobs.
      return false
    }

    if (ball == MasterBall) {
      // Master ball always succeeds.
      return true
    }

    val x = captureRate(ball, mob)
    println("${x} (${ball} ${mob})")
    if (x >= 255) {
      // Automatic success.
      return true
    }

    val y = repeatCaptureRate(ball, mob)
    for (_i in 1..4) {
      if (random.nextInt(65536) >= y) {
        // Broke out of the ball, bail out.
        return false
      }
    }

    // Success.
    return true

  }

  fun getStatusBonus(mob: Mob): Double {
    // In the original algorithm: s = 2.0 if asleep or frozen; s = 1.5
    // if poisoned, paralyzed, or burned, and s = 1.0 otherwise.
    //
    // isIncapacitated is the 2.0 case, which is currently only
    // applied to a mob who has the Wither effect. isStunned is the
    // 1.5 case, which applies to blinding, poison, or other similar
    // effects that loosely correspond to the Pokemon equivalents. 1.0
    // is, of course, the default if neither of the above apply.
    var isIncapacitated = false
    var isStunned = false
    for (effect in mob.getActivePotionEffects()) {
      when (effect.getType()) {
        PotionEffectType.WITHER -> {
          isIncapacitated = true
        }
        PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION,
        PotionEffectType.HARM, PotionEffectType.POISON -> {
          isStunned = true
        }
      }
    }
    if (isIncapacitated) {
      return 2.0
    } else if (isStunned) {
      return 1.5
    } else {
      return 1.0
    }
  }

}
