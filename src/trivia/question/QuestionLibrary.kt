
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.BlockTypes

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.enchantments.Enchantment

object QuestionLibrary {

  // ItemStack.addEnchantment, but fluent
  private fun ItemStack.withEnchantment(enchantment: Enchantment, level: Int): ItemStack {
    this.addEnchantment(enchantment, level)
    return this
  }

  val QUESTIONS: List<() -> TriviaQuestion> = listOf(
    {
      MultipleChoiceQuestion(
        questionBody = "At what difficulty does the world need to be set to for zombies to break down doors?",
        answers = listOf("Peaceful", "Easy", "Medium", "Hard"),
        correctAnswerIndex = 3,
        rewards = BlockTypes.WOODEN_DOORS.toList().map { ItemReward(it) } + listOf(ItemReward(Material.ZOMBIE_SPAWN_EGG)),
        shuffleAnswers = false,
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What is the most common type of armor to spawn naturally on zombies?",
        answers = listOf("Leather", "Gold", "Chainmail", "Iron", "Diamond"),
        correctAnswerIndex = 1,
        rewards = listOf(ItemReward(Material.GOLDEN_BOOTS), ItemReward(Material.GOLDEN_HELMET), ItemReward(Material.GOLDEN_LEGGINGS), ItemReward(Material.GOLDEN_CHESTPLATE)),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What was the name of the Minecraft 1.15 Update?",
        answers = listOf("World of Color", "Buzzy Bees", "Village & Pillage", "The Wild Update"),
        correctAnswerIndex = 1,
        rewards = listOf(ItemReward(ItemStack(Material.HONEYCOMB, 4))),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What was the name of the Minecraft 1.12 Update?",
        answers = listOf("World of Color", "Caves & Cliffs", "Village & Pillage", "The Wild Update"),
        correctAnswerIndex = 0,
        rewards = BlockTypes.CONCRETE_POWDERS.toList().map { ItemReward(ItemStack(it, 16)) },
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What was the name of the Minecraft 1.13 Update?",
        answers = listOf("Update Aquatic", "Trails & Tales", "Village & Pillage", "Wardens & Below"),
        correctAnswerIndex = 0,
        rewards = listOf(ItemReward(Material.KELP)),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "The barrel is the job site block for which villager type?",
        answers = listOf("Fisherman", "Armorer", "Butcher", "Fletcher", "Toolsmith"),
        correctAnswerIndex = 0,
        rewards = listOf(ItemReward(Material.BARREL)),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following is NOT disabled when mobGriefing is false?",
        answers = listOf("Creepers damaging blocks", "Zombies breaking doors", "Sheep eating grass", "Skeleton horses summoning lightning"),
        correctAnswerIndex = 3,
        rewards = listOf(ItemReward(ItemStack(Material.BONE, 8))),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which Minecraft update was called the Wild Update?",
        answers = listOf("1.16", "1.17", "1.18", "1.19", "1.20"),
        correctAnswerIndex = 3,
        rewards = listOf(ItemReward(ItemStack(Material.MANGROVE_LOG, 8)), ItemReward(ItemStack(Material.MUD, 8))),
        shuffleAnswers = false,
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which Minecraft update was called Village & Pillage?",
        answers = listOf("1.14", "1.15", "1.16", "1.17", "1.18"),
        correctAnswerIndex = 0,
        rewards = listOf(ItemReward(ItemStack(Material.BAMBOO, 64))),
        shuffleAnswers = false,
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "The April Fools update \"One Block at a Time\" removed the player's inventory for a day. Which year did this happen?",
        answers = listOf("2019", "2020", "2021", "2022", "2023"),
        correctAnswerIndex = 3,
        rewards = listOf(ItemReward(Material.CHEST)),
        shuffleAnswers = false,
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of these items has no smelting recipe when placed in a furnace?",
        answers = listOf("Steak", "Ancient Debris", "Iron Horse Armor", "Sea Pickle"),
        correctAnswerIndex = 0,
        rewards = listOf(ItemReward(ItemStack(Material.COOKED_BEEF, 2)), ItemReward(Material.FURNACE)),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following can a fox NOT eat?",
        answers = listOf("Beetroot", "Honey Bottle", "Cake", "Chorus Fruit"),
        correctAnswerIndex = 2,
        rewards = listOf(ItemReward(Material.CAKE), ItemReward(Material.FOX_SPAWN_EGG)),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following will NOT appear in a desert pyramid chest?",
        answers = listOf("Bone", "Sand", "Sandstone", "Saddle"),
        correctAnswerIndex = 2,
        rewards = listOf(ItemReward(ItemStack(Material.SANDSTONE, 64)), ItemReward(ItemStack(Material.BONE, 32)), ItemReward(Material.SADDLE)),
      )
    },
    {
      NumericalQuestion(
        questionBody = "How many signs can be stacked in a single inventory slot?",
        correctAnswer = 16,
        rewards = BlockTypes.WOODEN_SIGNS.toList().map { ItemReward(it) },
      )
    },
    {
      NumericalQuestion(
        questionBody = "What is the highest level of Blast Protection obtainable in a standard Minecraft game?",
        correctAnswer = 4,
        rewards = listOf(Material.IRON_CHESTPLATE, Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_HELMET).map { EnchantedItemReward(ItemStack(it).withEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1)) },
      )
    }
  )

  val SUPPLIER: TriviaQuestionSupplier = TriviaQuestionSupplier {
    val questionFunction = QUESTIONS.sample()!!
    questionFunction()
  }

}
