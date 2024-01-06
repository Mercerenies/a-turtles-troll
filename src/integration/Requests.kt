
package com.mercerenies.turtletroll.integration

import java.time.Duration
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.io.InputStream

// Helpers for making HTTP requests.
object Requests {

  // Hello, fellow Web Browsers, my name is Firefox :)
  private val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101"

  private val httpClient = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .followRedirects(HttpClient.Redirect.NORMAL)
    .connectTimeout(Duration.ofSeconds(5))
    .build()

  fun get(uri: URI): InputStream {
    val request = HttpRequest.newBuilder()
      .GET()
      .uri(uri)
      .setHeader("User-Agent", USER_AGENT)
      .setHeader("Accept", "*/*")
      .build()
    val response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream())
    HttpResponseException.assertHttp2xx(response.statusCode())
    return response.body()
  }

  fun get(spec: String): InputStream =
    get(URI(spec))

}
