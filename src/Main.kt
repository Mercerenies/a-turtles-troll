
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.drop.BlockBreakEventListener
import com.mercerenies.turtletroll.drop.NullAction
import com.mercerenies.turtletroll.drop.ReplaceDropsAction
import com.mercerenies.turtletroll.drop.CancelDropAction
import com.mercerenies.turtletroll.drop.EndermiteSpawnAction
import com.mercerenies.turtletroll.drop.filter
import com.mercerenies.turtletroll.drop.nearby.SilverfishAttackAction
import com.mercerenies.turtletroll.drop.nearby.BeeAttackAction
import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.anvil.AnvilRunnable
import com.mercerenies.turtletroll.transformed.GhastSpawnerListener
import com.mercerenies.turtletroll.transformed.RavagerSpawnerListener

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ItemStack

class Main : JavaPlugin() {
  val breakListener = BlockBreakEventListener(BREAK_OVERRIDES, BREAK_EVENTS)
  val chickenListener = ChickenDamageListener()
  val recipeDeleter = RecipeDeleter(*STONE_TOOLS)
  val anvilRunnable = AnvilRunnable()
  val grassListener = GrassPoisonListener()
  val ghastListener = GhastSpawnerListener()
  val ravagerListener = RavagerSpawnerListener()
  val skeleListener = SkeletonWitherListener()
  val electricListener = ElectricWaterListener(this)
  val zombifyListener = ZombifyTradeListener()
  val leavesListener = LeavesFireListener(this)

  companion object {

    val STONE_TOOLS = arrayOf(Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
                              Material.STONE_AXE, Material.STONE_SHOVEL)

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

  override fun onEnable() {
    Bukkit.getPluginManager().registerEvents(breakListener, this)
    Bukkit.getPluginManager().registerEvents(chickenListener, this)
    Bukkit.getPluginManager().registerEvents(grassListener, this)
    Bukkit.getPluginManager().registerEvents(ghastListener, this)
    Bukkit.getPluginManager().registerEvents(ravagerListener, this)
    Bukkit.getPluginManager().registerEvents(skeleListener, this)
    Bukkit.getPluginManager().registerEvents(electricListener, this)
    Bukkit.getPluginManager().registerEvents(zombifyListener, this)
    Bukkit.getPluginManager().registerEvents(leavesListener, this)
    val server = Bukkit.getServer()
    recipeDeleter.removeRecipes(server)
    anvilRunnable.register(this)
  }

  override fun onDisable() {
    recipeDeleter.addRecipes(server)
    try {
      anvilRunnable.cancel()
    } catch (_: IllegalStateException) {}
  }

}
