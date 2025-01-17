
package com.mercerenies.turtletroll.candy

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions

import org.bukkit.plugin.Plugin
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

import net.kyori.adventure.text.Component

class CandyShopManager(
  plugin: Plugin,
) : RecipeFeature(plugin), Listener {
  companion object : FeatureContainerFactory<FeatureContainer> {
    val COIN_CONSUMER_KEY = "com.mercerenies.turtletroll.candy.CandyShopManager.COIN_CONSUMER_KEY"
    val RECIPE_MARKER_KEY = "candy_shop_key_tag"
    val CUSTOM_MODEL_ID = 1

    fun getMarkerKey(plugin: Plugin): NamespacedKey =
      NamespacedKey(plugin, RECIPE_MARKER_KEY)

    override fun create(state: BuilderState): FeatureContainer {
      val manager = CandyShopManager(state.plugin)
      state.registerSharedData(
        COIN_CONSUMER_KEY,
        CandyShopCoinConsumer(manager.coinStore).boundToFeature(manager),
      )
      return FeatureBuilder()
        .addFeature(manager)
        .addListener(manager)
        .addGameModification(manager)
        .addCommand("coin" to manager.queryCommand.withPermission(Permissions.COIN))
        .build()
    }
  }

  override val name = "candyshop"

  override val description = "Coins can be collected and spent at the Candy Shop"

  val markerKey = getMarkerKey(plugin)
  val coinStore: CoinStore = MetadataCoinStore(plugin)
  val queryCommand = CoinQueryCommand(coinStore, this)

  private fun makeCandyShopKey(amount: Int = 1): ItemStack {
    val result = ItemStack(Material.WOODEN_HOE, amount)
    val meta = result.itemMeta!!
    meta.displayName(Component.text("Candy Shop Key"))
    meta.persistentDataContainer.set(markerKey, PersistentDataType.BOOLEAN, true)
    meta.setCustomModelData(CUSTOM_MODEL_ID)
    result.itemMeta = meta
    return result
  }

  private val recipe: UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = makeCandyShopKey()
      val recipe = ShapedRecipe(key, result)
      recipe.shape(
        "Nss",
      )
      recipe.setIngredient('N', Material.IRON_NUGGET)
      recipe.setIngredient('s', Material.STICK)
      recipe
    }

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> =
    listOf(recipe)
}
