
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedLeaveEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SweetDreamsListener(
  val effectsPool: List<(Int) -> PotionEffect>,
  val effectsCount: Int = 3
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val DEFAULT_EFFECTS_POOL: List<(Int) -> PotionEffect> = listOf(
      { length -> PotionEffect(PotionEffectType.SLOW, length, 1) },
      { length -> PotionEffect(PotionEffectType.CONFUSION, length, 0) },
      { length -> PotionEffect(PotionEffectType.BLINDNESS, length, 0) },
      { length -> PotionEffect(PotionEffectType.SLOW_DIGGING, length, 4) },
      { length -> PotionEffect(PotionEffectType.REGENERATION, length, 4) },
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SweetDreamsListener(effectsPool = DEFAULT_EFFECTS_POOL))

    fun cureAllEffects(entity: LivingEntity) {
      for (effect in entity.getActivePotionEffects().toList()) {
        entity.removePotionEffect(effect.type)
      }
    }

  }

  override val name = "sweetdreams"

  override val description = "After lying in a bed, you wake up drowsy"

  @EventHandler
  fun onPlayerBedLeave(event: PlayerBedLeaveEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.player
    cureAllEffects(player)
    val time = Constants.TICKS_PER_SECOND * 60
    val effects = ArrayList(effectsPool)
    effects.shuffle()
    for (effect in effects.take(effectsCount)) {
      player.addPotionEffect(effect(time))
    }
  }

}
