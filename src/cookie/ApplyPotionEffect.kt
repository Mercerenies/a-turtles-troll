
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Messages

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ApplyPotionEffect(
  val effectName: String,
  val potionEffect: PotionEffect,
) : CookieEffect {

  constructor(effectName: String, effectType: PotionEffectType, ticks: Int, level: Int) :
    this(effectName, PotionEffect(effectType, ticks, level))

  constructor(effectName: String, effectType: PotionEffectType) :
    this(effectName, effectType, Constants.TICKS_PER_SECOND * 60, 1)

  override fun cancelsDefault(): Boolean = false

  override fun onEat(action: CookieEatenAction) {
    val player = action.player
    Messages.sendMessage(player, "The cookie tastes like ${effectName}!")
    player.addPotionEffect(potionEffect)
  }

}
