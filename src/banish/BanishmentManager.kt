
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.generator.structure.StructureType
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EnderSignal
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.block.Action
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

import net.kyori.adventure.text.Component

class BanishmentManager(plugin: Plugin) : AbstractFeature(), Listener {

  override val name = "banishment"

  override val description = "Players are sometimes banished to a superflat world"

  @EventHandler
  fun onPlayerRespawn(event: PlayerRespawnEvent) {
    if (!isEnabled()) {
      return
    }
    /////
  }

}
