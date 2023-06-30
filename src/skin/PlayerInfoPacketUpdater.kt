
package com.mercerenies.turtletroll.skin

import com.mercerenies.turtletroll.nms.NMS
import com.mercerenies.turtletroll.packet.TrivialConverter

import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.PacketType

import org.bukkit.entity.Entity

import kotlin.math.min

object PlayerInfoPacketUpdater {

  private val ADDRESS_ID: Int = 1

  fun getBubbleCount(packet: PacketContainer): Int? {
    val list = packet.getLists(TrivialConverter).read(0)
    for (elem in list) {
      if (elem != null) {
        val meta = NMS.getEntityMetadata(elem)
        if ((meta != null) && (meta.id == ADDRESS_ID) && (meta.value is Int)) {
          return meta.value as Int
        }
      }
    }
    return null
  }

  fun updateBubbleCount(packet: PacketContainer, newBubbleCount: Int) {
    val list = packet.getLists(TrivialConverter).read(0)
    var found = false
    for (idx in list.indices) {
      val elem = list[idx]
      if (elem != null) {
        val meta = NMS.getEntityMetadata(elem)
        if ((meta != null) && (meta.id == ADDRESS_ID) && (meta.value is Int)) {
          val oldBubbleCount = meta.value as Int
          meta.value = min(newBubbleCount, oldBubbleCount)
          found = true
          list[idx] = meta.getHandle()
        }
      }
    }
    // If this packet did not contain a bubble count, append it onto
    // the end.
    if (!found) {
      list.add(NMS.constructEntityMetadataInt(ADDRESS_ID, newBubbleCount).getHandle())
    }
    packet.getLists(TrivialConverter).write(0, list)
  }

  fun createBubbleCountPacket(entity: Entity, bubbleCount: Int): PacketContainer {
    val packet = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
    packet.getModifier().write(0, entity.getEntityId())
    val lst = listOf(NMS.constructEntityMetadataInt(ADDRESS_ID, bubbleCount).getHandle())
    packet.getModifier().write(1, lst)
    return packet
  }

}
