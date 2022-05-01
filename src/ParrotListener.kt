
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.nms.NMS

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.entity.EntityType
import org.bukkit.entity.Parrot
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.spigotmc.event.entity.EntityMountEvent

class ParrotListener(val plugin: Plugin): AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ParrotListener(state.plugin))

    private val targetEffectType: PotionEffectType = PotionEffectType.LEVITATION

  }

  override val name = "parrots"

  override val description = "TBA"

  private val safePlayers = CooldownMemory<Player>(plugin);

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.player
    val info = NMS.getPlayerParrotInfo(player)
    val hasPerchedParrot = info.hasAnyShoulderPerch()
    if (hasPerchedParrot) {
      if (!safePlayers.contains(player)) {
        if (!player.hasPotionEffect(targetEffectType)) {
          player.sendMessage("SQUAAAAAAWK! Yer comin' with me!")
          player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, Constants.TICKS_PER_SECOND * 3, 100))
          safePlayers.add(player, Constants.TICKS_PER_SECOND * 10L)
        }
      }
    }
  }

}
