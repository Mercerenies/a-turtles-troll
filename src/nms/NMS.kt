
package com.mercerenies.turtletroll.nms

import org.bukkit.Bukkit
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player
import org.bukkit.entity.Allay

import java.util.logging.Logger

// https://riptutorial.com/bukkit/example/29589/accessing-the-current-minecraft-version :)
//
// I'm trying to leave detailed instructions on how to piece this
// together. When we inevitably have to update this for the next MC
// version (1.19), then it's going to be a pain. Hopefully I can
// alleviate that as much as possible.
object NMS {

  private class ParrotInfo(val left: Boolean, val right: Boolean) : ParrotInformation {

    override fun hasLeftShoulderPerch(): Boolean = left

    override fun hasRightShoulderPerch(): Boolean = right

  }

  private class RawEntityMetadata(
    private var handle: Any,
  ) : EntityMetadata {

    // I seriously doubt these field names will change, given that
    // it's a Java record. But just in case: We want the integer, then
    // the data watcher serializer, then the T. All of these are
    // constructor parameters to the record type identified by
    // getEntityMetadataClass below.

    override val id: Int
      get() =
        safely(0) {
          getEntityMetadataClass().getMethod("a").invoke(handle) as Int
        }

    override val serializer: Any?
      get() =
        safely(null) {
          getEntityMetadataClass().getMethod("b").invoke(handle)
        }

    override var value: Any?
      get() =
        safely(null) {
          getEntityMetadataClass().getMethod("c").invoke(handle)
        }
      set(newValue) {
        safely {
          val serializerCls = Class.forName("net.minecraft.network.syncher.DataWatcherSerializer")
          val ctor = getEntityMetadataClass().getConstructor(java.lang.Integer.TYPE, serializerCls, Object::class.java)
          handle = ctor.newInstance(id, serializer, newValue)
        }
      }

    override fun getHandle(): Any =
      handle

  }

  fun logger(): Logger =
    Bukkit.getLogger()

  // Run, logging any errors
  fun<R> safely(default: R, fn: () -> R): R {
    try {
      return fn()
    } catch (e: Exception) {
      logger().severe("Error during NMS: $e")
      return default
    }
  }

  fun safely(fn: () -> Unit) =
    safely(Unit, fn)

  // This should never need to change.
  fun version(): String =
    Bukkit.getServer()::class.java.getPackage().getName().substring(23)

  // This should never need to change.
  fun getClass(suffix: String): Class<*> =
    Class.forName("org.bukkit.craftbukkit.${version()}.${suffix}")

  // Go to org.bukkit.craftbukkit.*.entity.CraftFallingBlock and look
  // at the implementation of canHurtEntities(). It returns a Boolean.
  // Remember the name of that Boolean (in 1.17, it's 'ap'; in 1.18
  // and 1.19.2, it's 'aq'; in 1.20.1 it's 'i'). Now go to
  // net.minecraft.world.entity.item.EntityFallingBlock. There should
  // be a method with signature
  //
  // public void ???(float, int)
  //
  // Which sets that variable (again, `ap` in 1.17; 'aq' in
  // 1.18/1.19.2; `i` in 1.20.1) to true and sets two other variables
  // to the arguments. We want to call that function (`b` in 1.17,
  // 1.18, 1.19.2, and 1.20.1) with 1.0f and 40.
  fun makeFallingBlockHurt(block: FallingBlock) {
    safely {
      val cls = getClass("entity.CraftFallingBlock")
      val mcBlock = cls.getMethod("getHandle").invoke(block)
      Class.forName("net.minecraft.world.entity.item.EntityFallingBlock")
        .getMethod("b", java.lang.Float.TYPE, java.lang.Integer.TYPE)
        .invoke(mcBlock, 1.0f, 40)
    }
  }

