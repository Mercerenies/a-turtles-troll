
package com.mercerenies.turtletroll.recipe


import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class DripstoneRecipeFeature(plugin: Plugin) : RecipeFeature(plugin) {

  override val name = "dripstonerecipe"

  override val description = "Dripstone can be used in place of flint when making arrows"

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> {
    val recipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->

      //Evanski's horrible code here that gives the arrows custom names he can get in the datapack
      val ud_arrow = ItemStack(Material.ARROW, 4)
      val ud_meta = ud_arrow.getItemMeta()!!

      ud_meta.setDisplayName("Upside Down Arrow")
      ud_meta.setCustomModelData(8565)
      ud_arrow.setItemMeta(ud_meta)
      //code continues as normal

      val recipe = ShapedRecipe(key, ud_arrow)
      recipe.shape("A",
                   "B",
                   "C")
      recipe.setIngredient('A', Material.POINTED_DRIPSTONE)
      recipe.setIngredient('B', Material.STICK)
      recipe.setIngredient('C', Material.FEATHER)
      recipe
    }
    return listOf(recipe)
  }

}
