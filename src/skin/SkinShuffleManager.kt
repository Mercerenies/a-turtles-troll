
package com.mercerenies.turtletroll.skin

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.PlayerSelector
import com.mercerenies.turtletroll.packet.TrivialConverter

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

class SkinShuffleManager(
  plugin: Plugin,
) : AbstractFeature() {

  override val name: String = "skinshuffle"

  override val description: String = "Player skins are shuffled regularly"

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
      println("------------")
      for (elem in list) {
        println(elem)
        val cls = Class.forName("net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket\$b")
        val profileCls = Class.forName("com.mojang.authlib.GameProfile")
        val propMapCls = Class.forName("com.mojang.authlib.properties.PropertyMap")
        val propCls = Class.forName("com.mojang.authlib.properties.Property")
        if (cls.isInstance(elem)) {
          val profile = cls.getMethod("b").invoke(elem)
          val map = profileCls.getMethod("getProperties").invoke(profile)
          val props = propMapCls.getMethod("get", Object::class.java).invoke(map, "textures") as Collection<*>
          for (prop in props) {
            println(propCls.getMethod("getValue").invoke(prop))
          }
        } else {
          println("not relevant")
        }
      }
    }

  }

}
