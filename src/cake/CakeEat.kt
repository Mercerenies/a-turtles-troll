
package com.mercerenies.turtletroll.cake

import com.mercerenies.turtletroll.Weight

import org.bukkit.entity.*
import org.bukkit.plugin.Plugin

// Helper for common cake-eating effects
object CakeEat {

  @Suppress("UNUSED_PARAMETER")
  fun defaultEffects(plugin: Plugin) = listOf(
    // Positive
    Weight(NoEffect, 5.0),
    Weight(ApplyPotionEffect.Absorption, 0.20),
    Weight(ApplyPotionEffect.DamageResistance, 0.20),
    Weight(ApplyPotionEffect.DolphinsGrace, 0.20),
    Weight(ApplyPotionEffect.FastDigging, 0.20),
    Weight(ApplyPotionEffect.FireResistance, 0.20),
    Weight(ApplyPotionEffect.Heal, 0.20),
    Weight(ApplyPotionEffect.HealthBoost, 0.20),
    Weight(ApplyPotionEffect.HeroOfTheVillage, 0.20),
    Weight(ApplyPotionEffect.IncreaseDamage, 0.20),
    Weight(ApplyPotionEffect.Invisibility, 0.20),
    Weight(ApplyPotionEffect.Jump, 0.20),
    Weight(ApplyPotionEffect.Luck, 0.20),
    Weight(ApplyPotionEffect.NightVision, 0.20),
    Weight(ApplyPotionEffect.Regeneration, 0.20),
    Weight(ApplyPotionEffect.Saturation, 0.20),
    Weight(ApplyPotionEffect.SlowFalling, 0.20),
    Weight(ApplyPotionEffect.Speed, 0.20),
    Weight(ApplyPotionEffect.WaterBreathing, 0.20),
    Weight(ApplyPotionEffect.ConduitPower, 0.20),
    // Neutral
    Weight(ApplyPotionEffect.Glowing, 0.90),
    Weight(ApplyPotionEffect.Levitation, 0.90),
    // Negative
    Weight(ApplyPotionEffect.BadOmen, 0.32),
    Weight(ApplyPotionEffect.Blindness, 0.32),
    Weight(ApplyPotionEffect.Confusion, 0.32),
    Weight(ApplyPotionEffect.Harm, 0.32),
    Weight(ApplyPotionEffect.Hunger, 0.32),
    Weight(ApplyPotionEffect.Poison, 0.32),
    Weight(ApplyPotionEffect.Slow, 0.32),
    Weight(ApplyPotionEffect.SlowDigging, 0.32),
    Weight(ApplyPotionEffect.Unluck, 0.32),
    Weight(ApplyPotionEffect.Weakness, 0.32),
    Weight(ApplyPotionEffect.Wither, 0.32),

  )

}
