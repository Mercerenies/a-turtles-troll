
package com.mercerenies.turtletroll.angel

import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.Constants

import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.command.CommandSender

class WeepingAngelCommand(
  private val config: Configuration,
) : TerminalCommand() {

  companion object {
    val DISABLED_MESSAGE = "[Turtle] Weeping angels are currently disabled on this server"
    val PLAYER_ONLY_MESSAGE = "[Turtle] Weeping angels can only be summoned by players"
  }

  interface Configuration : HasEnabledStatus {

    fun onAngelSpawn(angel: ArmorStand)

  }

  class FromManager(
    private val manager: WeepingAngelManager,
    val gracePeriod: Long = Constants.TICKS_PER_SECOND * 3L,
  ) : Configuration {

    override fun isEnabled(): Boolean = manager.isEnabled()

    override fun onAngelSpawn(angel: ArmorStand) {
      manager.addGracePeriodFor(angel, gracePeriod)
      WeepingAngelManager.assignIdlePose(angel)
    }

  }

  fun isEnabled(): Boolean =
    config.isEnabled()

  override fun onCommand(sender: CommandSender): Boolean {
    if (!isEnabled()) {
      sender.sendMessage(DISABLED_MESSAGE)
      return true
    }
    if (sender !is Entity) {
      sender.sendMessage(PLAYER_ONLY_MESSAGE)
      return true
    }

    val stand = ArmorStandSpawner.spawn(sender.location)
    config.onAngelSpawn(stand)
    return true
  }

}
