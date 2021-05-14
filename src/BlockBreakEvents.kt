
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.drop.BlockBreakEventListener
import com.mercerenies.turtletroll.drop.NullAction
import com.mercerenies.turtletroll.drop.ReplaceDropsAction
import com.mercerenies.turtletroll.drop.CancelDropAction
import com.mercerenies.turtletroll.drop.EndermiteSpawnAction
import com.mercerenies.turtletroll.drop.filter
import com.mercerenies.turtletroll.drop.nearby.SilverfishAttackAction
import com.mercerenies.turtletroll.drop.nearby.BeeAttackAction
import com.mercerenies.turtletroll.drop.nearby.NetherrackBoomAction
import com.mercerenies.turtletroll.feature.Feature

import org.bukkit.inventory.ItemStack
import org.bukkit.event.Listener
import org.bukkit.Material

class BlockBreakEvents {

  val listener = BlockBreakEventListener(BREAK_OVERRIDES, BREAK_EVENTS)

  companion object {

    val NO_DROP_ON = arrayOf(Material.CRAFTING_TABLE, Material.FURNACE,
                             Material.SMOKER, Material.BLAST_FURNACE)

    val FREQUENT_DIRT_DROP_TRIGGERS = setOf(
      Material.COAL_ORE, Material.IRON_ORE, Material.LAPIS_ORE,
      Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
      Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS
    )

    val REGULAR_DIRT_DROP = ReplaceDropsAction(ItemStack(Material.DIRT, 64))
    val FREQUENT_DIRT_DROP = REGULAR_DIRT_DROP.filter { FREQUENT_DIRT_DROP_TRIGGERS.contains(it.block.type) }

    /*
    val DIRT_DROP_FEATURE = CompositeFeature(
      "dirtstacks",
      "Replaces drops with dirt stacks at random",
      listOf(REGULAR_DIRT_DROP, FREQUENT_DIRT_DROP),
    )
    */

    val BREAK_OVERRIDES = listOf(
      EndermiteSpawnAction,
      NetherrackBoomAction(),
      CancelDropAction.filter { NO_DROP_ON.contains(it.block.type) },
    )

    val BREAK_EVENTS = listOf(
      Weight(NullAction, 1.0),
      Weight(REGULAR_DIRT_DROP, 0.3),
      Weight(FREQUENT_DIRT_DROP, 1.0),
      Weight(SilverfishAttackAction(), 0.2),
      Weight(BeeAttackAction(), 0.2),
    )

  }

  fun getFeatures(): List<Feature> = listOf()

}
