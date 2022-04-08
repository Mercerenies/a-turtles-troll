
package com.mercerenies.turtletroll.spillage

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.block.BlockDropItemEvent

open class SpillLiquidAndEntity(
  val itemType: Material,
  val liquidType: Material?,
  val entityType: EntityType?,
) : SpillageHandler {

  override fun matches(item: Item): Boolean =
    item.itemStack.type == itemType

  open fun revertItem(item: Item) {
    item.itemStack.type = Material.BUCKET
  }

  open fun onSpawnedEntity(entity: Entity) {}

  override fun run(item: Item) {
    revertItem(item)

    if (liquidType != null) {
      item.location.block.type = liquidType
    }

    if (entityType != null) {
      val world = item.location.world!!
      val entity = world.spawnEntity(item.location, entityType)
      onSpawnedEntity(entity)
    }

  }

}
