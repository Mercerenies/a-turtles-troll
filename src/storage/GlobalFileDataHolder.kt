
package com.mercerenies.turtletroll.storage

import org.bukkit.plugin.Plugin

import org.json.JSONObject

import java.io.File
import kotlin.text.Charsets

class GlobalFileDataHolder(
  private val plugin: Plugin,
) : GlobalDataHolder {

  companion object {

    private val DATA_FILENAME = "data.dat"

  }

  private val dataFile = File(plugin.getDataFolder(), DATA_FILENAME)

  private var map: JSONObject = loadMap()

  private fun loadMap(): JSONObject =
    try {
      plugin.getDataFolder().mkdir()
      val contents = dataFile.readText(Charsets.UTF_8)
      JSONObject(contents)
    } catch (_exc: Exception) {
      JSONObject("{}") // Assume empty or nonexistent file.
    }

  override fun getData(key: String): String? =
    try {
      map.getString(key)
    } catch (_exc: Exception) {
      null
    }

  override fun putData(key: String, value: String) {
    map.put(key, value)
  }

  fun reload() {
    map = loadMap()
  }

  fun save() {
    dataFile.writeText(map.toString(), Charsets.UTF_8)
  }

}
