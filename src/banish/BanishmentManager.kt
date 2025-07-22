
package com.mercerenies.turtletroll.banish

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.command.Command
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.Messages
import com.mercerenies.turtletroll.Worlds

import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.GameRule
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
  _plugin: Plugin,
  val banishChance: Double,
) : RunnableFeature(_plugin), Listener {
  companion object {
    private val MIDNIGHT = 18_000L
  }

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

  val debugCommands: List<Pair<String, Command>> =
    listOf(
      "tobanish" to toBanishCommand,
      "frombanish" to fromBanishCommand,
    )

  override val taskPeriod: Long = Constants.TICKS_PER_SECOND * 11L

  @EventHandler
  fun onWorldInit(event: WorldInitEvent) {
    if (event.world == worldController.world) {
      event.world.populators.add(worldController.blockPopulator)
      val overworld = Worlds.getOverworld()
      if (overworld != null) {
        // Sync keepInventory, so that we don't lose inventories
        // stupidly.
        val keepInventory = overworld.getGameRuleValue(GameRule.KEEP_INVENTORY) ?: false
        worldController.world.setGameRule(GameRule.KEEP_INVENTORY, keepInventory)
      }
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
      val x = Random.nextDouble(-100.0, 100.0)
      val y = BanishmentWorldController.LOWER_GRASS_HEIGHT + 32.0
      val z = Random.nextDouble(-100.0, 100.0)
      event.respawnLocation = Location(worldController.world, x, y, z)
      SlowFallingRunnable(event.player).runTaskLater(plugin, 1)
    }
  }

  override fun run() {
    worldController.world.setFullTime(MIDNIGHT)
  }
}
