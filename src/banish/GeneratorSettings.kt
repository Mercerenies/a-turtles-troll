
package com.mercerenies.turtletroll.banish

import org.json.JSONObject
import org.json.JSONArray

data class GeneratorSettings(
  val biome: String,
  val layers: List<Layer>,
) {

  data class Layer(
    val block: String,
    val height: Int,
  ) {

    fun toJsonObject(): JSONObject =
      JSONObject(mapOf("block" to block, "height" to height))

  }

  fun toJsonObject(): JSONObject =
    JSONObject(
      mapOf(
        "biome" to biome,
        "layers" to JSONArray(layers.map { it.toJsonObject() }),
      )
    )

  fun toJsonString(): String =
    toJsonObject().toString()

}
