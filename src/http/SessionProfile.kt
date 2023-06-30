
package com.mercerenies.turtletroll.http

import com.mercerenies.turtletroll.util.UUIDs

import org.bukkit.Bukkit

import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException

import java.util.UUID

data class SessionProfile(
  val id: UUID,
  val name: String,
  val properties: List<Property>,
) {

  companion object {

    fun fromJson(obj: JSONObject): SessionProfile? =
      try {
        SessionProfile(
          id = UUIDs.stringToUuid(obj.getString("id")),
          name = obj.getString("name"),
          properties = parseProperties(obj.getJSONArray("properties")),
        )
      } catch (e: JSONException) {
        Bukkit.getLogger().severe("Error during SessionProfile JSON parsing: $e")
        null
      } catch (e: IllegalArgumentException) {
        Bukkit.getLogger().severe("Error during SessionProfile JSON parsing: $e")
        null
      }

    private fun parseProperties(array: JSONArray): List<Property> =
      JSONArrays.toTypedList(array) { index ->
        val obj = array.getJSONObject(index)
        Property(
          name = obj.getString("name"),
          value = obj.getString("value"),
        )
      }

  }

  data class Property(
    val name: String,
    val value: String,
  )

  fun getProperty(name: String): String? =
    properties.find { it.name == name }?.value

}
