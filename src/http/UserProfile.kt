
package com.mercerenies.turtletroll.http

import com.mercerenies.turtletroll.util.stringToUuid

import org.bukkit.Bukkit

import org.json.JSONObject
import org.json.JSONException

import java.util.UUID

data class UserProfile(
  val id: UUID,
  val name: String,
) {
  companion object {
    fun fromJson(obj: JSONObject): UserProfile? =
      try {
        UserProfile(
          id = stringToUuid(obj.getString("id")),
          name = obj.getString("name"),
        )
      } catch (e: JSONException) {
        Bukkit.getLogger().severe("Error during UserProfile JSON parsing: $e")
        null
      } catch (e: IllegalArgumentException) {
        Bukkit.getLogger().severe("Error during UserProfile JSON parsing: $e")
        null
      }
  }
}
