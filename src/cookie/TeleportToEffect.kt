
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Worlds
import com.mercerenies.turtletroll.Messages

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.plugin.Plugin
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.scheduler.BukkitRunnable

abstract class TeleportToEffect(private val plugin: Plugin) : CookieEffect {

  companion object {

    private val DELAY = Constants.TICKS_PER_SECOND * 3L

  }

  class ToWorldSpawn(_plugin: Plugin) : TeleportToEffect(_plugin) {

    override val message: String =
      "That cookie tastes like the world spawn!"

    override fun getTarget(player: Player): Location {
      // Not ideal, but if for some reason we can't find the
      // overworld, fall back to the player's current world.
      val overworld = Worlds.getOverworld() ?: player.world
      return overworld.getSpawnLocation()
    }

  }

  class ToPlayerSpawn(_plugin: Plugin) : TeleportToEffect(_plugin) {

    private val fallback = ToWorldSpawn(_plugin)

    override val message: String =
      "That cookie tastes like your spawn point!"

    override fun getTarget(player: Player): Location =
      // If the player doesn't have a bed spawn, use world spawn
      player.getRespawnLocation() ?: fallback.getTarget(player)

  }

  private inner class TeleportPlayer(val player: Player, val loc: Location) : BukkitRunnable() {
    override fun run() {
      val sound = this@TeleportToEffect.sound
      player.teleport(loc, cause)
      if (sound != null) {
        loc.world!!.playSound(loc, sound, 1.0f, 0.0f)
      }
    }
  }

  abstract val message: String

  open val sound: Sound?
    get() = Sound.ITEM_CHORUS_FRUIT_TELEPORT

  open val cause: PlayerTeleportEvent.TeleportCause
    get() = PlayerTeleportEvent.TeleportCause.PLUGIN

  abstract fun getTarget(player: Player): Location

  open override fun cancelsDefault(): Boolean = false

  override fun onEat(action: CookieEatenAction) {
    val player = action.player
    val target = getTarget(player)
    val sound = this.sound
    Messages.sendMessage(player, message)
    if (sound != null) {
      player.world.playSound(player.location, sound, 1.0f, 0.0f)
    }
    TeleportPlayer(player, target).runTaskLater(plugin, DELAY)
  }

}
