
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class WitherRoseListener : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(WitherRoseListener())
  }

  private val bootsDamager = BootsDamager()

  override val name = "witherrose"

  override val description = "All flowers behave like wither roses"

  @EventHandler
  fun onPlayerMove(event: PlayerMoveEvent) {
    if (!isEnabled()) {
      return
    }
    val block = event.getTo().getBlock()
    if (BlockTypes.FLOWERS.contains(block.type)) {
      if (!bootsDamager.tryWearDownBoots(event.player)) {
        applyWither(event.player)
      }
    }
  }

  @EventHandler
  fun onEntityPickupItemEvent(event: EntityPickupItemEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity !is Player) {
      return
    }

    val itemStack = event.item.itemStack
    if (BlockTypes.FLOWERS.contains(itemStack.type)) {
      applyWither(entity)
    }

  }

  private fun applyWither(player: Player) {
    player.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 2 * Constants.TICKS_PER_SECOND, 0))
  }

}
