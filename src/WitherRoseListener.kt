
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class WitherRoseListener : AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
    val BLOCKS = setOf(
      Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID,
      Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP, Material.OXEYE_DAISY,
      Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE,
      Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY,
      Material.PINK_TULIP, Material.WHITE_TULIP, Material.ORANGE_TULIP,
      Material.SWEET_BERRY_BUSH,
    )
  }

  private val bootsDamager = BootsDamager()

  override val name = "witherrose"

  override val description = "All flowers behave like wither roses"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getTo()?.getBlock()
    if ((block != null) && (BLOCKS.contains(block.type))) {
      if (!bootsDamager.tryWearDownBoots(event.player)) {
        applyWither(event.player)
      }
    }
  }

  @EventHandler
  fun onEntityPickupItemEvent(event: EntityPickupItemEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity !is Player) {
      return
    }

    val itemStack = event.item.itemStack
    if (BLOCKS.contains(itemStack.type)) {
      applyWither(entity)
    }

  }

  private fun applyWither(player: Player) {
    player.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 2 * TICKS_PER_SECOND, 0))
  }

}
