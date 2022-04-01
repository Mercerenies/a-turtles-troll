
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Server
import org.bukkit.Material


class MelonRecipeDeleter(private val server: Server) : RecipeDeleter(server, listOf(Material.MELON_SEEDS)) {

  override val name = "melondeleter"

  override val description = "Disables crafting melon seeds"

}
