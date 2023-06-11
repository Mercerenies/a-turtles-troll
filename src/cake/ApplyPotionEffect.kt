
package com.mercerenies.turtletroll.cake

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Messages

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ApplyPotionEffect(
  override val positivity: Double,
  val effectName: String,
  val potionEffect: PotionEffect,
) : CakeEffect {

  /* ktlint-disable no-multi-spaces comma-spacing paren-spacing */
  companion object {

    val Absorption       = ApplyPotionEffect( 0.85, "Absorption"         , PotionEffect(PotionEffectType.ABSORPTION         , Constants.TICKS_PER_SECOND * 60, 1))
    val DamageResistance = ApplyPotionEffect( 0.85, "Damage Resistance"  , PotionEffect(PotionEffectType.DAMAGE_RESISTANCE  , Constants.TICKS_PER_SECOND * 60, 1))
    val DolphinsGrace    = ApplyPotionEffect( 0.40, "Dolphin's Grace"    , PotionEffect(PotionEffectType.DOLPHINS_GRACE     , Constants.TICKS_PER_SECOND * 60, 1))
    val FastDigging      = ApplyPotionEffect( 0.55, "Fast Digging"       , PotionEffect(PotionEffectType.FAST_DIGGING       , Constants.TICKS_PER_SECOND * 60, 1))
    val FireResistance   = ApplyPotionEffect( 0.80, "Fire Resistance"    , PotionEffect(PotionEffectType.FIRE_RESISTANCE    , Constants.TICKS_PER_SECOND * 60, 1))
    val Heal             = ApplyPotionEffect( 0.75, "Heal"               , PotionEffect(PotionEffectType.HEAL               , Constants.TICKS_PER_SECOND * 60, 1))
    val HealthBoost      = ApplyPotionEffect( 0.75, "Health Boost"       , PotionEffect(PotionEffectType.HEALTH_BOOST       , Constants.TICKS_PER_SECOND * 60, 1))
    val HeroOfTheVillage = ApplyPotionEffect( 0.45, "Hero Of The Village", PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, Constants.TICKS_PER_SECOND * 60, 1))
    val IncreaseDamage   = ApplyPotionEffect( 0.55, "Increase Damage"    , PotionEffect(PotionEffectType.INCREASE_DAMAGE    , Constants.TICKS_PER_SECOND * 60, 1))
    val Invisibility     = ApplyPotionEffect( 0.35, "Invisibility"       , PotionEffect(PotionEffectType.INVISIBILITY       , Constants.TICKS_PER_SECOND * 60, 1))
    val Jump             = ApplyPotionEffect( 0.50, "Jump"               , PotionEffect(PotionEffectType.JUMP               , Constants.TICKS_PER_SECOND * 60, 1))
    val Luck             = ApplyPotionEffect( 0.40, "Luck"               , PotionEffect(PotionEffectType.LUCK               , Constants.TICKS_PER_SECOND * 60, 1))
    val NightVision      = ApplyPotionEffect( 0.65, "Night Vision"       , PotionEffect(PotionEffectType.NIGHT_VISION       , Constants.TICKS_PER_SECOND * 60, 1))
    val Regeneration     = ApplyPotionEffect( 0.90, "Regeneration"       , PotionEffect(PotionEffectType.REGENERATION       , Constants.TICKS_PER_SECOND * 60, 1))
    val Saturation       = ApplyPotionEffect( 0.65, "Saturation"         , PotionEffect(PotionEffectType.SATURATION         , Constants.TICKS_PER_SECOND * 60, 1))
    val SlowFalling      = ApplyPotionEffect( 0.40, "Slow Falling"       , PotionEffect(PotionEffectType.SLOW_FALLING       , Constants.TICKS_PER_SECOND * 60, 1))
    val Speed            = ApplyPotionEffect( 0.50, "Speed"              , PotionEffect(PotionEffectType.SPEED              , Constants.TICKS_PER_SECOND * 60, 1))
    val WaterBreathing   = ApplyPotionEffect( 0.55, "Water Breathing"    , PotionEffect(PotionEffectType.WATER_BREATHING    , Constants.TICKS_PER_SECOND * 60, 1))
    val ConduitPower     = ApplyPotionEffect( 0.80, "Conduit Power"      , PotionEffect(PotionEffectType.CONDUIT_POWER      , Constants.TICKS_PER_SECOND * 60, 1))

    val Glowing          = ApplyPotionEffect( 0.00, "Glowing"            , PotionEffect(PotionEffectType.GLOWING            , Constants.TICKS_PER_SECOND * 60, 1))
    val Levitation       = ApplyPotionEffect( 0.00, "Levitation"         , PotionEffect(PotionEffectType.LEVITATION         , Constants.TICKS_PER_SECOND * 60, 1))

    val BadOmen          = ApplyPotionEffect(-0.45, "Bad Omen"           , PotionEffect(PotionEffectType.BAD_OMEN           , Constants.TICKS_PER_SECOND * 60, 1))
    val Blindness        = ApplyPotionEffect(-0.55, "Blindness"          , PotionEffect(PotionEffectType.BLINDNESS          , Constants.TICKS_PER_SECOND * 60, 1))
    val Confusion        = ApplyPotionEffect(-0.60, "Confusion"          , PotionEffect(PotionEffectType.CONFUSION          , Constants.TICKS_PER_SECOND * 60, 1))
    val Harm             = ApplyPotionEffect(-0.70, "Harm"               , PotionEffect(PotionEffectType.HARM               , Constants.TICKS_PER_SECOND * 60, 1))
    val Hunger           = ApplyPotionEffect(-0.60, "Hunger"             , PotionEffect(PotionEffectType.HUNGER             , Constants.TICKS_PER_SECOND * 60, 1))
    val Poison           = ApplyPotionEffect(-0.80, "Poison"             , PotionEffect(PotionEffectType.POISON             , Constants.TICKS_PER_SECOND * 60, 1))
    val Slow             = ApplyPotionEffect(-0.30, "Slow"               , PotionEffect(PotionEffectType.SLOW               , Constants.TICKS_PER_SECOND * 60, 1))
    val SlowDigging      = ApplyPotionEffect(-0.50, "Slow Digging"       , PotionEffect(PotionEffectType.SLOW_DIGGING       , Constants.TICKS_PER_SECOND * 60, 1))
    val Unluck           = ApplyPotionEffect(-0.40, "Unluck"             , PotionEffect(PotionEffectType.UNLUCK             , Constants.TICKS_PER_SECOND * 60, 1))
    val Weakness         = ApplyPotionEffect(-0.30, "Weakness"           , PotionEffect(PotionEffectType.WEAKNESS           , Constants.TICKS_PER_SECOND * 60, 1))
    val Wither           = ApplyPotionEffect(-0.90, "Wither"             , PotionEffect(PotionEffectType.WITHER             , Constants.TICKS_PER_SECOND * 60, 1))

  }
  /* ktlint-enable no-multi-spaces comma-spacing paren-spacing */

  override fun cancelsDefault(): Boolean = false

  override fun onEat(loc: Location, player: Player) {
    Messages.sendMessage(player, "The cake tastes like ${effectName}!")
    player.addPotionEffect(potionEffect)
  }

}
