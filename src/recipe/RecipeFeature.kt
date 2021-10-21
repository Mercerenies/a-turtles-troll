
package com.mercerenies.turtletroll.recipe

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe

import kotlin.reflect.KClass

abstract class RecipeFeature(val plugin: Plugin) : AbstractFeature() {

  companion object {

    fun keyName(cls: KClass<out Any>, index: Int): String =
      "${cls.qualifiedName}.${index}"

  }

  fun interface UnkeyedRecipe<out T : Recipe> {
    operator fun invoke(key: NamespacedKey): T
  }

  private var recipeCount: Int = 0

  private fun namespacedKey(index: Int): NamespacedKey =
    NamespacedKey(plugin, keyName(this::class, index))

  abstract fun getRecipes(): List<UnkeyedRecipe<Recipe>>

  override fun enable() {
    if (!isEnabled()) {
      addRecipes()
    }
    super.enable()
  }

  override fun disable() {
    if (isEnabled()) {
      removeRecipes()
    }
    super.disable()
  }

  fun addRecipes() {
    val recipes = getRecipes()
    recipes.forEachIndexed { index, recipe ->
      val compiledRecipe = recipe(namespacedKey(index))
      Bukkit.addRecipe(compiledRecipe)
    }

    recipeCount = recipes.size
  }

  fun removeRecipes() {
    (0..recipeCount-1).forEach {
      Bukkit.removeRecipe(namespacedKey(it))
    }
    recipeCount = 0
  }

}
