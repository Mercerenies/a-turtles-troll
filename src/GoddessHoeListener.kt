
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Player
import org.bukkit.entity.LivingEntity
import org.bukkit.Material
import org.bukkit.plugin.Plugin

class GoddessHoeListener(
  val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val MAX_DURABILITY = 2031 // Max durability for netherite hoe
    override fun create(state: BuilderState): FeatureContainer = 
      ListenerContainer(GoddessHoeListener(state.plugin))

  }

  override val name = "goddesshoe"

  override val description = "Netherrite hoes deal one-shot KOs to any non-boss mob"

  @EventHandler
  fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
    if (!isEnabled()) {
      return
    }
    val victim = event.entity
    val attacker = event.damager
    if ((attacker is Player) && (victim is LivingEntity) && (Mobs.isNonBossMob(victim))) {
      val weapon = attacker.inventory.getItemInMainHand()
      if (weapon.type == Material.NETHERITE_HOE) {
        event.setDamage(99999.0)
        victim.world.strikeLightningEffect(victim.location)
      }
    }
  }

}
