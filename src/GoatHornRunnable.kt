
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ScheduledEventRunnable
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RunnableContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.util.removeInPlace

import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.RegistryAccess

import org.bukkit.plugin.Plugin
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MusicInstrumentMeta
import org.bukkit.Material
import org.bukkit.Bukkit
import org.bukkit.MusicInstrument

class GoatHornRunnable(plugin: Plugin) : ScheduledEventRunnable<GoatHornRunnable.State>(plugin) {
  companion object : FeatureContainerFactory<FeatureContainer> {
    val DAWN_TIME = 0L
    val DUSK_TIME = 12000L

    val STATES = listOf(
      Event(State.Daytime, DAWN_TIME),
      Event(State.Nighttime, DUSK_TIME),
    )

    override fun create(state: BuilderState): FeatureContainer =
      RunnableContainer(GoatHornRunnable(state.plugin))

    private fun removeHorns(inv: Inventory) {
      inv.remove(Material.GOAT_HORN)
    }

    private fun makeHorn(): ItemStack {
      val stack = ItemStack(Material.GOAT_HORN, 1)
      val meta = stack.itemMeta as MusicInstrumentMeta
      meta.instrument = PaperRegistry.valuesOf(RegistryKey.INSTRUMENT).random()
      stack.itemMeta = meta
      return stack
    }
  }

  enum class State {
    Daytime,
    Nighttime,
  }

  override val name: String = "horny"

  override val description: String = "Everybody gets a goat horn at dawn"

  override fun getAllStates() = STATES

  override fun onStateShift(newState: State) {
    if (newState == State.Daytime) {
      for (player in Bukkit.getOnlinePlayers()) {
        removeHorns(player.inventory)
        AllItems.give(player, makeHorn())
      }
    }
  }
}
