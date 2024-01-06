
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.util.SetListAllomorph

import org.bukkit.Material

object BlockTypes {

  val GLASS = SetListAllomorph.of(
    Material.BLACK_STAINED_GLASS, Material.BLACK_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS,
    Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS,
    Material.BROWN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS,
    Material.CYAN_STAINED_GLASS_PANE, Material.GLASS, Material.GLASS_PANE,
    Material.GRAY_STAINED_GLASS, Material.GRAY_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS,
    Material.GREEN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS,
    Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS,
    Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS,
    Material.LIME_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS,
    Material.MAGENTA_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS,
    Material.ORANGE_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS,
    Material.PINK_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS,
    Material.PURPLE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS,
    Material.RED_STAINED_GLASS_PANE, Material.TINTED_GLASS, Material.WHITE_STAINED_GLASS,
    Material.WHITE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS,
    Material.YELLOW_STAINED_GLASS_PANE,
  )

  val TALL_GRASS = SetListAllomorph.of(
    Material.GRASS, Material.TALL_GRASS, Material.FERN,
    Material.LARGE_FERN, Material.DEAD_BUSH, Material.CRIMSON_ROOTS, Material.WARPED_ROOTS,
  )

  val ORES = SetListAllomorph.of(
    Material.COAL_ORE, Material.IRON_ORE, Material.LAPIS_ORE,
    Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
    Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS,
    Material.COPPER_ORE, Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_COPPER_ORE,
    Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.DEEPSLATE_GOLD_ORE,
    Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DEEPSLATE_REDSTONE_ORE,
  )

  val SLABS = SetListAllomorph.of(
    Material.ACACIA_SLAB, Material.ANDESITE_SLAB, Material.BIRCH_SLAB, Material.BLACKSTONE_SLAB,
    Material.BRICK_SLAB, Material.COBBLESTONE_SLAB, Material.CRIMSON_SLAB,
    Material.CUT_RED_SANDSTONE_SLAB, Material.CUT_SANDSTONE_SLAB, Material.DARK_OAK_SLAB,
    Material.DARK_PRISMARINE_SLAB, Material.DIORITE_SLAB, Material.END_STONE_BRICK_SLAB,
    Material.GRANITE_SLAB, Material.JUNGLE_SLAB, Material.MOSSY_COBBLESTONE_SLAB,
    Material.MOSSY_STONE_BRICK_SLAB, Material.NETHER_BRICK_SLAB, Material.OAK_SLAB,
    Material.PETRIFIED_OAK_SLAB, Material.POLISHED_ANDESITE_SLAB,
    Material.POLISHED_BLACKSTONE_BRICK_SLAB, Material.POLISHED_BLACKSTONE_SLAB,
    Material.POLISHED_DIORITE_SLAB, Material.POLISHED_GRANITE_SLAB,
    Material.PRISMARINE_BRICK_SLAB, Material.PRISMARINE_SLAB, Material.PURPUR_SLAB,
    Material.QUARTZ_SLAB, Material.RED_NETHER_BRICK_SLAB, Material.RED_SANDSTONE_SLAB,
    Material.SANDSTONE_SLAB, Material.SMOOTH_QUARTZ_SLAB, Material.SMOOTH_RED_SANDSTONE_SLAB,
    Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_STONE_SLAB, Material.SPRUCE_SLAB,
    Material.STONE_BRICK_SLAB, Material.STONE_SLAB, Material.WARPED_SLAB,
    Material.COBBLED_DEEPSLATE_SLAB, Material.CUT_COPPER_SLAB, Material.DEEPSLATE_BRICK_SLAB,
    Material.DEEPSLATE_TILE_SLAB, Material.EXPOSED_CUT_COPPER_SLAB,
    Material.OXIDIZED_CUT_COPPER_SLAB, Material.POLISHED_DEEPSLATE_SLAB,
    Material.WAXED_CUT_COPPER_SLAB, Material.WAXED_EXPOSED_CUT_COPPER_SLAB,
    Material.WAXED_OXIDIZED_CUT_COPPER_SLAB, Material.WEATHERED_CUT_COPPER_SLAB,
    Material.MANGROVE_SLAB, Material.BAMBOO_SLAB, Material.CHERRY_SLAB,
  )

