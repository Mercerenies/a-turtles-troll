
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.inventory.meta.Damageable

import kotlin.random.Random

class GlassLuckListener(): AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
    val SECONDS_PER_SEVEN_YEARS = 3679200
    val BLOCKS = setOf(
      Material.BLACK_STAINED_GLASS, Material.BLACK_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS,
      Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS,
      Material.BROWN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS,
      Material.CYAN_STAINED_GLASS_PANE, Material.GLASS, Material.GLASS_PANE,
      Material.GRAY_STAINED_GLASS, Material.GRAY_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS,
      Material.GREEN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS,
      Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS,
      Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS,
      Material.LIME_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS,
      Material.MAGENTA_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS,
      Material.ORANGE_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS,
      Material.PINK_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS,
      Material.PURPLE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS,
      Material.RED_STAINED_GLASS_PANE, Material.TINTED_GLASS, Material.WHITE_STAINED_GLASS,
      Material.WHITE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS,
      Material.YELLOW_STAINED_GLASS_PANE,
    )
  }

  override val name = "glass"

  override val description = "Breaking any kind of glass gives seven years' bad luck"

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getBlock()
    if (BLOCKS.contains(block.type)) {
        event.player.addPotionEffect(PotionEffect(PotionEffectType.UNLUCK, TICKS_PER_SECOND * SECONDS_PER_SEVEN_YEARS, 10))
        event.player.sendMessage("The glass gods are furious! Seven years bad luck be upon ye!")
    }
  }

}
