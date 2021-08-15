
package com.mercerenies.turtletroll.cake

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ApplyPotionEffect(
  override val positivity: Double,
  val effectName: String,
  val potionEffect: PotionEffect,
) : CakeEffect {

  companion object {
    val TICKS_PER_SECOND = 20

    val Absorption       = ApplyPotionEffect( 0.85, "Absorption"         , PotionEffect(PotionEffectType.ABSORPTION         , TICKS_PER_SECOND * 60, 1));
    val DamageResistance = ApplyPotionEffect( 0.85, "Damage Resistance"  , PotionEffect(PotionEffectType.DAMAGE_RESISTANCE  , TICKS_PER_SECOND * 60, 1));
    val DolphinsGrace    = ApplyPotionEffect( 0.40, "Dolphin's Grace"    , PotionEffect(PotionEffectType.DOLPHINS_GRACE     , TICKS_PER_SECOND * 60, 1));
    val FastDigging      = ApplyPotionEffect( 0.55, "Fast Digging"       , PotionEffect(PotionEffectType.FAST_DIGGING       , TICKS_PER_SECOND * 60, 1));
    val FireResistance   = ApplyPotionEffect( 0.80, "Fire Resistance"    , PotionEffect(PotionEffectType.FIRE_RESISTANCE    , TICKS_PER_SECOND * 60, 1));
    val Heal             = ApplyPotionEffect( 0.75, "Heal"               , PotionEffect(PotionEffectType.HEAL               , TICKS_PER_SECOND * 60, 1));
    val HealthBoost      = ApplyPotionEffect( 0.75, "Health Boost"       , PotionEffect(PotionEffectType.HEALTH_BOOST       , TICKS_PER_SECOND * 60, 1));
    val HeroOfTheVillage = ApplyPotionEffect( 0.45, "Hero Of The Village", PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, TICKS_PER_SECOND * 60, 1));
    val IncreaseDamage   = ApplyPotionEffect( 0.55, "Increase Damage"    , PotionEffect(PotionEffectType.INCREASE_DAMAGE    , TICKS_PER_SECOND * 60, 1));
    val Invisibility     = ApplyPotionEffect( 0.35, "Invisibility"       , PotionEffect(PotionEffectType.INVISIBILITY       , TICKS_PER_SECOND * 60, 1));
    val Jump             = ApplyPotionEffect( 0.50, "Jump"               , PotionEffect(PotionEffectType.JUMP               , TICKS_PER_SECOND * 60, 1));
    val Luck             = ApplyPotionEffect( 0.40, "Luck"               , PotionEffect(PotionEffectType.LUCK               , TICKS_PER_SECOND * 60, 1));
    val NightVision      = ApplyPotionEffect( 0.65, "Night Vision"       , PotionEffect(PotionEffectType.NIGHT_VISION       , TICKS_PER_SECOND * 60, 1));
    val Regeneration     = ApplyPotionEffect( 0.90, "Regeneration"       , PotionEffect(PotionEffectType.REGENERATION       , TICKS_PER_SECOND * 60, 1));
    val Saturation       = ApplyPotionEffect( 0.65, "Saturation"         , PotionEffect(PotionEffectType.SATURATION         , TICKS_PER_SECOND * 60, 1));
    val SlowFalling      = ApplyPotionEffect( 0.40, "Slow Falling"       , PotionEffect(PotionEffectType.SLOW_FALLING       , TICKS_PER_SECOND * 60, 1));
    val Speed            = ApplyPotionEffect( 0.50, "Speed"              , PotionEffect(PotionEffectType.SPEED              , TICKS_PER_SECOND * 60, 1));
    val WaterBreathing   = ApplyPotionEffect( 0.55, "Water Breathing"    , PotionEffect(PotionEffectType.WATER_BREATHING    , TICKS_PER_SECOND * 60, 1));
    val ConduitPower     = ApplyPotionEffect( 0.80, "Conduit Power"      , PotionEffect(PotionEffectType.CONDUIT_POWER      , TICKS_PER_SECOND * 60, 1));

    val Glowing          = ApplyPotionEffect( 0.00, "Glowing"            , PotionEffect(PotionEffectType.GLOWING            , TICKS_PER_SECOND * 60, 1));
    val Levitation       = ApplyPotionEffect( 0.00, "Levitation"         , PotionEffect(PotionEffectType.LEVITATION         , TICKS_PER_SECOND * 60, 1));

    val BadOmen          = ApplyPotionEffect(-0.45, "Bad Omen"           , PotionEffect(PotionEffectType.BAD_OMEN           , TICKS_PER_SECOND * 60, 1));
    val Blindness        = ApplyPotionEffect(-0.55, "Blindness"          , PotionEffect(PotionEffectType.BLINDNESS          , TICKS_PER_SECOND * 60, 1));
    val Confusion        = ApplyPotionEffect(-0.60, "Confusion"          , PotionEffect(PotionEffectType.CONFUSION          , TICKS_PER_SECOND * 60, 1));
    val Harm             = ApplyPotionEffect(-0.70, "Harm"               , PotionEffect(PotionEffectType.HARM               , TICKS_PER_SECOND * 60, 1));
    val Hunger           = ApplyPotionEffect(-0.60, "Hunger"             , PotionEffect(PotionEffectType.HUNGER             , TICKS_PER_SECOND * 60, 1));
    val Poison           = ApplyPotionEffect(-0.80, "Poison"             , PotionEffect(PotionEffectType.POISON             , TICKS_PER_SECOND * 60, 1));
    val Slow             = ApplyPotionEffect(-0.30, "Slow"               , PotionEffect(PotionEffectType.SLOW               , TICKS_PER_SECOND * 60, 1));
    val SlowDigging      = ApplyPotionEffect(-0.50, "Slow Digging"       , PotionEffect(PotionEffectType.SLOW_DIGGING       , TICKS_PER_SECOND * 60, 1));
    val Unluck           = ApplyPotionEffect(-0.40, "Unluck"             , PotionEffect(PotionEffectType.UNLUCK             , TICKS_PER_SECOND * 60, 1));
    val Weakness         = ApplyPotionEffect(-0.30, "Weakness"           , PotionEffect(PotionEffectType.WEAKNESS           , TICKS_PER_SECOND * 60, 1));
    val Wither           = ApplyPotionEffect(-0.90, "Wither"             , PotionEffect(PotionEffectType.WITHER             , TICKS_PER_SECOND * 60, 1));

  }

  override fun cancelsDefault(): Boolean = false

  override fun onCakeEat(loc: Location, player: Player) {
    player.sendMessage("The cake tastes like ${effectName}!")
    player.addPotionEffect(potionEffect)
  }

}
