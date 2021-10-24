
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.dripstone.EqBlock
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.location.PlayerSelector
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.Llama
import org.bukkit.entity.TraderLlama
import org.bukkit.entity.LlamaSpit
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin
import org.bukkit.block.`data`.Levelled

import kotlin.random.Random

class LlamaHunterManager(
  plugin: Plugin,
  val speedMultiplier: Double = 5.5,
  val minLlamas: Int = 2,
) : RunnableFeature(plugin), Listener {

  companion object {

    fun getAllLlamas(): List<Llama> =
      Bukkit.getWorlds().flatMap { it.getEntitiesByClass(Llama::class.java) }

  }

  class MultiplySpeedRunnable(val target: Entity, val multiplier: Double) : BukkitRunnable() {
    override fun run() {
      target.velocity = target.velocity.multiply(multiplier)
    }
  }

  override val name: String = "llamahunter"

  override val description: String = "Llamas will hunt down a target player"

  override val taskPeriod: Long = 6L * Constants.TICKS_PER_SECOND.toLong()

  override fun run() {
    if (!isEnabled()) {
      return
    }

    val llamas = getAllLlamas()
    for (llama in llamas) {
      // Don't auto-anger trader llamas
      if (llama !is TraderLlama) {
        if (llama.target == null) {
          llama.target = PlayerSelector.findNearestPlayer(llama.location)
        }
      }
    }

  }

  @EventHandler
  fun onProjectileHit(event: ProjectileHitEvent) {
    if (!isEnabled()) {
      return
    }

    val projectile = event.entity
    val target = event.hitEntity
    if ((projectile is LlamaSpit) && (target != null)) {
      MultiplySpeedRunnable(target, speedMultiplier).runTaskLater(plugin, 1L)
    }
  }

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }

    if (event.entity.location.world?.environment != World.Environment.NORMAL) {
      // Only replace mobs with llamas in the overworld
      return
    }

    val llamaCount = getAllLlamas().size
    if (llamaCount < minLlamas) {
      val entity = event.entity
      if ((Mobs.isNonBossMob(entity)) && (entity !is Llama) && (entity.location.y >= BlockSelector.SEA_LEVEL)) {
        event.setCancelled(true)
        entity.world.spawnEntity(entity.location, EntityType.LLAMA)
      }
    }

  }

}
