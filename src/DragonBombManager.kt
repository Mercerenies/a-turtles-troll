
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.CooldownMemory
import com.mercerenies.turtletroll.BlockSelector
import com.mercerenies.turtletroll.ext.*

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Chunk
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.TNTPrimed
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import kotlin.random.Random

class DragonBombManager(val plugin: Plugin) : RunnableFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20L
  }

  override val name = "dragonbomb"

  override val description = "The Ender Dragon drops TNT regularly"

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val server = Bukkit.getServer()
    for (world in server.getWorlds()) {
      for (dragon in world.getEntitiesByClass(EnderDragon::class.java)) {
        val tnt = world.spawn(dragon.getLocation(), TNTPrimed::class.java)
        tnt.setFuseTicks((TICKS_PER_SECOND * 3).toInt())
      }
    }
  }

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.getEntity()

    // Protect dragon from all explosions
    if (entity is EnderDragon) {
      if ((event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
        event.setCancelled(true)
      }
    }

  }

  fun register() {
    this.runTaskTimer(plugin, 1L, TICKS_PER_SECOND * 3L)
  }

}
