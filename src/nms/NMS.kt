
package com.mercerenies.turtletroll.nms

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Allay

import java.util.logging.Logger
import java.util.logging.Level

// https://riptutorial.com/bukkit/example/29589/accessing-the-current-minecraft-version :)
//
// I'm trying to leave detailed instructions on how to piece this
// together. When we inevitably have to update this for the next MC
// version (1.19), then it's going to be a pain. Hopefully I can
// alleviate that as much as possible.
object NMS {
  fun logger(): Logger =
    Bukkit.getLogger()

  // Run, logging any errors
  fun<R> safely(default: R, fn: () -> R): R {
    try {
      return fn()
    } catch (e: Exception) {
      logger().log(Level.SEVERE, "Error during NMS", e)
      return default
    }
  }

  fun safely(fn: () -> Unit) =
    safely(Unit, fn)

  // As of 1.21.3, it looks like the version number doesn't exist
  // anymore and we just use the base package name, which is much
  // nicer.
  @Deprecated("Newer Bukkit versions do not place this information in the package name anymore.")
  fun version(): String =
    ""

  // This should never need to change.
  fun getClass(suffix: String): Class<*> =
    Class.forName("org.bukkit.craftbukkit.${suffix}")

  // Go to net.minecraft.world.entity.animal.allay.Allay. Find the
  // method with signature
  //
  // protected EnumInteractionResult ???(EntityHuman, EnumHand)
  //
  // It is called b on 1.19.2/1.20.1. In this method there should be a
  // few if statements. We'll use some lines in the second if
  // statement as a guide. Specifically, the line that uses a
  // MemoryModuleType.
  //
  // Now, go to net.minecraft.world.entity.ai.memory.MemoryModuleType
  // and find the static final field with value "liked_player". This
  // is aK on 1.19.2, aL on 1.20.1.
  //
  // Go to net.minecraft.world.entity.animal.allay.Allay. Find the
  // method that takes no arguments and returns
  // BehaviorController<Allay>. It's called dy on 1.19.2, dK on
  // 1.20.1.
  //
  // Now go to net.minecraft.world.entity.Entity and find the
  // zero-argument method that returns a java.util.UUID. It's called
  // co on 1.19.2, ct on 1.20.1.
  //
  // Finally, go to net.minecraft.world.entity.ai.BehaviorController
  // and find the non-static method that takes a MemoryModuleType<U>
  // and a (nullable) U. It's called a on 1.19.2/1.20.1 (and is one of
  // several overloads).
  //
  // ----
  //
  // As of 1.21.3, the Allay class has been refactored. The
  // EnumInteractionResult method is gone, and BehaviorController is
  // now Brain. But more importantly, the methods appear to NOT be
  // obfuscated, which means (maybe?) we don't have to keep updating
  // this.
  fun setAllayFriend(allay: Allay, player: Player) {
    safely {
      val playerCls = getClass("entity.CraftPlayer")
      val allayCls = getClass("entity.CraftAllay")
      val memoryModuleTypeCls = Class.forName("net.minecraft.world.entity.ai.memory.MemoryModuleType")
      val mcAllayCls = Class.forName("net.minecraft.world.entity.animal.allay.Allay")
      val behaviorControllerCls = Class.forName("net.minecraft.world.entity.ai.Brain")
      val entityCls = Class.forName("net.minecraft.world.entity.player.EntityHuman")

      val mcPlayerEntity = playerCls.getMethod("getHandle").invoke(player)
      val mcAllayEntity = allayCls.getMethod("getHandle").invoke(allay)
      val memoryModuleType = memoryModuleTypeCls.getField("LIKED_PLAYER").get(null)
      val controller = mcAllayCls.getMethod("getBrain").invoke(mcAllayEntity)
      val playerUuid = entityCls.getMethod("getUUID").invoke(mcPlayerEntity)
      behaviorControllerCls.getMethod("setMemory", memoryModuleTypeCls, Object::class.java).invoke(controller, memoryModuleType, playerUuid)
    }
  }
}
