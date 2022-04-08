
package com.mercerenies.turtletroll.spillage

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.block.BlockDropItemEvent

interface SpillageHandler {

  fun matches(item: Item): Boolean

  fun run(item: Item)

}
