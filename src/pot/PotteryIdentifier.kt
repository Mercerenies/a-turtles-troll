
package com.mercerenies.turtletroll.pot

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.DecoratedPot
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector

import kotlin.random.Random

class PotteryIdentifier(
  private val plugin: Plugin,
) {

  companion object {
    private val IDENTIFIER_KEY = "pottery_identifier_key"

    private val POT_MATERIAL = Material.DECORATED_POT
  }

  private val identifierKey = NamespacedKey(plugin, IDENTIFIER_KEY)

  fun spawnPottery(block: Block): DecoratedPot {
    block.setType(POT_MATERIAL)
    val pot = block.getState() as DecoratedPot
    pot.persistentDataContainer.set(identifierKey, PersistentDataType.INTEGER, 1)
    pot.update()
    return pot
  }

  fun isPottery(block: Block?): Boolean {
    if (block == null) {
      return false
    }
    val state = block.state
    if (state !is DecoratedPot) {
      return false
    }
    val persistentDataContainer = state.getPersistentDataContainer()
    return persistentDataContainer.get(identifierKey, PersistentDataType.INTEGER) == 1
  }
}
