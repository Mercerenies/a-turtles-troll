
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.drop.BlockBreakEventListener
import com.mercerenies.turtletroll.drop.NullAction
import com.mercerenies.turtletroll.drop.ReplaceDropsAction
import com.mercerenies.turtletroll.drop.CancelDropAction
import com.mercerenies.turtletroll.drop.filter
import com.mercerenies.turtletroll.drop.nearby.SilverfishAttackAction
import com.mercerenies.turtletroll.drop.nearby.BeeAttackAction
import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.anvil.AnvilRunnable

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
  val skeleListener = SkeletonWitherListener()
  val electricListener = ElectricWaterListener(this)

  companion object {

    val STONE_TOOLS = arrayOf(Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
                              Material.STONE_AXE, Material.STONE_SHOVEL)

    val NO_DROP_ON = arrayOf(Material.CRAFTING_TABLE, Material.FURNACE,
                             Material.SMOKER, Material.BLAST_FURNACE)

    val BREAK_OVERRIDES = listOf(
      CancelDropAction.filter { NO_DROP_ON.contains(it.block.type) },
    )

    val BREAK_EVENTS = listOf(
      Weight(NullAction, 1.0),
      Weight(ReplaceDropsAction(ItemStack(Material.DIRT, 64)), 1.0),
      Weight(SilverfishAttackAction(), 1.0),
      Weight(BeeAttackAction(), 1.0),
    )

  }

  override fun onEnable() {
    Bukkit.getPluginManager().registerEvents(breakListener, this)
    Bukkit.getPluginManager().registerEvents(chickenListener, this)
    Bukkit.getPluginManager().registerEvents(grassListener, this)
    Bukkit.getPluginManager().registerEvents(ghastListener, this)
    Bukkit.getPluginManager().registerEvents(skeleListener, this)
    Bukkit.getPluginManager().registerEvents(electricListener, this)
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
