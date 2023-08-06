
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.Worlds

import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.Bukkit

class FromBanishCommand(
  private val config: BanishCommandConfiguration,
) : TerminalCommand() {

  fun isEnabled(): Boolean =
    config.isEnabled()

  override fun onCommand(sender: CommandSender): Boolean {
    if (!isEnabled()) {
      Messages.sendMessage(sender, BanishCommandConfiguration.DISABLED_MESSAGE)
      return true
    }
    if (sender !is Entity) {
      Messages.sendMessage(sender, BanishCommandConfiguration.PLAYER_ONLY_MESSAGE)
      return true
    }

    val overworld = Worlds.getOverworld()
    if (overworld == null) {
      Bukkit.getLogger().warning("Could not find overworld")
      return true
    }

    val newLocation = sender.location.clone()
    newLocation.world = overworld
    sender.teleport(newLocation)
    return true
  }

}
