
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.transformed.GhastSpawnerListener
import com.mercerenies.turtletroll.transformed.RavagerSpawnerListener
import com.mercerenies.turtletroll.durability.DoorDamageListener
import com.mercerenies.turtletroll.durability.ButtonDamageListener
import com.mercerenies.turtletroll.angel.WeepingAngelManager
import com.mercerenies.turtletroll.mimic.MimicListener
import com.mercerenies.turtletroll.feature.Feature

import org.bukkit.plugin.Plugin
import org.bukkit.event.Listener

import kotlin.collections.Iterable

class AllPluginListeners(val plugin: Plugin) : Iterable<Listener> {
  val pumpkinManager = PumpkinSlownessManager()
  val angelManager = WeepingAngelManager(plugin)
  val phantomManager = PetPhantomManager(plugin)

  val breakEvents = BlockBreakEvents()
  val chickenListener = ChickenDamageListener()
  val grassListener = GrassPoisonListener()
  val snowListener = SnowListener()
  val endStoneListener = EndStoneListener()
  val ghastListener = GhastSpawnerListener()
  val ravagerListener = RavagerSpawnerListener()
  val skeleListener = SkeletonWitherListener()
  val electricListener = ElectricWaterListener(plugin, pumpkinManager)
  val blazeListener = BlazeAttackListener(plugin)
  val zombifyListener = ZombifyTradeListener()
  val leavesListener = LeavesFireListener(plugin)
  val roseListener = WitherRoseListener()
  val doorListener = DoorDamageListener()
  val buttonListener = ButtonDamageListener()
  val levitationListener = LevitationListener()
  val plateListener = PressurePlateFireListener()
  val slabListener = SlowSlabListener()
  val lightListener = BreakLightOnSightListener(plugin)
  val lavaListener = LavaLaunchListener()
  val mimicListener = MimicListener(plugin)

  fun getListeners(): List<Listener> =
    listOf(
      breakEvents.listener, chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener, electricListener,
      blazeListener, zombifyListener, leavesListener, roseListener,
      endStoneListener, doorListener, angelManager, levitationListener,
      buttonListener, plateListener, slabListener, lightListener,
      phantomManager, lavaListener, pumpkinManager, mimicListener,
    )

  fun getFeatures(): List<Feature> =
    listOf(
      chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener,
      blazeListener, zombifyListener, leavesListener,
      roseListener, endStoneListener, doorListener,
      angelManager, levitationListener, buttonListener,
      plateListener, slabListener, lightListener,
      phantomManager, lavaListener, pumpkinManager,
      mimicListener,
    ) + breakEvents.getFeatures()

  override fun iterator(): Iterator<Listener> =
    getListeners().iterator()

}
