
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Enchantments.addEnchantment
import com.mercerenies.turtletroll.Enchantments.EnchantmentData
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.PiglinBarterEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.Chicken
import org.bukkit.entity.PufferFish
import org.bukkit.entity.Zombie
import org.bukkit.entity.EntityType
import org.bukkit.entity.Entity
import org.bukkit.entity.Piglin
import org.bukkit.Location
import org.bukkit.Chunk
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack

import kotlin.random.Random

class PiglinBarterListener(
  val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val ARMOR_TYPES = BlockTypes.ARMORS.toList()

    private val FLOWERS_LIST = BlockTypes.FLOWERS.toList()

    private val ENCHANTMENT_CHOICES = listOf(
      EnchantmentData(Enchantment.MENDING, 1),
      EnchantmentData(Enchantment.THORNS, 1),
      EnchantmentData(Enchantment.DURABILITY, 2),
      EnchantmentData(Enchantment.DURABILITY, 3),
      EnchantmentData(Enchantment.PROTECTION_ENVIRONMENTAL, 1),
      EnchantmentData(Enchantment.PROTECTION_ENVIRONMENTAL, 2),
      EnchantmentData(Enchantment.PROTECTION_ENVIRONMENTAL, 3),
      EnchantmentData(Enchantment.PROTECTION_FIRE, 2),
      EnchantmentData(Enchantment.PROTECTION_FIRE, 3),
      EnchantmentData(Enchantment.PROTECTION_FIRE, 4),
      EnchantmentData(Enchantment.PROTECTION_EXPLOSIONS, 2),
      EnchantmentData(Enchantment.PROTECTION_EXPLOSIONS, 3),
      EnchantmentData(Enchantment.PROTECTION_EXPLOSIONS, 4),
      EnchantmentData(Enchantment.PROTECTION_PROJECTILE, 2),
      EnchantmentData(Enchantment.PROTECTION_PROJECTILE, 3),
      EnchantmentData(Enchantment.PROTECTION_PROJECTILE, 4),
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(PiglinBarterListener(state.plugin))

    private fun removeAllBarters(piglin: Piglin) {
      val barters = piglin.barterList.toList()
      for (barter in barters) {
        piglin.removeBarterMaterial(barter)
      }
      val interested = piglin.interestList.toList()
      for (material in interested) {
        piglin.removeMaterialOfInterest(material)
      }
    }

    private fun setupPiglin(piglin: Piglin) {
      removeAllBarters(piglin)
      piglin.addBarterMaterial(Material.DIAMOND)
      piglin.addMaterialOfInterest(Material.DIAMOND)
    }

    private fun generateBarterResult(): ItemStack {
      val armorMaterial = ARMOR_TYPES.sample()!!
      val itemStack = ItemStack(armorMaterial, 1)
      for (_i in 1..3) {
        itemStack.addEnchantment(ENCHANTMENT_CHOICES.sample()!!)
      }
      return itemStack
    }

  }

  private class SetupPiglins(_chunk: Chunk) : ReplaceMobsRunnable(_chunk) {

    override fun replaceWith(entity: Entity): EntityType? =
      null

    override fun onUnreplacedMob(entity: Entity) {
      if (entity is Piglin) {
        setupPiglin(entity)
      }
    }

  }

  private class SetupPiglin(private val entity: Piglin) : BukkitRunnable() {

    private val DELAY = 3L

    fun schedule(plugin: Plugin) {
      runTaskLater(plugin, DELAY)
    }

    override fun run() {
      setupPiglin(entity)
    }

  }

  override val name = "piglins"

  override val description = "Piglins only accept diamonds, but in return they give valuable armor"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    val entity = event.entity
    if (entity is Piglin) {
      SetupPiglin(entity).schedule(plugin)
    }
  }

  @EventHandler
  fun onChunkPopulate(event: ChunkPopulateEvent) {
    if (!isEnabled()) {
      return
    }
    val chunk = event.getChunk()
    SetupPiglins(chunk).schedule(plugin)
  }

  @EventHandler
  fun onPiglinBarter(event: PiglinBarterEvent) {
    if (!isEnabled()) {
      return
    }
    val outcome = event.getOutcome()
    outcome.clear()
    // I can't remove gold ingots as an item of interest because
    // that's baked into Minecraft. So instead, I'll just have them
    // give you a nice flower if you give them gold :)
    if (event.input.type == Material.GOLD_INGOT) {
      outcome.add(ItemStack(FLOWERS_LIST.sample()!!, 3))
    } else {
      outcome.add(generateBarterResult())
    }
  }

}
