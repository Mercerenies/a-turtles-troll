
package com.mercerenies.turtletroll.rain

import com.mercerenies.turtletroll.nms.NMS

import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.reflect.EquivalentConverter
import com.comphenix.protocol.PacketType

import org.bukkit.entity.Entity

import kotlin.math.min

object RainwaterPacketUpdater {

  object TrivialConverter : EquivalentConverter<Any?> {

    override fun getGeneric(specific: Any?): Any? =
      specific

    override fun getSpecific(generic: Any?): Any? =
      generic

    // We need Class<Any?> for the type. Java doesn't care about
    // nullability, so Class<Any> is good enough.
    @Suppress("UNCHECKED_CAST")
    override fun getSpecificType(): Class<Any?> =
      Any::class.java as Class<Any?>

  }

  private val ADDRESS_ID: Int = 1

  fun getBubbleCount(packet: PacketContainer): Int? {
    val list = packet.getLists(TrivialConverter).read(0)
    for (elem in list) {
      if (elem != null) {
        val meta = NMS.getEntityMetadata(elem)
        if ((meta != null) && (meta.id == ADDRESS_ID)) {
          return meta.value
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
        if ((meta != null) && (meta.id == ADDRESS_ID)) {
          val oldBubbleCount = meta.value
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
