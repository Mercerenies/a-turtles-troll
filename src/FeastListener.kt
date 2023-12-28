
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.*

import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.util.Vector
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.entity.ThrownPotion
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FeastListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val POTION_EFFECT_DURATION = Constants.TICKS_PER_SECOND * 20

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(FeastListener())

    private fun createThrownPotion(location: Location): ThrownPotion {
      val item = ItemStack(Material.SPLASH_POTION)
      val meta = item.itemMeta as PotionMeta
      meta.clearCustomEffects()
      meta.addCustomEffect(PotionEffect(PotionEffectType.REGENERATION, POTION_EFFECT_DURATION, 1), true)
      item.itemMeta = meta
      val entity = location.world.spawn(location, ThrownPotion::class.java)
      entity.item = item
      entity.potionMeta = meta
      entity.velocity = Vector(0.0, -1.0, 0.0)
      return entity
    }

  }

  override val name = "feastupontheweak"

  override val description = "When a player dies, nearby entities get Regeneration"

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }
    createThrownPotion(event.entity.location)
  }

}
