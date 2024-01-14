
package com.mercerenies.turtletroll.cake

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.bridge.RaccoonBridge
import com.mercerenies.turtletroll.SpawnReason
import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.mimic.MimicListener
import com.mercerenies.turtletroll.mimic.MimicIdentifier
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.GameRule
import org.bukkit.entity.Player
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.block.`data`.Lightable
import org.bukkit.block.`data`.`type`.Cake

import kotlin.collections.HashMap
import kotlin.random.Random
import kotlin.math.pow

class CakeListener(
  val plugin: Plugin,
  val effects: List<Weight<CakeEffect>>,
  val mobReplaceChance: Double,
) : AbstractFeature(), Listener {

  companion object {
    val FIVE_MINUTES = 300 * Constants.TICKS_PER_SECOND

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
      CAKE_TYPES.random()

    fun isCake(material: Material): Boolean =
      CAKE_TYPES.contains(material)

    private fun isRaccoonCakeSlice(item: ItemStack): Boolean {

      if (item.type != Material.SWEET_BERRIES) {
        return false
      }

      val cakeKey = RaccoonBridge.namespacedKey("is_CakeSlice")
      if (cakeKey == null) {
        return false // Raccoon Mischief is not loaded.
      }

      val dataContainer = item.itemMeta.persistentDataContainer
      return (dataContainer.getOrDefault(cakeKey, PersistentDataType.DOUBLE, 0.0) > 0.0)
    }

    private fun isLastBite(block: Block): Boolean {
      val blockData = block.blockData
      if (blockData is Cake) {
        return blockData.bites == blockData.maximumBites
      } else {
        return false
      }
    }

    private fun explodeBlock(block: Block) {
      val world = block.location.world!!
      block.setType(Material.AIR)
      if (world.getGameRuleValue(GameRule.MOB_GRIEFING) ?: true) {
        world.createExplosion(block.location, 5.0F, true, true)
      } else {
        world.createExplosion(block.location, 5.0F, true, false)
      }
    }

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
      val base = (1 - p) / 2 + p * (2.0).pow(- r / 7.0) // TODO Make half life configurable w/ config.yml
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
      val nearbyMatching = BlockSelector.countNearbyMatching(event.location.block, MimicListener.SAFETY_RADIUS) {
        BlockSelector.isMimicOrCake(MimicIdentifier(plugin), it)
      }
      if (nearbyMatching < 1) {
        if (Random.nextDouble() < mobReplaceChance) {
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
        if (isLastBite(block)) {
          explodeBlock(block)
          event.setCancelled(true)
        } else {
          val cancelsDefault = applyEffect(block.location, event.player)
          if (cancelsDefault) {
            event.setCancelled(true)
          }
        }
      }
    }
  }

  @EventHandler
  fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
    if (!isEnabled()) {
      return
    }
    val item = event.getItem()
    if (isRaccoonCakeSlice(item)) {
      val cancelsDefault = applyEffect(event.player.location, event.player)
      if (cancelsDefault) {
        event.setCancelled(true)
      }
    }
  }

  private fun applyEffect(location: Location, player: Player): Boolean {
    val effect = chooseEffect(player)
    effect.onEat(location, player)
    cakesEatenCounter.eat(player)
    return effect.cancelsDefault()
  }

  private fun chooseEffect(player: Player): CakeEffect {
    val recentlyEaten = cakesEatenCounter.get(player)
    val weights = updateWeights(effects, recentlyEaten)
    return sample(weights)
  }

}
