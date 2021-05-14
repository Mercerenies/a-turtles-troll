
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.ext.*;
import com.mercerenies.turtletroll.feature.Feature

import org.bukkit.Server
import org.bukkit.Material

import kotlin.collections.ArrayList

class StoneRecipeDeleter(private val server: Server) : RecipeDeleter(server, STONE_TOOLS) {

  companion object {
    val STONE_TOOLS = listOf(
      Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
      Material.STONE_AXE, Material.STONE_SHOVEL
    )
  }

  override fun name(): String = "stonetools"

  override fun description() = "Disables crafting of all stone tools"

}
