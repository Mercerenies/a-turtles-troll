
package com.mercerenies.turtletroll.rain

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.bridge.ProtocolLibBridge

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketListener
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketEvent

import kotlin.collections.HashMap

class RainwaterManager(
  plugin: Plugin,
  private val deathRegistry: CustomDeathMessageRegistry,
) : RunnableFeature(plugin), Listener {

  init {
    ProtocolLibBridge.assertExists(
      errorMessage = "RainwaterManager cannot be constructed without ProtocolLib",
    )
  }

  private val oxygenMeters: HashMap<Player, RainOxygenMeter> = HashMap()

  private fun getOxygenMeter(player: Player): RainOxygenMeter =
    oxygenMeters.getOrPut(player) { RainOxygenMeter(player, deathRegistry) }

  val oxygenMeterPacketListener: PacketListener = object : PacketAdapter(
    plugin,
    ListenerPriority.NORMAL,
    PacketType.Play.Server.ENTITY_METADATA,
  ) {

    override fun onPacketSending(event: PacketEvent) {
      if (!isEnabled()) {
        return
      }
      val packet = event.packet
      if (packet.getIntegers().read(0) as Int != event.player.getEntityId()) {
        return
      }
      val oxygenMeter = getOxygenMeter(event.player)
      RainwaterPacketUpdater.updateBubbleCount(packet, oxygenMeter.getDesiredHudBubbleValue())
    }

  }

  override val name: String = "rainwater"

  override val description: String = "Players can drown in the rain"

  override val taskPeriod = Constants.TICKS_PER_SECOND / 4L - 1L

  private var tick: Int = 0

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val onlinePlayers = Bukkit.getOnlinePlayers()
    val protocolManager = ProtocolLibrary.getProtocolManager()
    tick = (tick + 1) % 4
    for (player in onlinePlayers) {
      val oxygenMeter = getOxygenMeter(player)
      if (tick == 0) {
        // Only do this every four times we run (approximately once
        // per second). But send the packet every time we run.
        oxygenMeter.runTick()
      }
      val bubbleValue = oxygenMeter.getDesiredHudBubbleValue()
      val packet = RainwaterPacketUpdater.createBubbleCountPacket(player, bubbleValue)
      protocolManager.sendServerPacket(player, packet)
    }
  }

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Player) {
      val oxygenMeter = getOxygenMeter(entity)
      oxygenMeter.fullyReplenish()
    }
  }

}
