
package com.mercerenies.turtletroll.pokeball

import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.plugin.Plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.entity.Egg
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.entity.Item
import org.bukkit.entity.EntityType
import org.bukkit.inventory.PlayerInventory

import net.kyori.adventure.text.Component

class PokeballManager(plugin: Plugin) : RecipeFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val POKEBALL_MARKER_KEY = "pokeball_tag"

    override fun create(state: BuilderState): FeatureContainer =
      Container(PokeballManager(state.plugin))

    fun identifyFiredEgg(inv: PlayerInventory): ItemStack? {
      if (inv.getItemInMainHand().type == Material.EGG) {
        return inv.getItemInMainHand()
      }
      if (inv.getItemInOffHand().type == Material.EGG) {
        return inv.getItemInOffHand()
      }
      return null
    }

    private fun summonItem(location: Location, item: ItemStack) {
      val itemEntity = location.world!!.spawn(location, Item::class.java)
      itemEntity.itemStack = item
    }

    private fun doCapture(mob: Mob) {
      // If the mob has a spawn egg, spawn it.
      val spawnEggMaterial = toSpawnEgg(mob.type)
      if (spawnEggMaterial != null) {
        summonItem(mob.location, ItemStack(spawnEggMaterial))
      }
      // Exception: Iron golem
      if (mob.type == EntityType.IRON_GOLEM) {
        // Yes, you profit off this. It's more than the amount of iron
        // needed to build the golem.
        summonItem(mob.location, ItemStack(Material.IRON_BLOCK, 6))
      }
      // Exception: Snow golem
      if (mob.type == EntityType.SNOWMAN) {
        summonItem(mob.location, ItemStack(Material.SNOWBALL, 16))
      }
      // Exception: Giant
      if (mob.type == EntityType.GIANT) {
        summonItem(mob.location, ItemStack(Material.ZOMBIE_SPAWN_EGG))
      }
      // Exception: Illusioner
      if (mob.type == EntityType.ILLUSIONER) {
        summonItem(mob.location, ItemStack(Material.DIAMOND))
      }
      // Kill the mob
      val loc = mob.location
      loc.y += -999.0
      mob.teleport(loc)
      mob.damage(99999.0, null)
    }

  }

  private class Container(
    private val manager: PokeballManager,
  ) : AbstractFeatureContainer() {

    override val listeners =
      listOf(manager)

    override val features =
      listOf(manager)

    override val recipes =
      listOf(manager)

  }

  override val name = "pokeball"

  override val description = "Poké Balls can be crafted and thrown to capture mobs"

  val markerKey = NamespacedKey(plugin, POKEBALL_MARKER_KEY)

  private fun getPokeballRecipe(): UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = ItemStack(Material.EGG)
      val meta = result.itemMeta!!
      meta.displayName(Component.text("Poké Ball"))
      meta.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, NormalPokeball.toInt())
      meta.setCustomModelData(NormalPokeball.customModelId)
      result.itemMeta = meta
      val recipe = ShapedRecipe(key, result)
      recipe.shape(
        " B ",
        "IRI",
        " I ",
      )
      recipe.setIngredient('B', Material.BRICK)
      recipe.setIngredient('R', Material.REDSTONE)
      recipe.setIngredient('I', Material.IRON_NUGGET)
      recipe
    }

  private fun getGreatBallRecipe(): UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = ItemStack(Material.EGG)
      val meta = result.itemMeta!!
      meta.displayName(Component.text("Great Ball"))
      meta.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, GreatBall.toInt())
      meta.setCustomModelData(GreatBall.customModelId)
      result.itemMeta = meta
      val recipe = ShapedRecipe(key, result)
      recipe.shape(
        " B ",
        "IRI",
        " I ",
      )
      recipe.setIngredient('B', Material.IRON_BLOCK)
      recipe.setIngredient('R', Material.REDSTONE)
      recipe.setIngredient('I', Material.IRON_NUGGET)
      recipe
    }

  private fun getUltraBallRecipe(): UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = ItemStack(Material.EGG)
      val meta = result.itemMeta!!
      meta.displayName(Component.text("Ultra Ball"))
      meta.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, UltraBall.toInt())
      meta.setCustomModelData(UltraBall.customModelId)
      result.itemMeta = meta
      val recipe = ShapedRecipe(key, result)
      recipe.shape(
        " B ",
        "IRI",
        " I ",
      )
      recipe.setIngredient('B', Material.NETHERITE_INGOT)
      recipe.setIngredient('R', Material.REDSTONE)
      recipe.setIngredient('I', Material.IRON_NUGGET)
      recipe
    }

  private fun getMasterBallRecipe(): UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = ItemStack(Material.EGG)
      val meta = result.itemMeta!!
      meta.displayName(Component.text("Master Ball"))
      meta.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, MasterBall.toInt())
      meta.setCustomModelData(MasterBall.customModelId)
      result.itemMeta = meta
      val recipe = ShapedRecipe(key, result)
      recipe.shape(
        "XXX",
        "XBX",
        "XXX",
      )
      recipe.setIngredient('X', Material.NETHERITE_INGOT)
      recipe.setIngredient('B', Material.REDSTONE)
      recipe
    }

  private fun getNetBallRecipe(): UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = ItemStack(Material.EGG)
      val meta = result.itemMeta!!
      meta.displayName(Component.text("Net Ball"))
      meta.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, NetBall.toInt())
      meta.setCustomModelData(NetBall.customModelId)
      result.itemMeta = meta
      val recipe = ShapedRecipe(key, result)
      recipe.shape(
        "CBC",
        "IRI",
        " I ",
      )
      recipe.setIngredient('C', Material.STRING)
      recipe.setIngredient('B', Material.BRICK)
      recipe.setIngredient('R', Material.REDSTONE)
      recipe.setIngredient('I', Material.IRON_NUGGET)
      recipe
    }

  private fun getDiveBallRecipe(): UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = ItemStack(Material.EGG)
      val meta = result.itemMeta!!
      meta.displayName(Component.text("Dive Ball"))
      meta.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, DiveBall.toInt())
      meta.setCustomModelData(DiveBall.customModelId)
      result.itemMeta = meta
      val recipe = ShapedRecipe(key, result)
      recipe.shape(
        "KBK",
        "IRI",
        " I ",
      )
      recipe.setIngredient('K', Material.KELP)
      recipe.setIngredient('B', Material.BRICK)
      recipe.setIngredient('R', Material.REDSTONE)
      recipe.setIngredient('I', Material.IRON_NUGGET)
      recipe
    }

  private fun getDuskBallRecipe(): UnkeyedRecipe<Recipe> =
    UnkeyedRecipe<Recipe> { key: NamespacedKey ->
      val result = ItemStack(Material.EGG)
      val meta = result.itemMeta!!
      meta.displayName(Component.text("Dusk Ball"))
      meta.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, DuskBall.toInt())
      meta.setCustomModelData(DuskBall.customModelId)
      result.itemMeta = meta
      val recipe = ShapedRecipe(key, result)
      recipe.shape(
        "CBC",
        "IRI",
        " I ",
      )
      recipe.setIngredient('C', Material.COAL)
      recipe.setIngredient('B', Material.BRICK)
      recipe.setIngredient('R', Material.REDSTONE)
      recipe.setIngredient('I', Material.IRON_NUGGET)
      recipe
    }

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> =
    listOf(
      getPokeballRecipe(), getGreatBallRecipe(), getUltraBallRecipe(),
      getMasterBallRecipe(), getNetBallRecipe(), getDiveBallRecipe(),
      getDuskBallRecipe(),
    )

  @EventHandler
  fun onProjectileHit(event: ProjectileHitEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    val hitEntity = event.hitEntity
    if (entity is Egg) {
      val typeIndex = entity.persistentDataContainer.get(markerKey, PersistentDataType.INTEGER)
      if (typeIndex != null) {
        // Cancel the event, whether or not we hit something (so that
        // EggListener doesn't trigger)
        event.setCancelled(true)

        val type = PokeballType.fromInt(typeIndex)
        if (hitEntity is Mob) {
          val captureSucceeds = Capture.simulateCapture(type, hitEntity)
          if (captureSucceeds) {
            doCapture(hitEntity)
          }
        }
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
    if ((entity is Egg) && (shooter is Player)) {
      val egg = identifyFiredEgg(shooter.getInventory())
      if (egg != null) {
        val pokeballTypeIndex = egg.itemMeta?.persistentDataContainer?.get(markerKey, PersistentDataType.INTEGER)
        if (pokeballTypeIndex != null) {
          entity.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, pokeballTypeIndex)
        }
      }
    }
  }

  @EventHandler
  fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
    if (!isEnabled()) {
      return
    }
    val damager = event.damager
    if (damager is Egg) {
      val typeIndex = damager.persistentDataContainer.get(markerKey, PersistentDataType.INTEGER)
      if (typeIndex != null) {
        // It's a pokeball, so the damage doesn't count
        event.setCancelled(true)
      }
    }
  }

}
