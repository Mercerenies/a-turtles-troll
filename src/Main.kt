
package com.mercerenies.turtletroll

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.inventory.Recipe

import kotlin.collections.ArrayList

class Main : JavaPlugin() {
  val listener = EventListener()
  val recipe_deleter = RecipeDeleter(*STONE_TOOLS)

  companion object {
    val STONE_TOOLS = arrayOf(Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
                              Material.STONE_AXE, Material.STONE_SHOVEL)
  }

  override fun onEnable() {
    Bukkit.getPluginManager().registerEvents(listener, this)
    val server = Bukkit.getServer()
    recipe_deleter.removeRecipes(server)
  }

  override fun onDisable() {
    recipe_deleter.addRecipes(server)
  }

}
