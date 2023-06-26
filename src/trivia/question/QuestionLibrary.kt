
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.BlockTypes

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.enchantments.Enchantment

object QuestionLibrary {

  val QUESTIONS: List<() -> TriviaQuestion> = listOf(
    {
      MultipleChoiceQuestion(
        questionBody = "At what difficulty does the world need to be set to for zombies to break down doors?",
        answers = listOf("Peaceful", "Easy", "Medium", "Hard"),
        correctAnswerIndex = 3,
        rewards = toRewards(BlockTypes.WOODEN_DOORS) + listOf(ItemReward(Material.ZOMBIE_SPAWN_EGG)),
        shuffleAnswers = false,
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What is the most common type of armor to spawn naturally on zombies?",
        answers = listOf("Leather", "Gold", "Chainmail", "Iron", "Diamond"),
        correctAnswerIndex = 1,
        rewards = listOf(
          ItemReward(Material.GOLDEN_BOOTS),
          ItemReward(Material.GOLDEN_HELMET),
          ItemReward(Material.GOLDEN_LEGGINGS),
          ItemReward(Material.GOLDEN_CHESTPLATE),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What was the name of the Minecraft 1.15 Update?",
        answers = listOf("World of Color", "Buzzy Bees", "Village & Pillage", "The Wild Update"),
        correctAnswerIndex = 1,
        rewards = listOf(ItemReward(ItemStack(Material.HONEYCOMB, 16))),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What was the name of the Minecraft 1.12 Update?",
        answers = listOf("World of Color", "Caves & Cliffs", "Village & Pillage", "The Wild Update"),
        correctAnswerIndex = 0,
        rewards = toRewards(BlockTypes.CONCRETE_POWDERS, stackSize = 64),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What was the name of the Minecraft 1.13 Update?",
        answers = listOf("Update Aquatic", "Trails & Tales", "Village & Pillage", "Wardens & Below"),
        correctAnswerIndex = 0,
        rewards = listOf(ItemReward(ItemStack(Material.KELP, 16))),
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
        rewards = listOf(ItemReward(ItemStack(Material.BONE, 32))),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which Minecraft update was called the Wild Update?",
        answers = listOf("1.16", "1.17", "1.18", "1.19", "1.20"),
        correctAnswerIndex = 3,
        rewards = listOf(
          ItemReward(ItemStack(Material.MANGROVE_LOG, 16)),
          ItemReward(ItemStack(Material.MUD, 32)),
        ),
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
        rewards = listOf(
          ItemReward(ItemStack(Material.COOKED_BEEF, 4)),
          ItemReward(Material.FURNACE),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following can a fox NOT eat?",
        answers = listOf("Beetroot", "Honey Bottle", "Cake", "Chorus Fruit"),
        correctAnswerIndex = 2,
        rewards = listOf(
          ItemReward(Material.CAKE),
          ItemReward(Material.FOX_SPAWN_EGG),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following will NOT appear in a desert pyramid chest?",
        answers = listOf("Bone", "Sand", "Sandstone", "Saddle"),
        correctAnswerIndex = 2,
        rewards = listOf(
          ItemReward(ItemStack(Material.SANDSTONE, 64)),
          ItemReward(ItemStack(Material.BONE, 32)),
          ItemReward(Material.SADDLE),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following is NOT a way to obtain rotten flesh?",
        answers = listOf("Cat Gifts", "Fishing", "Buried Treasure", "Mob Loot"),
        correctAnswerIndex = 2,
        rewards = listOf(
          ItemReward(ItemStack(Material.ROTTEN_FLESH, 64)),
          ItemReward(Material.POTATO),
          ItemReward(Material.CARROT),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "How tall is the player?",
        answers = listOf("1.6 blocks", "1.7 blocks", "1.8 blocks", "1.9 blocks"),
        correctAnswerIndex = 2,
        rewards = listOf(
          ItemReward(Material.CAT_SPAWN_EGG),
          ItemReward(Material.WOLF_SPAWN_EGG),
        ),
        shuffleAnswers = false,
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following mobs has NEVER existed in any Minecraft version or official Mojang spinoff title?",
        answers = listOf("Horned Sheep", "Moon Cow", "Pony", "Rat"),
        correctAnswerIndex = 3,
        rewards = listOf(
          ItemReward(Material.SHEEP_SPAWN_EGG),
          ItemReward(Material.COW_SPAWN_EGG),
          ItemReward(Material.HORSE_SPAWN_EGG),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following subtitles can a creeper produce?",
        answers = listOf("Dying", "Creeper Chirps", "Creeper Purrs", "Splashing"),
        correctAnswerIndex = 3,
        rewards = listOf(
          ItemReward(Material.CREEPER_SPAWN_EGG),
          ItemReward(Material.WATER_BUCKET),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following CANNOT be waterlogged?",
        answers = listOf("Amethyst Cluster", "Small Dripleaf", "Candle", "Ladder", "Lectern"),
        correctAnswerIndex = 4,
        rewards = listOf(
          ItemReward(Material.LECTERN),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "In which of the following biomes will polar bears NOT spawn naturally?",
        answers = listOf("Snowy Plains", "Ice Spikes", "Deep Frozen Ocean", "Tundra"),
        correctAnswerIndex = 3,
        rewards = listOf(
          ItemReward(Material.POLAR_BEAR_SPAWN_EGG),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What is the longest delay a single redstone repeater can be set to?",
        answers = listOf("0.1 seconds", "0.4 seconds", "0.8 seconds", "1.0 seconds"),
        correctAnswerIndex = 1,
        rewards = listOf(
          ItemReward(Material.REPEATER),
        ),
        shuffleAnswers = false,
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following blocks can a redstone comparator NOT read the state of?",
        answers = listOf("Beehive", "Composter", "Respawn Anchor", "Cake", "Beacon"),
        correctAnswerIndex = 4,
        rewards = listOf(
          ItemReward(Material.COMPARATOR),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following is NOT a villager profession?",
        answers = listOf("Teacher", "Leatherworker", "Farmer", "Cartographer", "Toolsmith"),
        correctAnswerIndex = 0,
        rewards = listOf(
          ItemReward(Material.VILLAGER_SPAWN_EGG),
        ),
      )
    },
    {
      NumericalQuestion(
        questionBody = "How many signs can be stacked in a single inventory slot?",
        correctAnswer = 16,
        rewards = toRewards(BlockTypes.WOODEN_SIGNS),
      )
    },
    {
      NumericalQuestion(
        questionBody = "What light level is emitted by an Ender Chest?",
        correctAnswer = 7,
        rewards = listOf(
          ItemReward(Material.ENDER_CHEST),
        ),
      )
    },
    {
      NumericalQuestion(
        questionBody = "What light level is emitted by an Amethyst Cluster?",
        correctAnswer = 5,
        rewards = listOf(
          ItemReward(ItemStack(Material.AMETHYST_CLUSTER, 4)),
        ),
      )
    },
    {
      NumericalQuestion(
        questionBody = "What light level is emitted by a Magma Block?",
        correctAnswer = 3,
        rewards = listOf(
          ItemReward(ItemStack(Material.MAGMA_BLOCK, 16)),
        ),
      )
    },
    {
      NumericalQuestion(
        questionBody = "How many stages of oxidation does a copper block undergo (including the original \"unoxidized\" state)?",
        correctAnswer = 4,
        rewards = listOf(
          ItemReward(Material.COPPER_BLOCK),
        ),
      )
    },
    {
      NumericalQuestion(
        questionBody = "What is the highest Y coordinate where deepslate can replace a stone block?",
        correctAnswer = 8,
        rewards = listOf(
          ItemReward(ItemStack(Material.DEEPSLATE, 64)),
        ),
      )
    },
    {
      NumericalQuestion(
        questionBody = "How many minecart rails are produced by the crafting recipe for rails?",
        correctAnswer = 16,
        rewards = listOf(
          ItemReward(ItemStack(Material.RAIL, 16)),
        ),
      )
    },
    {
      NumericalQuestion(
        questionBody = "What is the highest level of Blast Protection obtainable in a standard Minecraft game?",
        correctAnswer = 4,
        rewards = run {
          val armorMaterials = listOf(
            Material.IRON_CHESTPLATE, Material.IRON_BOOTS,
            Material.IRON_LEGGINGS, Material.IRON_HELMET,
          )
          toRewards(armorMaterials) {
            EnchantedItemReward(it.withEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1))
          }
        },
      )
    }
  )

  val SUPPLIER: TriviaQuestionSupplier = TriviaQuestionSupplier {
    val questionFunction = QUESTIONS.sample()!!
    questionFunction()
  }

  private fun toRewards(
    collection: Iterable<Material>,
    stackSize: Int,
    block: (ItemStack) -> TriviaQuestionReward,
  ): List<TriviaQuestionReward> =
    collection.toList().map { ItemStack(it, stackSize).let(block) }

  private fun toRewards(
    collection: Iterable<Material>,
    stackSize: Int = 1,
  ): List<TriviaQuestionReward> =
    toRewards(collection, stackSize) { ItemReward(it) }

  private fun toRewards(
    collection: Iterable<Material>,
    block: (ItemStack) -> TriviaQuestionReward,
  ): List<TriviaQuestionReward> =
    toRewards(collection, stackSize = 1, block)

  // ItemStack.addEnchantment, but fluent
  private fun ItemStack.withEnchantment(enchantment: Enchantment, level: Int): ItemStack {
    this.addEnchantment(enchantment, level)
    return this
  }

}