  // Go to net.minecraft.world.entity.Entity and find the method with
  // signature
  //
  // public NBTTagCompound ???(NBTTagCompound nbt)
  //
  // There should only be one, and its body should be a large
  // try-catch block which loads a bunch of fields into the NBT
  // argument and then returns it. (In 1.18, 1.19.2, and 1.20.1, this
  // method is called `f`). That method needs to be called below when
  // we reassign to mcNbt.
  //
  // Now go to net.minecraft.nbt.NBTTagCompound and find the method
  // with signature
  //
  // public NBTBase ???(String key)
  //
  // Again, it should be unique. The body is a one-liner and calls
  // .get on an instance variable of type Map<String, NBTBase>. (This
  // method is called `c` in 1.18/1.19.2/1.20.1). That method needs to
  // be accessed as mcNbtGetter below.
  fun getPlayerParrotInfo(player: Player): ParrotInformation {
    return safely(ParrotInfo(false, false)) {
      val cls = getClass("entity.CraftPlayer")
      val mcEntity = cls.getMethod("getHandle").invoke(player)
      val mcNbtClass = Class.forName("net.minecraft.nbt.NBTTagCompound")
      var mcNbt = mcNbtClass
        .getConstructor()
        .newInstance()
      mcNbt = Class.forName("net.minecraft.world.entity.Entity")
        .getMethod("f", mcNbtClass)
        .invoke(mcEntity, mcNbt)
      val mcNbtGetter = mcNbtClass
        .getMethod("c", String::class.java)
      val leftShoulder = mcNbtGetter.invoke(mcNbt, "ShoulderEntityLeft")
      val rightShoulder = mcNbtGetter.invoke(mcNbt, "ShoulderEntityRight")
      ParrotInfo(leftShoulder != null, rightShoulder != null)
    }
  }

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
  fun setAllayFriend(allay: Allay, player: Player) {
    safely {
      val playerCls = getClass("entity.CraftPlayer")
      val allayCls = getClass("entity.CraftAllay")
      val memoryModuleTypeCls = Class.forName("net.minecraft.world.entity.ai.memory.MemoryModuleType")
      val mcAllayCls = Class.forName("net.minecraft.world.entity.animal.allay.Allay")
      val behaviorControllerCls = Class.forName("net.minecraft.world.entity.ai.BehaviorController")
      val entityCls = Class.forName("net.minecraft.world.entity.player.EntityHuman")

      val mcPlayerEntity = playerCls.getMethod("getHandle").invoke(player)
      val mcAllayEntity = allayCls.getMethod("getHandle").invoke(allay)
      val memoryModuleType = memoryModuleTypeCls.getField("aL").get(null)
      val controller = mcAllayCls.getMethod("dK").invoke(mcAllayEntity)
      val playerUuid = entityCls.getMethod("ct").invoke(mcPlayerEntity)
      behaviorControllerCls.getMethod("a", memoryModuleTypeCls, Object::class.java).invoke(controller, memoryModuleType, playerUuid)
    }
  }

  // Go to
  // net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata.
  // Look for a private final field of List type. This class name
  // should match the type of elements in that list. It's
  // DataWatcher.b in 1.20.1. Remember to translate using the Java
  // naming convention, so inner class scoping is replaced with '$'.
  fun getEntityMetadataClass(): Class<*> =
    Class.forName("net.minecraft.network.syncher.DataWatcher\$b")

  fun getEntityMetadata(handle: Any): EntityMetadata? {
    if (getEntityMetadataClass().isInstance(handle)) {
      return RawEntityMetadata(handle)
    } else {
      return null
    }
  }

  // Go to net.minecraft.network.syncher and find the
  // DataWatcherSerializer<Integer> field. It's b in 1.20.1.
  fun constructEntityMetadataInt(id: Int, value: Int): EntityMetadata {
    val serializerCls = Class.forName("net.minecraft.network.syncher.DataWatcherSerializer")
    val ctor = getEntityMetadataClass().getConstructor(java.lang.Integer.TYPE, serializerCls, Object::class.java)
    val serializer = Class.forName("net.minecraft.network.syncher.DataWatcherRegistry").getField("b").get(null)
    val handle = ctor.newInstance(id, serializer, value)
    return RawEntityMetadata(handle)
  }

}
