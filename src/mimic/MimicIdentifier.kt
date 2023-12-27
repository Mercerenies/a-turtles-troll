
package com.mercerenies.turtletroll.mimic

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Chest as TileChest
import org.bukkit.block.`data`.type.Chest as DataChest
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector

import kotlin.random.Random

class MimicIdentifier(
  private val plugin: Plugin,
) {

  companion object {
    private val IDENTIFIER_KEY = "mimic_identifier_key"

    private val MIMIC_DIRECTIONS = listOf(
      MimicDirection(Vector(0, 0, 1), BlockFace.EAST),
      MimicDirection(Vector(0, 0, 1), BlockFace.WEST),
      MimicDirection(Vector(0, 0, -1), BlockFace.EAST),
      MimicDirection(Vector(0, 0, -1), BlockFace.WEST),
      MimicDirection(Vector(1, 0, 0), BlockFace.NORTH),
      MimicDirection(Vector(1, 0, 0), BlockFace.SOUTH),
      MimicDirection(Vector(-1, 0, 0), BlockFace.NORTH),
      MimicDirection(Vector(-1, 0, 0), BlockFace.SOUTH),
    )

    private val MIMIC_MATERIALS = listOf(
      Material.CHEST, Material.TRAPPED_CHEST,
    )

    private val DOUBLE_MIMIC_CHANCE = 0.33
  }

  // Information about how to spawn a double mimic offset in the given
  // direction.
  private class MimicDirection(
    val relativePosition: Vector,
    val latchFacing: BlockFace,
  )

  private val identifierKey = NamespacedKey(plugin, IDENTIFIER_KEY)

  fun spawnMimic(block: Block): TileChest {
    // Consider trying to spawn a double mimic first.
    if (Random.nextDouble() < DOUBLE_MIMIC_CHANCE) {
      val doubleChest = trySpawnDoubleMimic(block)
      if (doubleChest != null) {
        return doubleChest
      }
    }
    // Always fall back to single, otherwise.
    return spawnSingleMimic(block)
  }

  private fun spawnSingleMimic(block: Block): TileChest {
    block.setType(MIMIC_MATERIALS.random())
    val chest = block.getState() as TileChest
    chest.persistentDataContainer.set(identifierKey, PersistentDataType.INTEGER, 1)
    chest.update()
    return chest
  }

  private fun trySpawnDoubleMimic(block: Block): TileChest? {
    for (direction in MIMIC_DIRECTIONS.shuffled()) {
      val newBlock = block.location.clone().add(direction.relativePosition).block
      if (newBlock.getType() == Material.AIR) {
        return spawnDoubleMimic(block, newBlock, direction.latchFacing)
      }
    }
    return null
  }

  private fun spawnDoubleMimic(leftBlock: Block, rightBlock: Block, latchFacing: BlockFace): TileChest {
    val material = MIMIC_MATERIALS.random()

    leftBlock.setType(material)
    rightBlock.setType(material)

    val leftChestData = leftBlock.getBlockData() as DataChest
    leftChestData.setType(DataChest.Type.LEFT)
    leftChestData.setFacing(latchFacing)
    leftBlock.setBlockData(leftChestData, true)
    val rightChestData = rightBlock.getBlockData() as DataChest
    rightChestData.setType(DataChest.Type.RIGHT)
    rightChestData.setFacing(latchFacing)
    rightBlock.setBlockData(rightChestData, true)

    val leftChest = leftBlock.getState() as TileChest
    leftChest.persistentDataContainer.set(identifierKey, PersistentDataType.INTEGER, 1)
    val rightChest = rightBlock.getState() as TileChest
    rightChest.persistentDataContainer.set(identifierKey, PersistentDataType.INTEGER, 1)
    leftChest.update()
    rightChest.update()

    return leftChest
  }

  fun isMimic(block: Block?): Boolean {
    // First, use the legacy system. Legacy mimics still count as
    // mimics.
    if (LegacyMimicIdentifier.isMimic(block)) {
      return true
    }

    // Else, check the metadata.
    if (block == null) {
      return false
    }
    val state = block.state
    if (state !is TileChest) {
      return false
    }
    val persistentDataContainer = state.getPersistentDataContainer()
    return persistentDataContainer.get(identifierKey, PersistentDataType.INTEGER) == 1
  }

  // Returns true if the block corresponding to the inventory is a
  // mimic, or if the inventory was synthetically created to look like
  // a mimic per MimicInventoryHolder.
  fun belongsToMimic(inventory: Inventory): Boolean =
    inventory.holder is MimicInventoryHolder ||
      isMimic(inventory.location?.block)

}
