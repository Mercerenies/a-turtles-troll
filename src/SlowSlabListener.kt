
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SlowSlabListener : AbstractFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
    val BLOCKS = setOf(
      Material.ACACIA_SLAB, Material.ANDESITE_SLAB, Material.BIRCH_SLAB, Material.BLACKSTONE_SLAB,
      Material.BRICK_SLAB, Material.COBBLESTONE_SLAB, Material.CRIMSON_SLAB,
      Material.CUT_RED_SANDSTONE_SLAB, Material.CUT_SANDSTONE_SLAB, Material.DARK_OAK_SLAB,
      Material.DARK_PRISMARINE_SLAB, Material.DIORITE_SLAB, Material.END_STONE_BRICK_SLAB,
      Material.GRANITE_SLAB, Material.JUNGLE_SLAB, Material.MOSSY_COBBLESTONE_SLAB,
      Material.MOSSY_STONE_BRICK_SLAB, Material.NETHER_BRICK_SLAB, Material.OAK_SLAB,
      Material.PETRIFIED_OAK_SLAB, Material.POLISHED_ANDESITE_SLAB,
      Material.POLISHED_BLACKSTONE_BRICK_SLAB, Material.POLISHED_BLACKSTONE_SLAB,
      Material.POLISHED_DIORITE_SLAB, Material.POLISHED_GRANITE_SLAB,
      Material.PRISMARINE_BRICK_SLAB, Material.PRISMARINE_SLAB, Material.PURPUR_SLAB,
      Material.QUARTZ_SLAB, Material.RED_NETHER_BRICK_SLAB, Material.RED_SANDSTONE_SLAB,
      Material.SANDSTONE_SLAB, Material.SMOOTH_QUARTZ_SLAB, Material.SMOOTH_RED_SANDSTONE_SLAB,
      Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_STONE_SLAB, Material.SPRUCE_SLAB,
      Material.STONE_BRICK_SLAB, Material.STONE_SLAB, Material.WARPED_SLAB,
      Material.ACACIA_STAIRS, Material.ANDESITE_STAIRS, Material.BIRCH_STAIRS, Material.BLACKSTONE_STAIRS,
      Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.CRIMSON_STAIRS,
      Material.DARK_OAK_STAIRS,
      Material.DARK_PRISMARINE_STAIRS, Material.DIORITE_STAIRS, Material.END_STONE_BRICK_STAIRS,
      Material.GRANITE_STAIRS, Material.JUNGLE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS,
      Material.MOSSY_STONE_BRICK_STAIRS, Material.NETHER_BRICK_STAIRS, Material.OAK_STAIRS,
      Material.POLISHED_ANDESITE_STAIRS,
      Material.POLISHED_BLACKSTONE_BRICK_STAIRS, Material.POLISHED_BLACKSTONE_STAIRS,
      Material.POLISHED_DIORITE_STAIRS, Material.POLISHED_GRANITE_STAIRS,
      Material.PRISMARINE_BRICK_STAIRS, Material.PRISMARINE_STAIRS, Material.PURPUR_STAIRS,
      Material.QUARTZ_STAIRS, Material.RED_NETHER_BRICK_STAIRS, Material.RED_SANDSTONE_STAIRS,
      Material.SANDSTONE_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_RED_SANDSTONE_STAIRS,
      Material.SMOOTH_SANDSTONE_STAIRS, Material.SPRUCE_STAIRS,
      Material.STONE_BRICK_STAIRS, Material.STONE_STAIRS, Material.WARPED_STAIRS,
    )
  }

  override val name = "slowslab"

  override val description = "Half slabs and stairs slow you down"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getTo()?.getBlock()
    if ((block != null) && (BLOCKS.contains(block.type))) {
      val player = event.player
      player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, TICKS_PER_SECOND * 10, 3))
    }
  }

}
