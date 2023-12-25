
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.NameSource
import com.mercerenies.turtletroll.storage.FortunesFile

import org.bukkit.Bukkit
import org.bukkit.entity.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffectType

// Helper for common cookie-eating effects
object CookieEat {

  // TODO Effect for "The player gets the effect of some other food item at random"

  fun defaultEffects(plugin: Plugin) = listOf(
    Weight(GivenItemEffect.AnyNonEpicItem(plugin), 2.6),
    Weight(defaultFortuneEffect(), 2.3),
    Weight(GivenItemEffect.AnotherCookie(plugin), 1.8),
    Weight(GivenItemEffect.TwoMoreCookies(plugin), 1.5),
    Weight(NoEffect, 1.3),
    Weight(TwoSilverfishSpawnMobsEffect, 0.7),
    Weight(OneCreeperSpawnMobsEffect, 0.5),
    Weight(TeleportToEffect.ToPlayerSpawn(plugin), 0.45),
    Weight(TeleportToEffect.ToWorldSpawn(plugin), 0.4),
    Weight(GivenItemEffect.TenMoreCookies(plugin), 0.35),
    Weight(fillWithDirtEffect(plugin), 0.3),
    Weight(DeathEffect(plugin), 0.2),
    // Status Effects (sum = 1.0)
    Weight(ApplyPotionEffect("Absorption", PotionEffectType.ABSORPTION), 0.031),
    Weight(ApplyPotionEffect("Damage Resistance", PotionEffectType.DAMAGE_RESISTANCE), 0.031),
    Weight(ApplyPotionEffect("Dolphins Grace", PotionEffectType.DOLPHINS_GRACE), 0.031),
    Weight(ApplyPotionEffect("Fast Digging", PotionEffectType.FAST_DIGGING), 0.031),
    Weight(ApplyPotionEffect("Fire Resistance", PotionEffectType.FIRE_RESISTANCE), 0.031),
    Weight(ApplyPotionEffect("Heal", PotionEffectType.HEAL), 0.031),
    Weight(ApplyPotionEffect("Health Boost", PotionEffectType.HEALTH_BOOST), 0.031),
    Weight(ApplyPotionEffect("Hero Of The Village", PotionEffectType.HERO_OF_THE_VILLAGE), 0.031),
    Weight(ApplyPotionEffect("Increase Damage", PotionEffectType.INCREASE_DAMAGE), 0.031),
    Weight(ApplyPotionEffect("Invisibility", PotionEffectType.INVISIBILITY), 0.031),
    Weight(ApplyPotionEffect("Jump", PotionEffectType.JUMP), 0.031),
    Weight(ApplyPotionEffect("Luck", PotionEffectType.LUCK), 0.031),
    Weight(ApplyPotionEffect("Night Vision", PotionEffectType.NIGHT_VISION), 0.031),
    Weight(ApplyPotionEffect("Regeneration", PotionEffectType.REGENERATION), 0.031),
    Weight(ApplyPotionEffect("Saturation", PotionEffectType.SATURATION), 0.031),
    Weight(ApplyPotionEffect("Slow Falling", PotionEffectType.SLOW_FALLING), 0.031),
    Weight(ApplyPotionEffect("Speed", PotionEffectType.SPEED), 0.031),
    Weight(ApplyPotionEffect("Water Breathing", PotionEffectType.WATER_BREATHING), 0.031),
    Weight(ApplyPotionEffect("Conduit Power", PotionEffectType.CONDUIT_POWER), 0.031),
    Weight(ApplyPotionEffect("Glowing", PotionEffectType.GLOWING), 0.031),
    Weight(ApplyPotionEffect("Levitation", PotionEffectType.LEVITATION), 0.031),
    Weight(ApplyPotionEffect("Bad Omen", PotionEffectType.BAD_OMEN), 0.031),
    Weight(ApplyPotionEffect("Blindness", PotionEffectType.BLINDNESS), 0.031),
    Weight(ApplyPotionEffect("Confusion", PotionEffectType.CONFUSION), 0.031),
    Weight(ApplyPotionEffect("Harm", PotionEffectType.HARM), 0.031),
    Weight(ApplyPotionEffect("Hunger", PotionEffectType.HUNGER), 0.031),
    Weight(ApplyPotionEffect("Poison", PotionEffectType.POISON), 0.031),
    Weight(ApplyPotionEffect("Slow", PotionEffectType.SLOW), 0.031),
    Weight(ApplyPotionEffect("Slow Digging", PotionEffectType.SLOW_DIGGING), 0.031),
    Weight(ApplyPotionEffect("Unluck", PotionEffectType.UNLUCK), 0.031),
    Weight(ApplyPotionEffect("Weakness", PotionEffectType.WEAKNESS), 0.031),
    Weight(ApplyPotionEffect("Wither", PotionEffectType.WITHER), 0.031),
  )

  private fun defaultFortuneEffect(): FortuneEffect {
    val authors = NameSource.FromList(FortuneEffect.DEFAULT_AUTHORS)
    val fortunes = try {
      FortunesFile("/data/fortunes.txt")
    } catch (e: IllegalArgumentException) {
      Bukkit.getLogger().severe("Failed to load fortunes.txt file! ${e}")
      NameSource.FromList(listOf(""))
    }
    return FortuneEffect(authors, fortunes)
  }

  private fun fillWithDirtEffect(plugin: Plugin): FillInventorySlotsEffect =
    FillInventorySlotsEffect.Constant(
      plugin,
      "That cookie was filled with dirt!",
      ItemStack(Material.DIRT, 64),
      replacesAll = false,
    )

  private fun fillAllWithDirtEffect(plugin: Plugin): FillInventorySlotsEffect =
    FillInventorySlotsEffect.Constant(
      plugin,
      "That cookie was filled with far too much dirt!",
      ItemStack(Material.DIRT, 64),
      replacesAll = true,
    )

}
