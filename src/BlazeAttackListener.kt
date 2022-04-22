
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.entity.Blaze
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin

class BlazeAttackListener(val plugin: Plugin) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val COOLDOWN_TIME = Constants.TICKS_PER_SECOND * 10

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(BlazeAttackListener(state.plugin))

  }

  private val memory = CooldownMemory<Blaze>(plugin)

  override val name = "blazepower"

  override val description = "Blazes will spawn Evokers"

  @EventHandler
  fun onProjectileLaunch(event: ProjectileLaunchEvent) {
    if (!isEnabled()) {
      return
    }
    val projectile = event.entity
    val source = projectile.shooter
    if (source is Blaze) {
      if (!memory.contains(source)) {
        event.setCancelled(true)
        projectile.location.world!!.spawnEntity(projectile.location, EntityType.EVOKER)
        memory.add(source, COOLDOWN_TIME.toLong())
      }
    }
  }

}
