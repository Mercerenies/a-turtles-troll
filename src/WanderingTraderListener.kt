
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.WanderingTrader
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

import kotlin.random.Random

class WanderingTraderListener(
  val chaosChance: Double = 0.05,
) : AbstractFeature(), Listener {

  companion object {

    val TRADE_COUNT = 8
    val CHAOS_TRADE_COUNT = 32
    val MAX_USES_ON_BUY = 64

    fun maxUsesByRarity(rarity: Rarity): Int =
      when (rarity) {
        Rarity.COMMON -> 64
        Rarity.UNCOMMON -> 16
        Rarity.RARE -> 4
        Rarity.EPIC -> 1
      }

    fun buyTrade(type: Material): MerchantRecipe {
      val recipe = MerchantRecipe(ItemStack(Material.EMERALD, 1), MAX_USES_ON_BUY)
      recipe.addIngredient(ItemStack(type, 1))
      recipe.setExperienceReward(true)
      return recipe
    }

    fun sellTrade(type: Material): MerchantRecipe {
      val recipe = MerchantRecipe(ItemStack(type, 1), maxUsesByRarity(AllItems.getRarity(type)))
      recipe.addIngredient(ItemStack(Material.EMERALD, 1))
      recipe.setExperienceReward(true)
      return recipe
    }

    fun makeTrade(): MerchantRecipe {
      val type = AllItems.sample()
      if (Random.nextDouble() < 0.5) {
        return buyTrade(type)
      } else if (AllItems.getRarity(type) == Rarity.EPIC) {
        // Can't sell epic items; try again
        return makeTrade()
      } else {
        return sellTrade(type)
      }
    }

    // Maximum chaos ^.^
    fun makeTradeChaos(): MerchantRecipe {
      val lhs = AllItems.sample()
      val rhs = AllItems.sample()
      if (AllItems.getRarity(rhs) == Rarity.EPIC) {
        // Can't sell epic items; try again
        return makeTradeChaos()
      } else {
        val recipe = MerchantRecipe(ItemStack(rhs, 1), maxUsesByRarity(AllItems.getRarity(rhs)))
        recipe.addIngredient(ItemStack(lhs, 1))
        recipe.setExperienceReward(true)
        return recipe
      }
    }

  }

  override val name = "trader"

  override val description = "Wandering traders can spawn with any trades in the game"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is WanderingTrader) {
      // Decide if he's a chaos trader or not
      val trades = if (Random.nextDouble() < chaosChance) {
        (1..CHAOS_TRADE_COUNT).map { makeTradeChaos() }
      } else {
        (1..TRADE_COUNT).map { makeTrade() }
      }
      entity.setRecipes(trades)
    }
  }

}
