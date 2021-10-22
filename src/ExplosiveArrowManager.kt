
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.recipe.RecipeFeature

import org.bukkit.plugin.Plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Trident
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

class ExplosiveArrowManager(plugin: Plugin) : RecipeFeature(plugin), Listener {

  companion object {
    val ARROW_MARKER_KEY = "explosive_arrow_tag"

    val ARROWS = setOf(
      Material.ARROW, Material.TIPPED_ARROW, Material.SPECTRAL_ARROW
    )

    fun identifyFiredArrow(inv: PlayerInventory): ItemStack? {
      // Items in either hand are prioritized if they're arrows
      if (ARROWS.contains(inv.getItemInMainHand().type)) {
        return inv.getItemInMainHand()
      }
      if (ARROWS.contains(inv.getItemInOffHand().type)) {
        return inv.getItemInOffHand()
      }
      for (item in inv.getStorageContents()) {
        if ((item != null) && ARROWS.contains(item.type)) {
          return item
        }
      }
      return null
    }

    fun roman(n: Int): String =
      when (n) {
        1 -> "I"
        2 -> "II"
        3 -> "III"
        else -> ""
      }

  }

  override val name = "explosivearrow"

  override val description = "Explosive arrows can be crafted, and tridents explode"

  val markerKey = NamespacedKey(plugin, ARROW_MARKER_KEY)

  private fun getRecipe(n: Int): UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = ItemStack(Material.TIPPED_ARROW)
      val meta = result.itemMeta!!
      meta.setDisplayName("Explosive Arrow (${roman(n)})")
      meta.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, n)
      result.itemMeta = meta
      val recipe = ShapelessRecipe(key, result)
      recipe.addIngredient(1, Material.ARROW)
      recipe.addIngredient(n, Material.GUNPOWDER)
      recipe
    }

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> =
    (1..3).map { this.getRecipe(it) }

  @EventHandler
  fun onProjectileHit(event: ProjectileHitEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is AbstractArrow) {
      val strength = entity.persistentDataContainer.get(markerKey, PersistentDataType.INTEGER)
      if (strength != null) {
        entity.world.createExplosion(entity.location, (strength / 2.0F) + 1.0F, false)
      }
    }
  }

  @EventHandler
  fun onProjectileLaunch(event: ProjectileLaunchEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    val shooter = entity.shooter
    if ((entity is AbstractArrow) && (shooter is Player)) {
      val arrow = identifyFiredArrow(shooter.getInventory())
      var strength = arrow?.itemMeta?.persistentDataContainer?.get(markerKey, PersistentDataType.INTEGER)
      if (entity is Trident) {
        strength = 3
      }
      if (strength != null) {
        entity.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, strength)
      }
    }
  }

}
