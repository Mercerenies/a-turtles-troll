
package com.mercerenies.turtletroll.spillage

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

open class SpillLiquidAndEntity(
  val itemType: Material,
  val liquidType: Material?,
  val entityType: EntityType?,
) : SpillageHandler {

  override fun matches(item: Item): Boolean =
    item.itemStack.type == itemType

  open fun revertItem(item: Item) {
    val itemStack = ItemStack(Material.BUCKET, item.itemStack.amount)
    item.itemStack = itemStack
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
