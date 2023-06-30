
package com.mercerenies.turtletroll.http

import com.mercerenies.turtletroll.util.*

import org.json.JSONObject
import org.json.JSONException

import org.bukkit.Bukkit

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.URI
import java.util.UUID

class MojangApi(
  private val userAgentSupplier: UserAgentSupplier,
) {

  companion object {

    private fun toJson(responseBody: String): JSONObject? =
      try {
        JSONObject(responseBody)
      } catch (e: JSONException) {
        Bukkit.getLogger().severe("Error during JSON parsing: $e")
        null
      }

    private fun uuidToString(uuid: UUID): String =
      uuid.toString().replace(Regex("-"), "").lowercase()

  }

  private fun httpRequest(uri: String): HttpRequest =
    httpRequest(URI(uri))

  private fun httpRequest(uri: URI): HttpRequest =
    HttpRequest.newBuilder(uri)
      .GET()
      .header("User-Agent", userAgentSupplier.getUserAgent())
      .build()

  private val httpClient = HttpClient.newHttpClient()

  fun readUserProfile(username: String): UserProfile? {
    val url = "https://api.mojang.com/users/profiles/minecraft/$username"
    val response = httpClient.send(httpRequest(url), HttpResponse.BodyHandlers.ofString())
    val statusCode = response.statusCode()
    if (!StatusCodes.isSuccessful(statusCode)) {
      Bukkit.getLogger().severe("Got status code $statusCode at $url")
      return null
    }
    return toJson(response.body()).andThen { UserProfile.fromJson(it) }
  }

  fun readSessionProfile(userId: UUID): SessionProfile? {
    val userIdString = uuidToString(userId)
    val url = "https://sessionserver.mojang.com/session/minecraft/profile/$userIdString"
    val response = httpClient.send(httpRequest(url), HttpResponse.BodyHandlers.ofString())
    val statusCode = response.statusCode()
    if (!StatusCodes.isSuccessful(statusCode)) {
      Bukkit.getLogger().severe("Got status code $statusCode at $url")
      return null
    }
    return toJson(response.body()).andThen { SessionProfile.fromJson(it) }
  }

}
