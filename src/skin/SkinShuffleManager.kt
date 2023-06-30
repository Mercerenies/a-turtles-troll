
package com.mercerenies.turtletroll.skin

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.PlayerSelector
import com.mercerenies.turtletroll.packet.TrivialConverter
import com.mercerenies.turtletroll.nms.NMS
import com.mercerenies.turtletroll.http.MojangApi
import com.mercerenies.turtletroll.http.PluginUserAgentSupplier

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.entity.IronGolem
import org.bukkit.entity.EntityType
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketListener
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketEvent

import java.util.UUID

class SkinShuffleManager(
  plugin: Plugin,
) : AbstractFeature() {

  override val name: String = "skinshuffle"

  override val description: String = "Player skins are shuffled regularly"

  private val skinServer: SkinServer = run {
    val mojangApi = MojangApi(PluginUserAgentSupplier(plugin))
    SkinServer(mojangApi)
  }

  val playerInfoPacketListener: PacketListener = object : PacketAdapter(
    plugin,
    ListenerPriority.NORMAL,
    PacketType.Play.Server.PLAYER_INFO,
  ) {
    override fun onPacketSending(event: PacketEvent) {
      if (!isEnabled()) {
        return
      }
      val packet = event.packet
      val list = packet.getLists(TrivialConverter).read(0)
      for (elem in list) {
        val profile = NMS.infoPacketToGameProfile(elem!!)
        println("-----")
        println(profile)
        if (profile != null) {
          val newSkin = skinServer.querySkin(UUID.fromString("5d1e680a-18c7-4ec4-8bb6-ff2f2a842dec"))
          if (newSkin != null) {
            PlayerInfoPacketUpdater.replaceSkinTexture(profile, newSkin)
          }
        }
        println(profile)
      }
    }

  }

}
