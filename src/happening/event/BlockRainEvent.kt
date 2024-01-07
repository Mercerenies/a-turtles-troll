
package com.mercerenies.turtletroll.happening.event

import com.mercerenies.turtletroll.happening.NotifiedRandomEvent
import com.mercerenies.turtletroll.happening.RandomEventState
import com.mercerenies.turtletroll.util.runTaskTimer
import com.mercerenies.turtletroll.falling.BlockDropper

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

import java.util.function.Consumer

// A random event that makes blocks fall from the sky on all players
// at regular intervals for a period of time.
abstract class BlockRainEvent(
  private val plugin: Plugin,
) : NotifiedRandomEvent(plugin) {
  abstract val blockDropper: BlockDropper
  abstract val ticksBetweenDrops: Long
  abstract val dropCount: Int

  private inner class BlockDropConsumer(
    private var remainingDrops: Int,
  ) : Consumer<BukkitTask> {

    override fun accept(task: BukkitTask) {
      if (remainingDrops <= 0) {
        task.cancel()
        return
      }
      remainingDrops--
      for (player in Bukkit.getOnlinePlayers()) {
        blockDropper.doDrop(player)
      }
    }

  }

  open override fun canFire(state: RandomEventState): Boolean =
    true

  override fun onAfterDelay(state: RandomEventState) {
    Bukkit.getScheduler().runTaskTimer(plugin, BlockDropConsumer(dropCount), 1L, ticksBetweenDrops)
  }

}