  val STAIRS = SetListAllomorph.of(
    Material.ACACIA_STAIRS, Material.ANDESITE_STAIRS, Material.BIRCH_STAIRS,
    Material.BLACKSTONE_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS,
    Material.CRIMSON_STAIRS, Material.DARK_OAK_STAIRS,
    Material.DARK_PRISMARINE_STAIRS, Material.DIORITE_STAIRS, Material.END_STONE_BRICK_STAIRS,
    Material.GRANITE_STAIRS, Material.JUNGLE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS,
    Material.MOSSY_STONE_BRICK_STAIRS, Material.NETHER_BRICK_STAIRS, Material.OAK_STAIRS,
    Material.POLISHED_ANDESITE_STAIRS,
    Material.POLISHED_BLACKSTONE_BRICK_STAIRS, Material.POLISHED_BLACKSTONE_STAIRS,
    Material.POLISHED_DIORITE_STAIRS, Material.POLISHED_GRANITE_STAIRS,
    Material.PRISMARINE_BRICK_STAIRS, Material.PRISMARINE_STAIRS, Material.PURPUR_STAIRS,
    Material.QUARTZ_STAIRS, Material.RED_NETHER_BRICK_STAIRS, Material.RED_SANDSTONE_STAIRS,
    Material.SANDSTONE_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_RED_SANDSTONE_STAIRS,
    Material.SMOOTH_SANDSTONE_STAIRS, Material.SPRUCE_STAIRS,
    Material.STONE_BRICK_STAIRS, Material.STONE_STAIRS, Material.WARPED_STAIRS,
    Material.COBBLED_DEEPSLATE_STAIRS, Material.CUT_COPPER_STAIRS, Material.DEEPSLATE_BRICK_STAIRS,
    Material.DEEPSLATE_TILE_STAIRS, Material.EXPOSED_CUT_COPPER_STAIRS,
    Material.OXIDIZED_CUT_COPPER_STAIRS, Material.POLISHED_DEEPSLATE_STAIRS,
    Material.WAXED_CUT_COPPER_STAIRS, Material.WAXED_EXPOSED_CUT_COPPER_STAIRS,
    Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS, Material.WEATHERED_CUT_COPPER_STAIRS,
    Material.MANGROVE_STAIRS, Material.BAMBOO_STAIRS, Material.CHERRY_STAIRS,
  )

  val LOGS = SetListAllomorph.of(
    Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG,
    Material.OAK_LOG, Material.SPRUCE_LOG, Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_BIRCH_LOG,
    Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_OAK_LOG,
    Material.STRIPPED_SPRUCE_LOG, Material.MANGROVE_LOG, Material.STRIPPED_MANGROVE_LOG,
    Material.BAMBOO_BLOCK, Material.STRIPPED_BAMBOO_BLOCK, Material.CHERRY_LOG,
    Material.STRIPPED_CHERRY_LOG,
  )

  val PLANKS = SetListAllomorph.of(
    Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.JUNGLE_PLANKS,
    Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.MANGROVE_PLANKS,
    Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_DARK_OAK_WOOD,
    Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_OAK_WOOD, Material.STRIPPED_SPRUCE_WOOD,
    Material.STRIPPED_MANGROVE_WOOD, Material.BAMBOO_PLANKS, Material.CHERRY_WOOD,
    Material.STRIPPED_CHERRY_WOOD,
  )

  val LEAVES = SetListAllomorph.of(
    Material.ACACIA_LEAVES, Material.AZALEA_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES,
    Material.FLOWERING_AZALEA_LEAVES, Material.OAK_LEAVES, Material.JUNGLE_LEAVES, Material.SPRUCE_LEAVES,
    Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES,
  )

  val BUCKETS = SetListAllomorph.of(
    Material.AXOLOTL_BUCKET, Material.BUCKET, Material.COD_BUCKET, Material.LAVA_BUCKET,
    Material.MILK_BUCKET, Material.POWDER_SNOW_BUCKET, Material.PUFFERFISH_BUCKET,
    Material.SALMON_BUCKET, Material.TADPOLE_BUCKET, Material.TROPICAL_FISH_BUCKET,
    Material.WATER_BUCKET,
  )

  val ARMORS = SetListAllomorph.of(
    Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
    Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
    Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
    Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
    Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
    Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
  )

  val FLOWERS = SetListAllomorph.of(
    Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID,
    Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP, Material.OXEYE_DAISY,
    Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE,
    Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY,
    Material.PINK_TULIP, Material.WHITE_TULIP, Material.ORANGE_TULIP,
    Material.SWEET_BERRY_BUSH, Material.TORCHFLOWER,
  )

