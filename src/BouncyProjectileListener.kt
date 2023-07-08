
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.plugin.Plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

import kotlin.math.PI
import kotlin.random.Random

class BouncyProjectileListener(
  private val plugin: Plugin,
  private val entities: Set<EntityType>,
  private val splitCount: Int,
  private val splitIterations: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val BOUNCY_MARKER_KEY = "bouncy_projectile_tag"

    val VELOCITY_MULTIPLIER = 1.2

    val DEFAULT_ENTITY_SET = setOf(
      EntityType.ARROW, EntityType.SPECTRAL_ARROW,
    )

    override fun create(state: BuilderState): FeatureContainer {
      return ListenerContainer(
        BouncyProjectileListener(
          plugin = state.plugin,
          entities = DEFAULT_ENTITY_SET,
          splitCount = state.config.getInt("bouncyprojectile.split_count"),
          splitIterations = state.config.getInt("bouncyprojectile.split_iterations"),
        )
      )
    }

    private fun addNoise(vector: Vector): Vector {
      val newVector = vector.clone()
      newVector.rotateAroundX(Random.nextDouble(-0.1, 0.1))
      newVector.rotateAroundY(Random.nextDouble(-0.1, 0.1))
      newVector.rotateAroundZ(Random.nextDouble(-0.1, 0.1))
      return newVector
    }

  }

  override val name = "bouncyprojectile"

  override val description = "Certain projectile types bounce off walls"

  private val markerKey = NamespacedKey(plugin, BOUNCY_MARKER_KEY)

  private fun getMarker(entity: Entity): Int? =
    entity.persistentDataContainer.get(markerKey, PersistentDataType.INTEGER)

  private fun setMarker(entity: Entity, value: Int) {
    entity.persistentDataContainer.set(markerKey, PersistentDataType.INTEGER, value)
  }

  @EventHandler
  fun onProjectileHit(event: ProjectileHitEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    val blockFace = event.getHitBlockFace()
    if (ExplosiveArrowManager.isExplosiveArrow(plugin, entity)) {
      // Do not duplicate explosive arrows, because holy cow.
      return
    }

    if ((entity.type in entities) && (blockFace != null)) {
      val bounceCount = getMarker(entity) ?: 0
      if (bounceCount < splitIterations) {
        splitEntity(entity, blockFace.direction, bounceCount + 1)
      }
    }
  }

  private fun splitEntity(entity: Entity, normalVector: Vector, newBounceCount: Int) {
    val newVelocity =
      entity.velocity.clone()
        .rotateAroundAxis(normalVector, PI)
        .multiply(- VELOCITY_MULTIPLIER)
    repeat(splitCount) {
      val newEntity = entity.world.spawnEntity(entity.location, entity.type)
      newEntity.velocity = addNoise(newVelocity)
      setMarker(newEntity, newBounceCount)
    }
  }

}
