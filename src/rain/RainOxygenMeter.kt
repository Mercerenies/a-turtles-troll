
package com.mercerenies.turtletroll.rain

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.Hats
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Vanilla

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffectType
import org.bukkit.enchantments.Enchantment
import org.bukkit.GameRule

import net.kyori.adventure.text.Component

import kotlin.random.Random

class RainOxygenMeter(
  private val player: Player,
  private val deathRegistry: CustomDeathMessageRegistry,
) {

  companion object {
    val MAX_AIR: Int = 20
    val DAMAGE_CAUSE: EntityDamageEvent.DamageCause = EntityDamageEvent.DamageCause.DROWNING
    val DROWNING_DAMAGE_RULE: GameRule<Boolean> = GameRule.DROWNING_DAMAGE

    fun getHudBubbleValue(bubbleCount: Int): Int =
      if (bubbleCount == 10) {
        300
      } else {
        30 * (bubbleCount - 1) + 3
      }

    fun isInRain(player: Player): Boolean {
      // If we're wearing an umbrella hat, then we're safe from the rain.
      val playerHat = player.inventory.helmet?.let(Hats::getCustomHatName)
      if (playerHat == Hats.UMBRELLA_HAT_NAME) {
        return false
      } else {
        // Delegate to vanilla Minecraft logic.
        return player.isInRain()
      }
    }

  }

  private var airAmount: Int = MAX_AIR
    set(value) {
      field = clamp(value, 0, MAX_AIR)
    }

  private fun getCustomDeathMessage(): CustomDeathMessage =
    CustomDeathMessage(
      Vanilla(EntityDamageEvent.DamageCause.DROWNING),
      Component.text("").append(player.displayName()).append(" drowned."),
    )

  fun fullyReplenish() {
    airAmount = MAX_AIR
  }

  fun runTick() {
    if (isInRain(player)) {
      if (airAmount <= 0) {
        dealDamage()
      } else {
        depleteOxygen()
      }
    } else {
      airAmount += 2
    }
  }

  fun getAirFraction(): Double =
    if (airAmount < MAX_AIR) {
      // Subtracting 0.25 here seems to make the UI less flickery for
      // whatever reason. It's still flickery, mind, just less so.
      (airAmount.toDouble() - 0.25) / MAX_AIR
    } else {
      1.0
    }

  private fun hasWaterBreathing(): Boolean =
    player.getPotionEffect(PotionEffectType.WATER_BREATHING) != null

  private fun getRespirationLevel(): Int {
    val helmet = player.equipment.helmet
    if (helmet != null) {
      return helmet.getEnchantmentLevel(Enchantment.RESPIRATION)
    } else {
      return 0
    }
  }

  private fun depleteOxygen() {
    if (hasWaterBreathing()) {
      // Totally immune; skip
      return
    }
    val respirationLevel = getRespirationLevel().toDouble()
    if (Random.nextDouble() < respirationLevel / (respirationLevel + 1)) {
      // Got the respiration tick; skip
      return
    }
    airAmount -= 1
  }

  private fun dealDamage() {
    if (player.world.getGameRuleValueOrDefault(DROWNING_DAMAGE_RULE)) {
      deathRegistry.withCustomDeathMessage(getCustomDeathMessage()) {
        player.damage(2.0, null)
      }
    }
  }

}
