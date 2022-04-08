
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.entity.Player
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

import kotlin.random.Random
import kotlin.math.PI

// Note: Doesn't work on the player's own inventory right now. It
// looks like that's a client-side thing that I can't control,
// sadly...
class ButterfingersListener(): AbstractFeature(), Listener {

  override val name = "butterfingers"

  override val description = "Whenever a player opens an inventory, the item in their primary hand is dropped"

  @EventHandler
  fun onInventoryOpen(event: InventoryOpenEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.player
    // Technically we could do this for HumanEntity, but just in case
    // someone has an NPC plugin installed, let's check that they're a
    // Player to be safe.
    if (player is Player) {
      val inv = player.inventory
      val heldSlot = inv.getHeldItemSlot()
      val heldObject = inv.getItem(heldSlot)
      if (heldObject != null) {
        inv.setItem(heldSlot, null)
        val droppedItemEntity = player.world.spawn(player.location, Item::class.java)
        droppedItemEntity.itemStack = heldObject
        droppedItemEntity.pickupDelay = Constants.TICKS_PER_SECOND * 3
        droppedItemEntity.velocity = Vector(0.3, 2.0, 0.3).rotateAroundY(Random.nextDouble() * 2 * PI)
      }
    }
  }

}
