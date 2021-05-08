
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.drop.BlockBreakEventListener
import com.mercerenies.turtletroll.drop.NullAction
import com.mercerenies.turtletroll.drop.ReplaceDropsAction
import com.mercerenies.turtletroll.drop.SilverfishAttackAction
import com.mercerenies.turtletroll.drop.Weight

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ItemStack

import kotlin.collections.ArrayList

class Main : JavaPlugin() {
  val breakListener = BlockBreakEventListener(BREAK_EVENTS)
  val recipeDeleter = RecipeDeleter(*STONE_TOOLS)

  companion object {

    val STONE_TOOLS = arrayOf(Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
                              Material.STONE_AXE, Material.STONE_SHOVEL)

    val BREAK_EVENTS = listOf(
      Weight(NullAction, 1.0),
      Weight(ReplaceDropsAction(ItemStack(Material.DIRT, 64)), 1.0),
      Weight(SilverfishAttackAction(), 1.0),
    )

  }

  override fun onEnable() {
    Bukkit.getPluginManager().registerEvents(breakListener, this)
    val server = Bukkit.getServer()
    recipeDeleter.removeRecipes(server)
  }

  override fun onDisable() {
    recipeDeleter.addRecipes(server)
  }

}
