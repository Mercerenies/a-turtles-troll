
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.util.*

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Inventory
import org.bukkit.potion.PotionEffectType

class StatusEffectStat(
  val effectName: String,
  val effectType: PotionEffectType,
  val multiplier: Double,
) : EncumbranceStat {

  override fun calculateEncumbrance(player: Player): EncumbranceContribution {
    val hasEffect = player.hasPotionEffect(effectType)
    return EncumbranceContribution(
      amount = if (hasEffect) { multiplier } else { 0.0 },
      reason = "from ${effectName}",
    )
  }

}

