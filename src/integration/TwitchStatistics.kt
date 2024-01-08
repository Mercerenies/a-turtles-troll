
package com.mercerenies.turtletroll.integration

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.util.tryCancel

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import org.bukkit.plugin.Plugin

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.logging.Logger
import java.util.logging.Level
import javax.swing.text.html.parser.Parser
import javax.swing.text.html.parser.DTD

class TwitchStatistics(
  private val plugin: Plugin,
) {

  companion object {
    private val TASK_DELAY = 1L // Immediate
    private val TASK_TIMER = 15L * Constants.TICKS_PER_MINUTE.toLong() // 15 minutes

    private val STATISTICS_URL = "https://streamscharts.com/games/minecraft"

    private var _singleton: TwitchStatistics? = null

    val singleton: TwitchStatistics
      get() = _singleton ?: throw IllegalStateException("TwitchStatistics has not been initialized yet")

    fun initializeSingleton(plugin: Plugin) {
      if (_singleton == null) {
        _singleton = TwitchStatistics(plugin)
      }
    }

  }

  private class HtmlParser(
    private val logger: Logger,
  ) : Parser(DTD.getDTD("html")) {

    private val firstMatchText = "Channels now:"
    private val secondMatchRegex = """\s*\d[\d\s]*""".toRegex()

    // 0 = looking for firstMatchText
    // 1 = looking for secondMatchRegex
    // 2 = done
    private var matchState = 0

    private var _channelsNowStreaming: Long? = null

    val channelsNowStreaming: Long?
      get() = _channelsNowStreaming

    protected override fun handleText(text: CharArray) {
      val string = String(text)
      when (matchState) {
        0 -> {
          if (string == firstMatchText) {
            matchState = 1
          }
        }
        1 -> {
          if (secondMatchRegex.matches(string)) {
            try {
              _channelsNowStreaming = string.replace("\\s".toRegex(), "").toLong()
              matchState = 2
            } catch (e: NumberFormatException) {
              logger.warning("Failed to parse Twitch response $string as a number")
            }
          }
        }
        else -> {
          // Done; do nothing.
        }
      }
    }

  }

  private inner class QueryStatisticsRunnable(
    private val logger: Logger,
  ) : Runnable {
    override fun run() {
      logger.info("Querying Twitch statistics")
      try {
        val inputStream = Requests.get(STATISTICS_URL)
        BufferedReader(InputStreamReader(inputStream))
      } catch (e: Exception) {
        logger.log(Level.SEVERE, "Failed to query Twitch statistics", e)
        return
      }.use { reader ->
        val parser = HtmlParser(logger)
        parser.parse(reader)
        val channelsNowStreaming = parser.channelsNowStreaming
        if (channelsNowStreaming != null) {
          _latestStreamerCount = channelsNowStreaming
        } else {
          logger.warning("Could not find Twitch statistics in HTML response")
        }
      }
    }
  }

  private var listenerTask: BukkitTask? = null

  @Volatile private var _latestStreamerCount: Long? = null

  val latestStreamerCount: Long?
    get() = _latestStreamerCount

  init {
    startListening()
  }

  fun startListening() {
    if (listenerTask != null) {
      return // Already listening
    }
    listenerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
      plugin,
      QueryStatisticsRunnable(Bukkit.getLogger()),
      TASK_DELAY,
      TASK_TIMER,
    )
  }

  fun stopListening() {
    listenerTask?.tryCancel()
    listenerTask = null
  }

}
