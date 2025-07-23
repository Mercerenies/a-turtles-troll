
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.dripstone.EqBlock

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.entity.Bee
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType

import net.kyori.adventure.text.Component

import kotlin.random.Random

import java.util.UUID

class BoomBedListener(
    val plugin: Plugin,
) : AbstractFeature(), Listener {
  companion object : FeatureContainerFactory<FeatureContainer> {
    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(BoomBedListener(state.plugin))

    private fun makeDamageSource(location: Location): DamageSource =
        DamageSource.builder(DamageType.BAD_RESPAWN_POINT)
        .withDamageLocation(location)
        .build()
  }

  override val name = "boombed"

  override val description = "Sleeping in an exploding bed always kills the person who slept in it"

  private val recentlySleptBeds: CooldownMemoryMap<EqBlock, UUID> =
      CooldownMemoryMap(plugin)

  @EventHandler
  fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.bedEnterResult == PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_HERE) {
      recentlySleptBeds.add(EqBlock(event.bed), event.player.getUniqueId(), Constants.TICKS_PER_SECOND * 10L)
    }
  }

  @EventHandler
  fun onBlockExplode(event: BlockExplodeEvent) {
    if (!isEnabled()) {
      return
    }
    val server = Bukkit.getServer()
    val placingPlayer = recentlySleptBeds.get(EqBlock(event.block))?.let { server.getPlayer(it) }
    if (placingPlayer != null) {
      placingPlayer.damage(9999.0, makeDamageSource(event.block.location))
    }
  }
}
