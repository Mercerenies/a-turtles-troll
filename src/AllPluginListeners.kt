
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.chicken.ChickenDamageListener
import com.mercerenies.turtletroll.transformed.GhastSpawnerListener
import com.mercerenies.turtletroll.transformed.RavagerSpawnerListener
import com.mercerenies.turtletroll.durability.DoorDamageListener
import com.mercerenies.turtletroll.durability.ButtonDamageListener
import com.mercerenies.turtletroll.angel.WeepingAngelManager
import com.mercerenies.turtletroll.mimic.MimicListener
import com.mercerenies.turtletroll.egg.EggListener
import com.mercerenies.turtletroll.egg.EggHatch
import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.CompositeFeature

import org.bukkit.plugin.Plugin
import org.bukkit.event.Listener

import kotlin.collections.Iterable

class AllPluginListeners(val plugin: Plugin) : Iterable<Listener> {
  val pumpkinManager = PumpkinSlownessManager()
  val angelManager = WeepingAngelManager(plugin)
  val phantomManager = PetPhantomManager(plugin)
  val mossManager = ContagiousMossManager()

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
  //val lightListener = TransformTorchOnSightListener(plugin, pumpkinManager)
  val lightListener = BreakLightOnSightListener(plugin, pumpkinManager)
  val lavaListener = LavaLaunchListener()
  val mimicListener = MimicListener(plugin)
  val bedListener = BedDropListener()
  val eggListener = EggListener(EggHatch.defaultEffects(plugin))
  val eggArrowListener = EggArrowListener()
  val eggDropListener = EggDropListener()
  val witherArmorListener = WitherArmorListener()

  // CancelDropAction is a BlockBreakAction and BedDropListener is a
  // Bukkit event listener, but conceptually they do the same thing,
  // so we want to treat them as one feature.
  private val dropCompositeFeature =
    CompositeFeature(
      breakEvents.cancelDropAction.name,
      breakEvents.cancelDropAction.description,
      listOf(breakEvents.cancelDropAction, bedListener)
    )

  fun getListeners(): List<Listener> =
    listOf(
      breakEvents.listener, chickenListener, grassListener, snowListener,
      ghastListener, ravagerListener, skeleListener, electricListener,
      blazeListener, zombifyListener, leavesListener, roseListener,
      endStoneListener, doorListener, angelManager, levitationListener,
      buttonListener, plateListener, slabListener, lightListener,
      phantomManager, lavaListener, pumpkinManager, mimicListener,
      bedListener, eggListener, eggArrowListener, eggDropListener,
      witherArmorListener, mossManager,
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
      mimicListener, dropCompositeFeature, eggListener,
      eggArrowListener, eggDropListener, witherArmorListener,
      mossManager,
    ) + (breakEvents.getFeatures() - breakEvents.cancelDropAction)

  override fun iterator(): Iterator<Listener> =
    getListeners().iterator()

}
