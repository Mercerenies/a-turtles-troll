
package com.mercerenies.turtletroll.angel

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.color.UniformColorGenerator

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.LeatherArmorMeta

import kotlin.random.Random

object ArmorStandSpawner {

  private val colorGenerator = UniformColorGenerator

  enum class RawMaterial {
    LEATHER, GOLD, IRON,
  }

  enum class ArmorSlot {
    HELMET, CHESTPLATE, LEGGINGS, BOOTS,
  }

  fun spawn(location: Location): ArmorStand {
    val stand = location.world!!.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand
    stand.setArms(true)
    val equipment = stand.getEquipment()
    if (equipment != null) {
      equipment.helmet = generateArmorPiece(ArmorSlot.HELMET)
      equipment.chestplate = generateArmorPiece(ArmorSlot.CHESTPLATE)
      equipment.leggings = generateArmorPiece(ArmorSlot.LEGGINGS)
      equipment.boots = generateArmorPiece(ArmorSlot.BOOTS)
    }
    return stand
  }

  fun makeMaterial(raw: RawMaterial, armor: ArmorSlot): Material =
    when (raw) {
      RawMaterial.LEATHER ->
        when (armor) {
          ArmorSlot.HELMET -> Material.LEATHER_HELMET
          ArmorSlot.CHESTPLATE -> Material.LEATHER_CHESTPLATE
          ArmorSlot.LEGGINGS -> Material.LEATHER_LEGGINGS
          ArmorSlot.BOOTS -> Material.LEATHER_BOOTS
        }
      RawMaterial.GOLD ->
        when (armor) {
          ArmorSlot.HELMET -> Material.GOLDEN_HELMET
          ArmorSlot.CHESTPLATE -> Material.GOLDEN_CHESTPLATE
          ArmorSlot.LEGGINGS -> Material.GOLDEN_LEGGINGS
          ArmorSlot.BOOTS -> Material.GOLDEN_BOOTS
        }
      RawMaterial.IRON ->
        when (armor) {
          ArmorSlot.HELMET -> Material.IRON_HELMET
          ArmorSlot.CHESTPLATE -> Material.IRON_CHESTPLATE
          ArmorSlot.LEGGINGS -> Material.IRON_LEGGINGS
          ArmorSlot.BOOTS -> Material.IRON_BOOTS
        }
    }

  fun chooseRawMaterial(): RawMaterial? =
    sample(
      listOf(
        Weight(null, 20.0),
        Weight(RawMaterial.LEATHER, 100.0),
        Weight(RawMaterial.GOLD, 10.0),
        Weight(RawMaterial.IRON, 1.0),
      )
    )

  fun generateArmorPiece(slot: ArmorSlot): ItemStack? {
    val raw = chooseRawMaterial()
    if (raw == null) {
      return null
    }
    val material = makeMaterial(raw, slot)
    val stack = ItemStack(material)
    val maxDurability = material.getMaxDurability()
    val durabilityFrac =
      sample(
        listOf(
          Weight(0.05, 10.0),
          Weight(0.10, 20.0),
          Weight(0.15, 10.0),
          Weight(0.30, 1.0),
          Weight(0.50, 0.5),
          Weight(1.00, 0.1)
        )
      )
    val meta = stack.getItemMeta()!!
    (meta as Damageable).setDamage(((1.0 - durabilityFrac) * maxDurability).toInt())
    if (meta is LeatherArmorMeta) {
      meta.setColor(colorGenerator.generate(Random))
    }
    stack.setItemMeta(meta)
    return stack
  }

}
