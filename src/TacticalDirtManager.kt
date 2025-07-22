
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.setBasicCustomModelData

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.CraftingInventory
import org.bukkit.plugin.Plugin
import org.bukkit.Material
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

import net.kyori.adventure.text.Component

class TacticalDirtManager(_plugin: Plugin) : RecipeFeature(_plugin), Listener {
  companion object : FeatureContainerFactory<FeatureContainer> {
    private val NUTRIENT_DIRT_MARKER_KEY = "nutrient_dirt_tag"
    private val CUSTOM_MODEL_DATA = 1

    private val VALID_ACTIONS = listOf(
        InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF,
        InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME,
        InventoryAction.MOVE_TO_OTHER_INVENTORY,
    )

    override fun create(state: BuilderState): FeatureContainer {
      val manager = TacticalDirtManager(state.plugin)
      return FeatureBuilder()
          .addFeature(manager)
          .addListener(manager)
          .addGameModification(manager)
          .build()
    }

    private fun getMarkerKey(plugin: Plugin): NamespacedKey =
        NamespacedKey(plugin, NUTRIENT_DIRT_MARKER_KEY)

    private fun makeNutrientDirt(plugin: Plugin, amount: Int = 1): ItemStack {
      val item = ItemStack(Material.BAKED_POTATO, amount)
      val meta = item.itemMeta
      meta.displayName(Component.text("Nutrient Dirt"))

      // TODO Custom model data in the resource pack is currently
      // broken, so we fall back to dirt right now.
      meta.setItemModel(NamespacedKey.minecraft("dirt"))

      //meta.setBasicCustomModelData(CUSTOM_MODEL_DATA)

      meta.persistentDataContainer.set(getMarkerKey(plugin), PersistentDataType.BOOLEAN, true)
      item.itemMeta = meta
      return item
    }
  }

  override val name = "tacticaldirt"

  override val description = "Crafting dirt and seeds makes Tactical Dirt."

  private val tacticalDirtRecipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
    val outItem = makeNutrientDirt(plugin)
    val recipe = ShapelessRecipe(key, outItem)
    recipe.addIngredient(1, Material.DIRT)
    recipe.addIngredient(1, Material.WHEAT_SEEDS)
    recipe
  }

  private fun isNutrientDirt(itemStack: ItemStack?): Boolean =
      itemStack != null &&
      itemStack.itemMeta.persistentDataContainer.getOrDefault(getMarkerKey(plugin), PersistentDataType.BOOLEAN, false)

  private fun isNutrientDirtClickEvent(event: InventoryClickEvent): Boolean {
    if (!VALID_ACTIONS.contains(event.action)) {
      return false
    }
    if (!isNutrientDirt(event.currentItem)) {
      return false
    }
    val inventory = event.inventory
    return (inventory is CraftingInventory || inventory is PlayerInventory) && event.slotType == InventoryType.SlotType.RESULT
  }

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> =
      listOf(tacticalDirtRecipe)

  @EventHandler
  fun onInventoryClick(event: InventoryClickEvent) {
    if (!isEnabled()) {
      return
    }
    val sound = Sounds.TACTICAL_DIRT.random()
    if (isNutrientDirtClickEvent(event)) {
      val amountOfDirt = event.currentItem!!.amount
      for (player in Bukkit.getOnlinePlayers()) {
        if (player.entityId != event.whoClicked.entityId) {
          player.inventory.addItem(makeNutrientDirt(plugin, amountOfDirt))
        }
        player.world.playSound(player.location, sound, 1.0f, 0.0f)
      }
    }
  }
}
