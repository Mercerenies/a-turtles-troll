
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.transformed.GhastSpawnerListener
import com.mercerenies.turtletroll.transformed.RavagerSpawnerListener
import com.mercerenies.turtletroll.durability.DoorDamageListener
import com.mercerenies.turtletroll.durability.ButtonDamageListener
import com.mercerenies.turtletroll.feature.Feature

import org.bukkit.plugin.Plugin
import org.bukkit.event.Listener

import kotlin.collections.Iterable

class AllPluginListeners(val plugin: Plugin) : Iterable<Listener> {
  val breakEvents = BlockBreakEvents()
  val chickenListener = ChickenDamageListener()
  val grassListener = GrassPoisonListener()
  val snowListener = SnowListener()
  val endStoneListener = EndStoneListener()
  val ghastListener = GhastSpawnerListener()
  val ravagerListener = RavagerSpawnerListener()
  val skeleListener = SkeletonWitherListener()
  val electricListener = ElectricWaterListener(plugin)
  val blazeListener = BlazeAttackListener(plugin)
  val zombifyListener = ZombifyTradeListener()
  val leavesListener = LeavesFireListener(plugin)
  val roseListener = WitherRoseListener()
  val doorListener = DoorDamageListener()
  val buttonListener = ButtonDamageListener()
  val angelManager = WeepingAngelManager(plugin)
  val levitationListener = LevitationListener()
  val plateListener = PressurePlateFireListener()

  fun getListeners(): List<Listener> =
    listOf(
      breakEvents.listener, chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener, electricListener,
      blazeListener, zombifyListener, leavesListener, roseListener,
      endStoneListener, doorListener, angelManager, levitationListener,
      buttonListener, plateListener,
    )

  fun getFeatures(): List<Feature> =
    listOf(
      chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener,
      blazeListener, zombifyListener, leavesListener,
      roseListener, endStoneListener, doorListener,
      angelManager, levitationListener, buttonListener,
      plateListener,
    ) + breakEvents.getFeatures()

  override fun iterator(): Iterator<Listener> =
    getListeners().iterator()

}
