
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.command.PermittedCommand
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Messages

import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.world.WorldInitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

import kotlin.random.Random

class BanishmentManager(
  val plugin: Plugin,
  val banishChance: Double,
) : AbstractFeature(), Listener {

  private class SlowFallingRunnable(
    private val player: Player,
  ) : BukkitRunnable() {
    override fun run() {
      player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, Constants.TICKS_PER_SECOND * 30, 0))
    }
  }

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
  fun onWorldInit(event: WorldInitEvent) {
    if (!isEnabled()) {
      return
    }

    if (event.world == worldController.world) {
      event.world.populators.add(worldController.blockPopulator)
    }
  }

  @EventHandler
  fun onPlayerRespawn(event: PlayerRespawnEvent) {
    if (!isEnabled()) {
      return
    }

    if (event.respawnReason != PlayerRespawnEvent.RespawnReason.DEATH) {
      // We only banish players for dying.
      return
    }

    if (Random.nextDouble() < banishChance) {
      // Banish them!
      Messages.sendMessage(event.player, BanishCommandConfiguration.BANISHMENT_MESSAGE)
      val world = worldController.world!!
      val x = Random.nextDouble(-100.0, 100.0)
      val y = BanishmentWorldController.LOWER_GRASS_HEIGHT + 32.0
      val z = Random.nextDouble(-100.0, 100.0)
      event.respawnLocation = Location(world, x, y, z)
      SlowFallingRunnable(event.player).runTaskLater(plugin, 1)
    }
  }

}
