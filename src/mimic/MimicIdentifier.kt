
package com.mercerenies.turtletroll.mimic

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import org.bukkit.persistence.PersistentDataType

class MimicIdentifier(
  private val plugin: Plugin,
) {

  companion object {
    private val IDENTIFIER_KEY = "mimic_identifier_key"
  }

  private val identifierKey = NamespacedKey(plugin, IDENTIFIER_KEY)

  fun spawnMimic(block: Block): Chest {
    block.setType(Material.CHEST)
    val chest = block.getState() as Chest
    chest.persistentDataContainer.set(identifierKey, PersistentDataType.INTEGER, 1)
    chest.update()
    return chest
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
    if (state !is Chest) {
      return false
    }
    val persistentDataContainer = state.getPersistentDataContainer()
    return persistentDataContainer.get(identifierKey, PersistentDataType.INTEGER) == 1
  }

  fun belongsToMimic(inventory: Inventory): Boolean =
    isMimic(inventory.location?.block)

}
