
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.util.setBasicCustomModelData

import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ItemStack

import net.kyori.adventure.text.Component

class DripstoneRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  override val name = "dripstonerecipe"

  override val description = "Dripstone can be used in place of flint when making arrows"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> {
    val recipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->

      // Evanski's horrible code here that gives the arrows custom names he can get in the datapack
      val ud_arrow = ItemStack(Material.ARROW, 4)
      val ud_meta = ud_arrow.getItemMeta()!!

      ud_meta.displayName(Component.text("Upside Down Arrow"))
      ud_meta.setBasicCustomModelData(8565)
      ud_arrow.setItemMeta(ud_meta)
      // Thought it would be funnier if the recipe was made upside down aswell

      val recipe = ShapedRecipe(key, ud_arrow)
      recipe.shape(
        "A",
        "B",
        "C",
      )
      recipe.setIngredient('A', Material.FEATHER)
      recipe.setIngredient('B', Material.STICK)
      recipe.setIngredient('C', Material.POINTED_DRIPSTONE)
      recipe
    }
    return listOf(recipe)
  }

}