  val WOOLS = SetListAllomorph.of(
    Material.BLACK_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.CYAN_WOOL,
    Material.GRAY_WOOL, Material.GREEN_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIGHT_GRAY_WOOL,
    Material.LIME_WOOL, Material.MAGENTA_WOOL, Material.ORANGE_WOOL, Material.PINK_WOOL,
    Material.PURPLE_WOOL, Material.RED_WOOL, Material.WHITE_WOOL, Material.YELLOW_WOOL,
  )

  val WOODEN_DOORS = SetListAllomorph.of(
    Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR,
    Material.ACACIA_DOOR, Material.CHERRY_DOOR, Material.DARK_OAK_DOOR, Material.BAMBOO_DOOR,
  )

  val WOODEN_SIGNS = SetListAllomorph.of(
    Material.OAK_SIGN, Material.SPRUCE_SIGN, Material.BIRCH_SIGN, Material.JUNGLE_SIGN,
    Material.ACACIA_SIGN, Material.CHERRY_SIGN, Material.DARK_OAK_SIGN, Material.BAMBOO_SIGN,
  )

  val CONCRETE_POWDERS = SetListAllomorph.of(
    Material.WHITE_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER, Material.MAGENTA_CONCRETE_POWDER,
    Material.LIGHT_BLUE_CONCRETE_POWDER, Material.YELLOW_CONCRETE_POWDER,
    Material.LIME_CONCRETE_POWDER, Material.PINK_CONCRETE_POWDER, Material.GRAY_CONCRETE_POWDER,
    Material.LIGHT_GRAY_CONCRETE_POWDER, Material.CYAN_CONCRETE_POWDER,
    Material.PURPLE_CONCRETE_POWDER, Material.BLUE_CONCRETE_POWDER, Material.BROWN_CONCRETE_POWDER,
    Material.GREEN_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER, Material.BLACK_CONCRETE_POWDER,
  )

  val BEDS = SetListAllomorph.of(
    Material.WHITE_BED, Material.ORANGE_BED, Material.MAGENTA_BED,
    Material.LIGHT_BLUE_BED, Material.YELLOW_BED,
    Material.LIME_BED, Material.PINK_BED, Material.GRAY_BED,
    Material.LIGHT_GRAY_BED, Material.CYAN_BED,
    Material.PURPLE_BED, Material.BLUE_BED, Material.BROWN_BED,
    Material.GREEN_BED, Material.RED_BED, Material.BLACK_BED,
  )

  val DYES = SetListAllomorph.of(
    Material.WHITE_DYE, Material.ORANGE_DYE, Material.MAGENTA_DYE,
    Material.LIGHT_BLUE_DYE, Material.YELLOW_DYE,
    Material.LIME_DYE, Material.PINK_DYE, Material.GRAY_DYE,
    Material.LIGHT_GRAY_DYE, Material.CYAN_DYE,
    Material.PURPLE_DYE, Material.BLUE_DYE, Material.BROWN_DYE,
    Material.GREEN_DYE, Material.RED_DYE, Material.BLACK_DYE,
  )

  // Takes an ore block and turns it into the non-ore block type it's
  // most similar to. Returns the block itself if given a non-ore.
  fun removeOreFrom(material: Material): Material =
    when (material) {
      Material.COAL_ORE -> Material.STONE
      Material.IRON_ORE -> Material.STONE
      Material.LAPIS_ORE -> Material.STONE
      Material.GOLD_ORE -> Material.STONE
      Material.DIAMOND_ORE -> Material.STONE
      Material.EMERALD_ORE -> Material.STONE
      Material.NETHER_QUARTZ_ORE -> Material.NETHERRACK
      Material.NETHER_GOLD_ORE -> Material.NETHERRACK
      Material.ANCIENT_DEBRIS -> Material.NETHERRACK
      Material.COPPER_ORE -> Material.STONE
      Material.DEEPSLATE_COAL_ORE -> Material.DEEPSLATE
      Material.DEEPSLATE_COPPER_ORE -> Material.DEEPSLATE
      Material.DEEPSLATE_DIAMOND_ORE -> Material.DEEPSLATE
      Material.DEEPSLATE_EMERALD_ORE -> Material.DEEPSLATE
      Material.DEEPSLATE_GOLD_ORE -> Material.DEEPSLATE
      Material.DEEPSLATE_IRON_ORE -> Material.DEEPSLATE
      Material.DEEPSLATE_LAPIS_ORE -> Material.DEEPSLATE
      Material.DEEPSLATE_REDSTONE_ORE -> Material.DEEPSLATE
      else -> material // Default value
    }

}
