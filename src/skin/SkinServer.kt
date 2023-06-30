
package com.mercerenies.turtletroll.skin

import com.mercerenies.turtletroll.http.MojangApi

import org.bukkit.entity.Player

import java.util.UUID
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import kotlin.collections.HashMap

// Provider for Minecraft skins based on player identity. This class
// caches results, to avoid spamming mojang.com.
class SkinServer(
  private val mojangApi: MojangApi,
  private val expirationMinutes: Long = 360L,
) {
  private data class CacheEntry(
    val encodedString: String,
    val date: LocalDateTime,
  )

  private val skinCache: HashMap<UUID, CacheEntry> = HashMap()

  fun querySkin(playerUuid: UUID): String? {
    val now = LocalDateTime.now()
    val cacheEntry = skinCache[playerUuid]
    if ((cacheEntry != null) && (!isExpired(cacheEntry.date, now))) {
      return cacheEntry.encodedString
    }

    val textureString = mojangApi.readSessionProfile(playerUuid)?.getProperty("textures")
    if (textureString == null) {
      return null
    }
    skinCache[playerUuid] = CacheEntry(textureString, now)
    return textureString
  }

  fun querySkin(player: Player): String? =
    querySkin(player.getUniqueId())

  private fun isExpired(cacheDate: LocalDateTime, now: LocalDateTime): Boolean =
    ChronoUnit.MINUTES.between(cacheDate, now) > expirationMinutes

}
