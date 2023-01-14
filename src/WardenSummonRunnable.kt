
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RunnableContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.entity.Warden
import org.bukkit.entity.Player
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.block.Block
import org.bukkit.Bukkit
import org.bukkit.World

import kotlin.collections.HashSet
import kotlin.math.max

class WardenSummonRunnable(plugin: Plugin) : RunnableFeature(plugin) {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      RunnableContainer(WardenSummonRunnable(state.plugin))

  }

  private class SummonWardenRunnable(val block: Block, val player: Player) : BukkitRunnable() {
    override fun run() {
      val warden = block.world.spawn(block.location, Warden::class.java)
      warden.setAnger(player, 75) // Just below the 80 threshold for an active attack :)
    }
  }

  override val name: String = "wardensummon"

  override val description: String = "A Warden appears if you spend too much time in 0 light"

  override val taskPeriod = (Constants.TICKS_PER_SECOND * 2.5).toLong()

  private val darkPlayers = HashSet<Player>()
  private val recentSummons = CooldownMemory<Player>(plugin)

  override fun run() {
    if (!isEnabled()) {
      return
    }

    // If someone is in the dark, add them to the dark players list.
    // If they were already in this list from last time,
    for (player in Bukkit.getOnlinePlayers()) {
      if (player.location.world?.environment != World.Environment.NORMAL) {
        // Do NOT try this in the nether or the end. It's basically
        // always dark in both of those.
        continue
      }
      val block = player.location.block
      val lightLevel = max(block.lightFromBlocks.toInt(), block.lightFromSky.toInt())
      if (lightLevel == 0) {
        if (darkPlayers.contains(player)) {
          summonWarden(player)
          darkPlayers.remove(player)
        } else {
          darkPlayers.add(player)
        }
      } else {
        darkPlayers.remove(player)
      }
    }
  }

  private fun summonWarden(sourcePlayer: Player): Unit {
    if (recentSummons.contains(sourcePlayer)) {
      // In the name of sanity, don't spam wardens on the same player
      // in rapid succession.
      return
    }

    SummonWardenRunnable(sourcePlayer.location.block, sourcePlayer).runTaskLater(plugin, Constants.TICKS_PER_SECOND * 2L)
    recentSummons.add(sourcePlayer, Constants.TICKS_PER_SECOND * 60L)

  }

}
