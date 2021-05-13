
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
import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.transformed.GhastSpawnerListener
import com.mercerenies.turtletroll.transformed.RavagerSpawnerListener

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack
import org.bukkit.event.Listener

import kotlin.collections.Iterable

class AllPluginListeners(val plugin: Plugin) : Iterable<Listener> {
  val breakListener = BlockBreakEventListener(BREAK_OVERRIDES, BREAK_EVENTS)
  val chickenListener = ChickenDamageListener()
  val grassListener = GrassPoisonListener()
  val snowListener = SnowListener()
  val ghastListener = GhastSpawnerListener()
  val ravagerListener = RavagerSpawnerListener()
  val skeleListener = SkeletonWitherListener()
  val electricListener = ElectricWaterListener(plugin)
  val blazeListener = BlazeAttackListener(plugin)
  val zombifyListener = ZombifyTradeListener()
  val leavesListener = LeavesFireListener(plugin)

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

  fun getListeners(): List<Listener> =
    listOf(
      breakListener, chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener, electricListener,
      blazeListener, zombifyListener, leavesListener
    )

  override fun iterator(): Iterator<Listener> =
    getListeners().iterator()

}
