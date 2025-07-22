
package com.mercerenies.turtletroll.pot

import com.mercerenies.turtletroll.util.EventUtils
import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.drop.Positivity

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Item
import org.bukkit.entity.EntityType

import kotlin.random.Random

class PotteryBreakAction(
    private val potteryFeature: HasEnabledStatus,
    private val potIdentifier: PotteryIdentifier,
) : BlockBreakAction {

  override val positivity = Positivity.POSITIVE

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
      potteryFeature.isEnabled() && potIdentifier.isPottery(event.block)

  override fun trigger(event: BlockBreakEvent) {
    val w = event.block.world
    val loc = event.block.location.add(0.5, 0.5, 0.5)

    event.block.type = Material.AIR
    event.setCancelled(true)

    val item = w.spawnEntity(loc, EntityType.ITEM) as Item
    item.itemStack = ItemStack(Material.EMERALD, 1)
  }
}
