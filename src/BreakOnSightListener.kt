
package com.mercerenies.turtletroll

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

abstract class BreakOnSightListener(_plugin: Plugin) : OnSightListener(_plugin) {

  override fun performEffect(player: Player, block: Block) {
    block.breakNaturally()
  }

}
