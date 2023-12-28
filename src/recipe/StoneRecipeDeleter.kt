
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RecipeDeleterContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Server
import org.bukkit.Material
import org.bukkit.Bukkit

class StoneRecipeDeleter(private val server: Server) : RecipeDeleter(server, STONE_TOOLS) {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val STONE_TOOLS = listOf(
      Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
      Material.STONE_AXE, Material.STONE_SHOVEL
    )

    override fun create(state: BuilderState): FeatureContainer =
      RecipeDeleterContainer(StoneRecipeDeleter(Bukkit.getServer()))

  }

  override val name = "stonetools"

  override val description = "Disables crafting of all stone tools"

}
