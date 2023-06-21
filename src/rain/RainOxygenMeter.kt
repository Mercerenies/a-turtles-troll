
package com.mercerenies.turtletroll.rain

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.Vanilla

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.GameRule

import net.kyori.adventure.text.Component

class RainOxygenMeter(
  private val player: Player,
  private val deathRegistry: CustomDeathMessageRegistry,
) {

  companion object {
    val MAX_AIR: Int = 20
    val DAMAGE_CAUSE: EntityDamageEvent.DamageCause = EntityDamageEvent.DamageCause.DROWNING
    val DROWNING_DAMAGE_RULE: GameRule<Boolean> = GameRule.DROWNING_DAMAGE
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
    if (player.isInRain()) {
      if (airAmount <= 0) {
        dealDamage()
      } else {
        airAmount -= 1
      }
    } else {
      airAmount += 2
    }
    println("" + player + " " + airAmount)
  }

  private fun dealDamage() {
    if (player.world.getGameRuleValueOrDefault(DROWNING_DAMAGE_RULE)) {
      deathRegistry.withCustomDeathMessage(getCustomDeathMessage()) {
        player.damage(2.0, null)
      }
    }
  }

}
