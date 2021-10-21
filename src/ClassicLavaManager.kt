
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.dripstone.EqBlock
import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.plugin.Plugin
import org.bukkit.enchantments.Enchantment
import org.bukkit.block.Block
import org.bukkit.block.`data`.Levelled

import kotlin.random.Random

class ClassicLavaManager(val plugin: Plugin) : RunnableFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20L
    val IGNORE_DELAY_TIME = TICKS_PER_SECOND * 60L
  }

  override val name: String = "classiclava"

  override val description: String = "Lava spreads much further than usual"

  private var active: Boolean = false
  private var netherActive: Boolean = false
  private var memory: CooldownMemory<EqBlock> = CooldownMemory(plugin)

  val ignorer: BlockIgnorer =
    BlockIgnorer.MemoryIgnorer(memory, IGNORE_DELAY_TIME)

  @EventHandler
  fun onBlockFromTo(event: BlockFromToEvent) {
    if (!isEnabled()) {
      return
    }
    if (!isActiveFor(event.getBlock().world.environment)) {
      return
    }
    val from = event.getBlock()
    val to = event.getToBlock()
    if (from.type == Material.LAVA) {
      if (memory.contains(EqBlock(from))) {
        ignorer.ignore(to)
      } else {
        to.type = Material.LAVA
        val blockData = to.getBlockData()
        if (blockData is Levelled) {
          blockData.setLevel(0) // Make it a source block
          to.setBlockData(blockData)
          event.setCancelled(true)
        }
      }
    }
  }

  fun isActiveFor(env: World.Environment): Boolean =
    if (env == World.Environment.NETHER) {
      netherActive
    } else {
      active
    }

  fun register() {
    this.runTaskTimer(plugin, 1L, TICKS_PER_SECOND)
  }

  override fun run() {
    if (!isEnabled()) {
      return
    }

    // Overworld
    if (Random.nextDouble() < 0.4) {
      active = !active
    }

    // Nether
    netherActive = false

  }

}
