
package com.mercerenies.turtletroll

import org.bukkit.Material
import org.bukkit.block.Block

object Infestation {

  fun infestedMaterial(material: Material): Material? =
    when (material) {
      Material.COBBLESTONE -> Material.INFESTED_COBBLESTONE
      Material.STONE_BRICKS -> Material.INFESTED_STONE_BRICKS
      Material.CRACKED_STONE_BRICKS -> Material.INFESTED_CRACKED_STONE_BRICKS
      Material.CHISELED_STONE_BRICKS -> Material.INFESTED_CHISELED_STONE_BRICKS
      Material.MOSSY_STONE_BRICKS -> Material.INFESTED_MOSSY_STONE_BRICKS
      Material.STONE -> Material.INFESTED_STONE
      Material.DEEPSLATE -> Material.INFESTED_DEEPSLATE
      else -> null
    }

  fun canInfest(material: Material): Boolean =
    infestedMaterial(material) != null

  fun infest(block: Block): Boolean {
    val mat = infestedMaterial(block.type)
    if (mat != null) {
      block.type = mat
      return true
    } else {
      return false
    }
  }

}
