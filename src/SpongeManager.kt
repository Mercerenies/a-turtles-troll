
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.recipe.RecipeFeature
import com.mercerenies.turtletroll.location.BlockSelector
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.nms.NMS
import com.mercerenies.turtletroll.location.PlayerSelector
import com.mercerenies.turtletroll.util.component.*

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.potion.PotionEffectType
import org.bukkit.entity.Allay
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe

import net.kyori.adventure.text.Component

class SpongeManager(_plugin: Plugin) : RecipeFeature(_plugin), Listener {
  companion object : FeatureContainerFactory<FeatureContainer> {
    val EFFECT_DISTANCE: Int = 7

    override fun create(state: BuilderState): FeatureContainer {
      val manager = SpongeManager(state.plugin)
      return FeatureBuilder()
          .addFeature(manager)
          .addListener(manager)
          .addGameModification(manager)
          .build()
    }
  }

  override val name = "sponges"

  override val description = "Wet sponges can absorb lava when placed and dry out"

  private val spongeRecipe = UnkeyedRecipe<Recipe> { key: NamespacedKey ->
    val recipe = ShapedRecipe(key, ItemStack(Material.SPONGE, 8))
    recipe.shape("AA", "AA")
    recipe.setIngredient('A', Material.PHANTOM_MEMBRANE)
    recipe
  }

  override fun getRecipes(): List<UnkeyedRecipe<Recipe>> = listOf(spongeRecipe)

  @EventHandler
  fun onBlockPlace(event: BlockPlaceEvent) {
    if (!isEnabled()) {
      return
    }
    val targetBlock = event.getBlockPlaced()
    var changedAny = false
    if (targetBlock.type == Material.WET_SPONGE) {
      for (loc in BlockSelector.getNearby(targetBlock.location, EFFECT_DISTANCE)) {
        val block = loc.block
        if (block.type == Material.LAVA) {
          block.type = Material.AIR
          changedAny = true
        }
      }
    }
    if (changedAny) {
      targetBlock.world.playSound(targetBlock.location, Sound.BLOCK_SPONGE_ABSORB, 1.0f, 0.0f)
      targetBlock.type = Material.SPONGE
    }
  }
}
