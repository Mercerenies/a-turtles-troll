
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions

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

  private val worldController = BanishmentWorldController()

  private val commandConfig = object : BanishCommandConfiguration {
    override fun isEnabled(): Boolean = this@BanishmentManager.isEnabled()
    override val superflatWorld: World
      get() = worldController.world
  }

  private val toBanishCommand = ToBanishCommand(commandConfig)
  private val fromBanishCommand = FromBanishCommand(commandConfig)

  val commands: List<Pair<String, PermittedCommand<Command>>> =
    listOf(
      "tobanish" to toBanishCommand.withPermission(Permissions.DEBUG),
      "frombanish" to fromBanishCommand.withPermission(Permissions.DEBUG),
    )

  @EventHandler
  fun onPlayerRespawn(event: PlayerRespawnEvent) {
    if (!isEnabled()) {
      return
    }
    /////
  }

}
