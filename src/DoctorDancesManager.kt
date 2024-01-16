
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.*

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.enchantments.Enchantment

import net.kyori.adventure.text.Component

class DoctorDancesManager(
  plugin: Plugin,
  private val rewards: List<Weight<Reward>> = DEFAULT_REWARDS,
) : RunnableFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val LENGTH_OF_DAY = 24000L

    val DEFAULT_REWARDS: List<Weight<Reward>> =
      listOf(
        Weight(
          Reward(
            ItemStack(Material.STONE_PICKAXE).withItemMeta { meta ->
              (meta as Damageable).damage = Material.STONE_PICKAXE.maxDurability - 1
            },
          ),
          1.0,
        ),
        Weight(
          Reward(
            ItemStack(Material.STONE_PICKAXE).withItemMeta { meta ->
              (meta as Damageable).damage = Material.STONE_PICKAXE.maxDurability - 2
              meta.addEnchant(Enchantment.SILK_TOUCH, 1, false)
            },
          ),
          1.0,
        ),
        Weight(Reward(ItemStack(Material.STONE_SHOVEL)), 1.0),
        Weight(Reward(ItemStack(Material.STONE_AXE)), 1.0),
        Weight(Reward(ItemStack(Material.DIAMOND_HOE)), 0.2),
        Weight(Reward(ItemStack(Material.WATER_BUCKET)), 0.3),
        Weight(Reward(ItemStack(Material.EGG, 3)), 0.3),
        Weight(Reward(ItemStack(Material.DIRT, 63)), 0.3),
      )

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(DoctorDancesManager(state.plugin))

  }

  class Reward(val items: List<ItemStack>) {

    constructor(vararg items: ItemStack) : this(items.toList())

    fun toComponent(): Component =
      pluralize(items.map(AllItems::getName))

    fun giveToPlayer(player: Player) {
      for (item in items) {
        // Defensive clone, since addItem can apparently mutate the
        // ItemStack instance (??)
        player.inventory.addItem(item.clone())
      }
    }

  }

  override val name = "doctordances"

  override val description = "If nobody dies for a full Minecraft day (20 mins) then everyone gets a reward"

  override val taskPeriod = 25L * Constants.TICKS_PER_SECOND

  private var lastDeath: Long = ScheduledEventRunnable.getAbsoluteSystemTime()

  private fun resetTimer() {
    lastDeath = ScheduledEventRunnable.getAbsoluteSystemTime()
  }

  private fun rewardPlayer(player: Player, reward: Reward) {
    val message = Component.text("The Doctor dances, and you're rewarded with ")
      .append(reward.toComponent())
    Messages.sendMessage(player, message)
    reward.giveToPlayer(player)
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }

    // If there's no one online, then reset the timer (we aren't
    // rewarding players for being offline)
    if (Bukkit.getOnlinePlayers().isEmpty()) {
      resetTimer()
    }

    // If the time has been hit, reward everyone and reset the timer
    val currentTime = ScheduledEventRunnable.getAbsoluteSystemTime()
    if (currentTime - lastDeath >= LENGTH_OF_DAY) {
      resetTimer()
      val reward = sample(rewards)
      for (player in Bukkit.getOnlinePlayers()) {
        rewardPlayer(player, reward)
      }
    }

  }

  override fun enable() {
    super.enable()
    resetTimer()
  }

  @EventHandler
  @Suppress("UNUSED_PARAMETER")
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }
    resetTimer()
  }

}
