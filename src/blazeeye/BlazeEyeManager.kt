
package com.mercerenies.turtletroll.blazeeye

import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.generator.structure.StructureType
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EnderSignal
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.Action
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

import net.kyori.adventure.text.Component

class BlazeEyeManager(plugin: Plugin) : RecipeFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val EYE_MARKER_KEY = "blaze_eye_tag"
    val CUSTOM_MODEL_ID = 1

    private val STRUCTURE_SEARCH_RADIUS = 32

    private val ACTIONS =
      listOf(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)

    fun getMarkerKey(plugin: Plugin): NamespacedKey =
      NamespacedKey(plugin, EYE_MARKER_KEY)

    override fun create(state: BuilderState): FeatureContainer =
      Container(BlazeEyeManager(state.plugin))

    private fun findNearestFortress(origin: Location): Location? {
      val structureResult = origin.world.locateNearestStructure(origin, StructureType.FORTRESS, STRUCTURE_SEARCH_RADIUS, false)
      return structureResult?.location
    }

  }

  private class Container(
    private val manager: BlazeEyeManager,
  ) : AbstractFeatureContainer() {

    override val listeners =
      listOf(manager)

    override val features =
      listOf(manager)

    override val gameModifications =
      listOf(manager)

  }

  override val name = "blazeeye"

  override val description = "Blaze Eyes can be crafted and lead to a nether fortress"

  val markerKey = getMarkerKey(plugin)

  private fun makeEnderEye(amount: Int = 1): ItemStack {
    val result = ItemStack(Material.ENDER_EYE, amount)
    val meta = result.itemMeta!!
    meta.displayName(Component.text("Eye of Blaze"))
    meta.persistentDataContainer.set(markerKey, PersistentDataType.BOOLEAN, true)
    meta.setCustomModelData(CUSTOM_MODEL_ID)
    result.itemMeta = meta
    return result
  }

  private val recipe: UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = makeEnderEye()
      val recipe = ShapelessRecipe(key, result)
      recipe.addIngredient(1, Material.FLINT_AND_STEEL)
      recipe.addIngredient(1, Material.ENDER_PEARL)
      recipe
    }

  private fun isBlazeEye(item: ItemStack): Boolean {
    val container = item.itemMeta?.persistentDataContainer
    if (container == null) {
      return false
    } else {
      return item.type == Material.ENDER_EYE && container.getOrDefault(markerKey, PersistentDataType.BOOLEAN, false)
    }
  }

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> =
    listOf(recipe)

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }

    val item = event.item
    val player = event.player
    if ((item != null) && (isBlazeEye(item)) && (event.action in ACTIONS)) {
      event.setCancelled(true)
      if (player.world.environment == World.Environment.NETHER) {
        spawnEnderSignal(player)
        // event.hand cannot be null, as event.action is not Action.PHYSICAL.
        decrementBlazeEyes(player, event.hand!!)
      }
    }
  }

  private fun spawnEnderSignal(player: Player): EnderSignal? {
    val signalLocation = player.location.clone().add(0.0, 1.0, 0.0)
    val targetLocation = findNearestFortress(player.location)
    if (targetLocation != null) {
      val enderSignal = player.world.spawn(signalLocation, EnderSignal::class.java)
      enderSignal.setItem(makeEnderEye())
      enderSignal.targetLocation = targetLocation
      return enderSignal
    } else {
      return null
    }
  }

  private fun decrementBlazeEyes(player: Player, equipmentSlot: EquipmentSlot) {
    val item = player.inventory.getItem(equipmentSlot)
    if (item.amount > 1) {
      item.amount -= 1
    } else {
      player.inventory.setItem(equipmentSlot, null)
    }
  }

}
