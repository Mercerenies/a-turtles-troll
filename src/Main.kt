
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.anvil.AnvilRunnable

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
  val recipeDeleter = RecipeDeleter(*STONE_TOOLS)
  val anvilRunnable = AnvilRunnable()
  val listenerManager = AllPluginListeners(this)

  companion object {

    val STONE_TOOLS = arrayOf(Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
                              Material.STONE_AXE, Material.STONE_SHOVEL)

  }

  override fun onEnable() {
    val server = Bukkit.getServer()
    for (listener in listenerManager) {
      Bukkit.getPluginManager().registerEvents(listener, this)
    }
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
