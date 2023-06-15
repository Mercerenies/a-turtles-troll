
package com.mercerenies.turtletroll.pokeball

import org.bukkit.entity.EntityType

fun speciesCaptureRate(entityType: EntityType): Int =
  when (entityType) {
    EntityType.ALLAY -> 220
    EntityType.AXOLOTL -> 240
    EntityType.BAT -> 255
    EntityType.BEE -> 245
    EntityType.BLAZE -> 40
    EntityType.CAMEL -> 210
    EntityType.CAT -> 230
    EntityType.CAVE_SPIDER -> 80
    EntityType.CHICKEN -> 180
    EntityType.COD -> 255
    EntityType.COW -> 210
    EntityType.CREEPER -> 100
    EntityType.DOLPHIN -> 160
    EntityType.DONKEY -> 200
    EntityType.DROWNED -> 120
    EntityType.ELDER_GUARDIAN -> 3
    EntityType.ENDER_DRAGON -> 0
    EntityType.ENDERMAN -> 60
    EntityType.ENDERMITE -> 200
    EntityType.EVOKER -> 50
    EntityType.FOX -> 255
    EntityType.FROG -> 255
    EntityType.GHAST -> 65
    EntityType.GIANT -> 70
    EntityType.GLOW_SQUID -> 240
    EntityType.GOAT -> 200
    EntityType.GUARDIAN -> 90
    EntityType.HOGLIN -> 90
    EntityType.HORSE -> 210
    EntityType.HUSK -> 120
    EntityType.ILLUSIONER -> 115
    EntityType.IRON_GOLEM -> 50
    EntityType.LLAMA -> 190
    EntityType.MAGMA_CUBE -> 120
    EntityType.MULE -> 200
    EntityType.MUSHROOM_COW -> 210
    EntityType.OCELOT -> 220
    EntityType.PANDA -> 240
    EntityType.PARROT -> 190
    EntityType.PHANTOM -> 80
    EntityType.PIG -> 220
    EntityType.PIGLIN -> 110
    EntityType.PIGLIN_BRUTE -> 3
    EntityType.PILLAGER -> 60
    EntityType.POLAR_BEAR -> 200
    EntityType.PUFFERFISH -> 240
    EntityType.RABBIT -> 200
    EntityType.RAVAGER -> 80
    EntityType.SALMON -> 255
    EntityType.SHEEP -> 250
    EntityType.SHULKER -> 200
    EntityType.SILVERFISH -> 200
    EntityType.SKELETON -> 120
    EntityType.SKELETON_HORSE -> 130
    EntityType.SLIME -> 150
    EntityType.SNIFFER -> 220
    EntityType.SNOWMAN -> 255
    EntityType.SPIDER -> 100
    EntityType.SQUID -> 255
    EntityType.STRAY -> 120
    EntityType.STRIDER -> 255
    EntityType.TADPOLE -> 255
    EntityType.TRADER_LLAMA -> 190
    EntityType.TROPICAL_FISH -> 255
    EntityType.TURTLE -> 240
    EntityType.VEX -> 255
    EntityType.VILLAGER -> 255
    EntityType.VINDICATOR -> 90
    EntityType.WANDERING_TRADER -> 255
    EntityType.WARDEN -> 3
    EntityType.WITCH -> 100
    EntityType.WITHER -> 0
    EntityType.WITHER_SKELETON -> 10
    EntityType.WOLF -> 170
    EntityType.ZOGLIN -> 90
    EntityType.ZOMBIE -> 120
    EntityType.ZOMBIE_HORSE -> 130
    EntityType.ZOMBIE_VILLAGER -> 120
    EntityType.ZOMBIFIED_PIGLIN -> 150
    else -> 10 // Unknown entity, so assume very low capture rate
  }
