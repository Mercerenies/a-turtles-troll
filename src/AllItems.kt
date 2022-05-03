
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Item

object AllItems {

  // Note: I make no guarantees on the *order* of this list.
  val allMaterials: List<Material> = Material.values().toList()

  @Suppress("DEPRECATION")
  val allItems: List<Material> =
    allMaterials.filter { it.isItem() && !it.isLegacy() }

  private fun sampleUnchecked(): Material =
    allMaterials.sample()!!

  fun sample(): Material =
    allItems.sample()!!

  fun getRarity(item: Material): Rarity =
    when (item) {
      Material.CREEPER_BANNER_PATTERN  -> Rarity.UNCOMMON
      Material.SKULL_BANNER_PATTERN    -> Rarity.UNCOMMON
      Material.EXPERIENCE_BOTTLE       -> Rarity.UNCOMMON
      Material.DRAGON_BREATH           -> Rarity.UNCOMMON
      Material.ELYTRA                  -> Rarity.UNCOMMON
      Material.CREEPER_HEAD            -> Rarity.UNCOMMON
      Material.CREEPER_WALL_HEAD       -> Rarity.UNCOMMON
      Material.DRAGON_HEAD             -> Rarity.UNCOMMON
      Material.DRAGON_WALL_HEAD        -> Rarity.UNCOMMON
      Material.PLAYER_HEAD             -> Rarity.UNCOMMON
      Material.PLAYER_WALL_HEAD        -> Rarity.UNCOMMON
      Material.ZOMBIE_HEAD             -> Rarity.UNCOMMON
      Material.ZOMBIE_WALL_HEAD        -> Rarity.UNCOMMON
      Material.HEART_OF_THE_SEA        -> Rarity.UNCOMMON
      Material.NETHER_STAR             -> Rarity.UNCOMMON
      Material.TOTEM_OF_UNDYING        -> Rarity.UNCOMMON
      Material.ENCHANTED_BOOK          -> Rarity.UNCOMMON
      Material.BEACON                  -> Rarity.RARE
      Material.CONDUIT                 -> Rarity.RARE
      Material.END_CRYSTAL             -> Rarity.RARE
      Material.GOLDEN_APPLE            -> Rarity.RARE
      Material.MUSIC_DISC_11           -> Rarity.RARE
      Material.MUSIC_DISC_13           -> Rarity.RARE
      Material.MUSIC_DISC_BLOCKS       -> Rarity.RARE
      Material.MUSIC_DISC_CAT          -> Rarity.RARE
      Material.MUSIC_DISC_CHIRP        -> Rarity.RARE
      Material.MUSIC_DISC_FAR          -> Rarity.RARE
      Material.MUSIC_DISC_MALL         -> Rarity.RARE
      Material.MUSIC_DISC_MELLOHI      -> Rarity.RARE
      Material.MUSIC_DISC_PIGSTEP      -> Rarity.RARE
      Material.MUSIC_DISC_STAL         -> Rarity.RARE
      Material.MUSIC_DISC_STRAD        -> Rarity.RARE
      Material.MUSIC_DISC_WAIT         -> Rarity.RARE
      Material.MUSIC_DISC_WARD         -> Rarity.RARE
      Material.MOJANG_BANNER_PATTERN   -> Rarity.EPIC
      Material.COMMAND_BLOCK           -> Rarity.EPIC
      Material.DRAGON_EGG              -> Rarity.EPIC
      Material.STRUCTURE_BLOCK         -> Rarity.EPIC
      Material.STRUCTURE_VOID          -> Rarity.EPIC
      Material.JIGSAW                  -> Rarity.EPIC
      Material.LIGHT                   -> Rarity.EPIC
      Material.SPAWNER                 -> Rarity.EPIC
      Material.BARRIER                 -> Rarity.EPIC
      Material.COMMAND_BLOCK_MINECART  -> Rarity.EPIC
      Material.CHAIN_COMMAND_BLOCK     -> Rarity.EPIC
      Material.REPEATING_COMMAND_BLOCK -> Rarity.EPIC
      else                             -> Rarity.COMMON
    }

  fun give(entity: HumanEntity, vararg items: ItemStack) {
    val remaining = entity.inventory.addItem(*items)
    for (remainingItem in remaining.values) {
      entity.world.spawn(entity.location, Item::class.java) { item ->
        item.itemStack = remainingItem
      }
    }
  }

}
