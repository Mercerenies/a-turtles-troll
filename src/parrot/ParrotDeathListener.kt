
package com.mercerenies.turtletroll.parrot

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.gravestone.shape.GravestoneSpawner
import com.mercerenies.turtletroll.gravestone.Inscriptions
import com.mercerenies.turtletroll.gravestone.Rotation

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.Parrot
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.block.`data`.`type`.WallSign
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side
import org.bukkit.plugin.Plugin

import net.kyori.adventure.text.Component

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ParrotDeathListener(val plugin: Plugin) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val DELAY_SECONDS = 2L

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ParrotDeathListener(state.plugin))

  }

  private object ParrotGravestoneSpawner : GravestoneSpawner() {

    override fun spawnGravestone(centerBlock: Block, inscriptions: Inscriptions, rotation: Rotation) {

      // The stone itself
      replaceWithStone(centerBlock)

      // Now the sign
      val signBlock = centerBlock.location.clone().add(rotation.vector(1, 0, 0)).block
      replaceWith(signBlock, Material.BIRCH_WALL_SIGN)

      val blockData = signBlock.blockData
      if (blockData is WallSign) {
        blockData.setFacing(rotation.blockFace(BlockFace.EAST))
      }
      signBlock.blockData = blockData

      val blockState = signBlock.state
      if (blockState is Sign) {
        inscriptions.printTo(blockState.getSide(Side.FRONT))
      }
      blockState.update()

    }

  }

  private class ParrotInscriptions(
    val parrot: Parrot,
    val timestamp: LocalDateTime,
  ) : Inscriptions {

    override fun getLine(index: Int): Component =
      when (index) {
        0 -> {
          // Parrot
          parrot.customName() ?: Component.text("Parrot")
        }
        1 -> {
          // TODO Get display name (if player)?
          Component.text(parrot.owner?.getName() ?: "Died alone :(")
        }
        2 -> {
          Component.text(timestamp.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)))
        }
        else -> {
          Component.text("")
        }
      }

  }

  override val name = "parrotdeath"

  override val description = "Parrots get gravestones too"

  private class SpawnParrotGravestone(val block: Block, val cause: Inscriptions) : BukkitRunnable() {
    override fun run() {
      val rotation = Rotation.NONE
      val spawner = ParrotGravestoneSpawner
      spawner.spawnGravestone(block, cause, rotation)
    }
  }

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.entity
    if (entity is Parrot) {
      // In the name of sanity, don't do this and the
      // ParrotCookieListener thing at the same time (let the latter
      // take precedent).
      if (!ParrotCookieListener.wasDamagedByCookie(entity)) {
        val block = entity.location.block
        val inscription = ParrotInscriptions(entity, LocalDateTime.now())
        SpawnParrotGravestone(block, inscription).runTaskLater(plugin, Constants.TICKS_PER_SECOND * DELAY_SECONDS)
      }
    }

  }

}
