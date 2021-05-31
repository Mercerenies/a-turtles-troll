
package com.mercerenies.turtletroll.durability

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.entity.Player
import org.bukkit.block.Block
import org.bukkit.block.`data`.Bisected

import kotlin.collections.HashMap

class ButtonDamageListener(
  override val maxUses: Int = 4,
) : DurabilityListener() {

  companion object {

    val BUTTONS = setOf(
      Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.CRIMSON_BUTTON,
      Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, Material.OAK_BUTTON,
      Material.POLISHED_BLACKSTONE_BUTTON, Material.SPRUCE_BUTTON,
      Material.STONE_BUTTON, Material.WARPED_BUTTON,
    )

  }

  override val name = "buttondrop"

  override val description = "Buttons break after some number of uses"

  override fun shouldAffect(block: Block): Boolean =
    BUTTONS.contains(block.type)

}
