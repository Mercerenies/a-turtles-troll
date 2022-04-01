
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Server
import org.bukkit.Material


class StoneRecipeDeleter(private val server: Server) : RecipeDeleter(server, STONE_TOOLS) {

  companion object {
    val STONE_TOOLS = listOf(
      Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
      Material.STONE_AXE, Material.STONE_SHOVEL
    )
  }

  override val name = "stonetools"

  override val description = "Disables crafting of all stone tools"

}
