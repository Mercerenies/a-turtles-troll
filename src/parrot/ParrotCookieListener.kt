
package com.mercerenies.turtletroll.parrot

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.Parrot
import org.bukkit.entity.HumanEntity

class ParrotCookieListener(
  private val parrotDuplicateCount: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private fun isCookie(material: Material): Boolean =
      material == Material.COOKIE

    fun wasDamagedByCookie(parrot: Parrot): Boolean {
      // So feeding a cookie to a parrot counts as attacking the
      // parrot with the cookie, apparently, as though you just poked
      // them with a sword or something.
      val lastDamage = parrot.lastDamageCause
      if (lastDamage is EntityDamageByEntityEvent) {
        val damager = lastDamage.damager
        if (damager is HumanEntity) {
          val inv = damager.inventory
          if (isCookie(inv.itemInMainHand.type)) {
            return true
          }
        }
      }
      return false
    }

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ParrotCookieListener(state.config.getInt("parrotcookie.parrot_duplicate_count")))

  }

  override val name = "parrotcookie"

  override val description = "Parrots duplicate when fed cookies"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.entity
    if (entity is Parrot) {
      if (wasDamagedByCookie(entity)) {
        repeat(parrotDuplicateCount) {
          val newParrot = entity.location.world!!.spawn(entity.location, Parrot::class.java)
          newParrot.variant = Parrot.Variant.values().toList().random()
          newParrot.isTamed = entity.isTamed
          newParrot.owner = entity.owner
        }
      }
    }

  }

}
