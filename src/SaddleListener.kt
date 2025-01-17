
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Steerable
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.Chunk
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.entity.EntityDismountEvent

class SaddleListener(
  val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SaddleListener(state.plugin))

    private fun getPotionEffectForMount(entityType: EntityType): PotionEffect? =
      when (entityType) {
        EntityType.PIG -> {
          PotionEffect(PotionEffectType.SPEED, Constants.TICKS_PER_SECOND * 999, 100)
        }
        EntityType.STRIDER -> {
          PotionEffect(PotionEffectType.SLOWNESS, Constants.TICKS_PER_SECOND * 999, 2)
        }
        else -> {
          null
        }
      }

  }

  private inner class SaddlePigsAndStriders(_chunk: Chunk) : ReplaceMobsRunnable(_chunk) {

    override fun replaceWith(entity: Entity): EntityType? =
      null

    override fun onUnreplacedMob(entity: Entity) {
      super.onUnreplacedMob(entity)
      if (entity is Steerable) {
        entity.setSaddle(true)
      }
    }

  }

  override val name = "pigs"

  override val description = "Saddles all pigs and striders and applies speed changes when ridden"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    val entity = event.entity
    if (entity is Steerable) {
      entity.setSaddle(true)
    }
  }

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val chunk = event.getChunk()
    SaddlePigsAndStriders(chunk).schedule(plugin)
  }

  @EventHandler
  fun onEntityMount(event: EntityMountEvent) {
    if (!isEnabled()) {
      return
    }
    val mount = event.mount
    if (mount is LivingEntity) {
      val effect = getPotionEffectForMount(mount.type)
      if (effect != null) {
        mount.addPotionEffect(effect)
      }
    }
  }

  @EventHandler
  fun onEntityDismount(event: EntityDismountEvent) {
    if (!isEnabled()) {
      return
    }
    val mount = event.dismounted
    if (mount is LivingEntity) {
      val effect = getPotionEffectForMount(mount.type)
      if (effect != null) {
        mount.removePotionEffect(effect.type)
      }
    }
  }

}
