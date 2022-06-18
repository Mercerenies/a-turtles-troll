
package com.mercerenies.turtletroll.cake

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.mimic.MimicListener
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.entity.Player
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.block.`data`.Lightable

import kotlin.collections.HashMap
import kotlin.random.Random
import kotlin.math.pow

class CakeListener(
  val plugin: Plugin,
  val effects: List<Weight<CakeEffect>>,
) : AbstractFeature(), Listener {

  companion object {
    val FIVE_MINUTES = 300 * Constants.TICKS_PER_SECOND

    val MOB_REPLACE_CHANCE = 0.05
    val MOBS_TO_REPLACE = setOf(
      EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.ZOMBIFIED_PIGLIN,
      EntityType.STRAY, EntityType.HUSK,
    )

    val CAKE_TYPES = listOf(
      Material.CAKE, Material.CANDLE_CAKE, Material.CYAN_CANDLE_CAKE, Material.GRAY_CANDLE_CAKE,
      Material.GREEN_CANDLE_CAKE, Material.LIGHT_BLUE_CANDLE_CAKE, Material.LIGHT_GRAY_CANDLE_CAKE,
      Material.LIME_CANDLE_CAKE, Material.MAGENTA_CANDLE_CAKE, Material.ORANGE_CANDLE_CAKE,
      Material.PINK_CANDLE_CAKE, Material.PURPLE_CANDLE_CAKE, Material.RED_CANDLE_CAKE,
      Material.WHITE_CANDLE_CAKE, Material.YELLOW_CANDLE_CAKE,
    )

    fun makeCake(): Material =
      CAKE_TYPES.sample()!!

    fun isCake(material: Material): Boolean =
      CAKE_TYPES.contains(material)

    // Desired properties of this function
    //
    // * f(p, 0) has positive slope
    // * f(1, 0) = 1    (The first cake you eat should be highly likely to be positive)
    // * f(-1, 0) = 0   (The first cake you eat shouldn't be negative very often)
    // * f(1, inf) = 0  (The more cakes you eat, the worse effects get)
    // * f(-1, inf) = 1 (The more cakes you eat, the worse effects get)
    // * f(-1, r) and f(1, r) should be exponential decay with half life 7
    fun compatibility(positivity: Double, recentCakesEaten: Int): Double {
      val p = positivity
      val r = recentCakesEaten.toDouble()
      val base = (1 - p) / 2 + p * (2.0).pow(- r / 7.0)
      return base.pow(3.0)
    }

    private fun updateWeight(w: Weight<CakeEffect>, cakesEaten: Int): Weight<CakeEffect> =
      Weight(w.value, compatibility(w.value.positivity, cakesEaten))

    fun updateWeights(ws: List<Weight<CakeEffect>>, cakesEaten: Int): List<Weight<CakeEffect>> =
      ws.map { updateWeight(it, cakesEaten) }

  }

  private inner class CakesEatenCounter {
    private val recentCakes = HashMap<Player, Int>()

    private inner class CakesEatenRunnable(val player: Player) : BukkitRunnable() {
      override fun run() {
        recentCakes[player] = (recentCakes[player] ?: 0) - 1
      }
    }

    fun eat(player: Player) {
      recentCakes[player] = (recentCakes[player] ?: 0) + 1
      CakesEatenRunnable(player).runTaskLater(plugin, FIVE_MINUTES.toLong())
    }

    fun get(player: Player): Int =
      recentCakes[player] ?: 0

  }
  private val cakesEatenCounter = CakesEatenCounter()

  override val name = "cakes"

  override val description = "Cakes randomly spawn in the world; eating cake has a random effect"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    if (MOBS_TO_REPLACE.contains(event.entity.type)) {
      if (BlockSelector.countNearbyMatching(event.location.block, MimicListener.SAFETY_RADIUS, BlockSelector::isMimicOrCake) < 1) {
        if (Random.nextDouble() < MOB_REPLACE_CHANCE) {
          val below = event.location.clone().add(0.0, -1.0, 0.0)
          if (below.block.type != Material.AIR) {
            event.setCancelled(true)
            event.location.block.type = makeCake()
            val blockData = event.location.block.getBlockData()
            if (blockData is Lightable) {
              blockData.setLit(true)
            }
            event.location.block.setBlockData(blockData)
          }
        }
      }
    }
  }

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    if (!isEnabled()) {
      return
    }

    val block = event.getClickedBlock()
    if (block != null) {
      val material = block.type
      if ((isCake(material)) && (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
        val effect = chooseEffect(event.player)
        effect.onEat(block.location, event.player)
        cakesEatenCounter.eat(event.player)
        if (effect.cancelsDefault()) {
          event.setCancelled(true)
        }
      }
    }
  }

  private fun chooseEffect(player: Player): CakeEffect {
    val recentlyEaten = cakesEatenCounter.get(player)
    val weights = updateWeights(effects, recentlyEaten)
    return sample(weights)
  }

}
