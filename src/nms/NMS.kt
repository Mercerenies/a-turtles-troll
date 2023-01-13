
package com.mercerenies.turtletroll.nms

import com.mercerenies.turtletroll.ParrotInformation

import org.bukkit.Bukkit
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player

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

  // This should never need to change.
  fun version(): String =
    Bukkit.getServer()::class.java.getPackage().getName().substring(23)

  // This should never need to change.
  fun getClass(suffix: String): Class<*> =
    Class.forName("org.bukkit.craftbukkit.${version()}.${suffix}")

  // Go to org.bukkit.craftbukkit.*.entity.CraftFallingBlock and look
  // at the implementation of canHurtEntities(). It returns a Boolean.
  // Remember the name of that Boolean (in 1.17, it's 'ap'; in 1.18
  // and 1.19.2, it's 'aq'). Now go to
  // net.minecraft.world.entity.item.EntityFallingBlock. There should
  // be a method with signature
  //
  // public void ???(float, int)
  //
  // Which sets that variable (again, `ap` in 1.17; 'aq' in
  // 1.18/1.19.2) to true and sets two other variables to the
  // arguments. We want to call that function (`b` in 1.17, 1.18,
  // 1.19.2) with 1.0f and 40.
  fun makeFallingBlockHurt(block: FallingBlock) {
    val cls = getClass("entity.CraftFallingBlock")
    val mcBlock = cls.getMethod("getHandle").invoke(block)
    Class.forName("net.minecraft.world.entity.item.EntityFallingBlock")
      .getMethod("b", java.lang.Float.TYPE, java.lang.Integer.TYPE)
      .invoke(mcBlock, 1.0f, 40)
  }

  // Go to net.minecraft.world.entity.Entity and find the method with
  // signature
  //
  // public NBTTagCompound ???(NBTTagCompound nbt)
  //
  // There should only be one, and its body should be a large
  // try-catch block which loads a bunch of fields into the NBT
  // argument and then returns it. (In 1.18 and 1.19.2, this method is
  // called `f`). That method needs to be called below when we
  // reassign to mcNbt.
  //
  // Now go to net.minecraft.nbt.NBTTagCompound and find the method
  // with signature
  //
  // public NBTBase ???(String key)
  //
  // Again, it should be unique. The body is a one-liner and calls
  // .get on an instance variable of type Map<String, NBTBase>. (This
  // method is called `c` in 1.18/1.19.2). That method needs to be
  // accessed as mcNbtGetter below.
  fun getPlayerParrotInfo(player: Player): ParrotInformation {
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
    return ParrotInfo(leftShoulder != null, rightShoulder != null)
  }

}
