
package com.mercerenies.turtletroll.trivia.question

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.BlockTypes

import org.bukkit.Material
import org.bukkit.potion.PotionType
import org.bukkit.potion.PotionData
import org.bukkit.inventory.ItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.`meta`.EnchantmentStorageMeta
import org.bukkit.inventory.`meta`.PotionMeta

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
      MultipleChoiceQuestion(
        questionBody = "In which of the following biomes do cacti NOT generate naturally?",
        answers = listOf("Desert", "Badlands", "Wooded Badlands", "Ice Desert", "Desert Lakes"),
        correctAnswerIndex = 3,
        rewards = listOf(
          ItemReward(ItemStack(Material.CACTUS, 32)),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Piglins can give enchanted books during bartering. Which enchantment is on books obtained this way?",
        answers = listOf("Soul Speed", "Fire Aspect", "Fire Protection", "Mending", "Power"),
        correctAnswerIndex = 0,
        rewards = run {
          val book = ItemStack(Material.ENCHANTED_BOOK)
          val meta = book.itemMeta as EnchantmentStorageMeta
          meta.addStoredEnchant(Enchantment.SOUL_SPEED, 2, true)
          book.itemMeta = meta
          listOf(ItemReward(book))
        },
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "At roughly what X and Z coordinate did the famous \"Far Lands\" glitch occur in old versions of Minecraft?",
        answers = listOf("12,550,000", "13,300,000", "15,001,000", "11,910,000", "14,014,000", "19"),
        correctAnswerIndex = 0,
        rewards = listOf(
          ItemReward(ItemStack(Material.STONE, 64)), ItemReward(ItemStack(Material.DIRT, 64)),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which Minecraft Java Edition version fixed the Far Lands glitch?",
        answers = listOf("Beta 1.6", "Beta 1.7", "Beta 1.8", "Beta 1.9"),
        correctAnswerIndex = 2,
        rewards = listOf(
          ItemReward(ItemStack(Material.STONE, 64)), ItemReward(ItemStack(Material.DIRT, 64)),
        ),
        shuffleAnswers = false,
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which type of wood is NOT present in Woodland Mansions?",
        answers = listOf("Oak", "Birch", "Dark Oak", "Acacia"),
        correctAnswerIndex = 3,
        rewards = listOf(
          ItemReward(ItemStack(Material.ACACIA_PLANKS, 16)),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following CANNOT be placed inside a minecart?",
        answers = listOf("Chest", "Furnace", "Hopper", "TNT", "Dispenser"),
        correctAnswerIndex = 4,
        rewards = listOf(
          ItemReward(Material.DISPENSER),
          ItemReward(Material.MINECART),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following do pufferfish have a chance to drop in Java Edition?",
        answers = listOf("Bone Meal", "Rotten Flesh", "Bone", "Bone Block"),
        correctAnswerIndex = 0,
        rewards = listOf(
          ItemReward(ItemStack(Material.BONE_MEAL, 8)),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following enchantments has NO effect when worn by an armor stand?",
        answers = listOf("Frost Walker", "Depth Strider", "Thorns", "Protection"),
        correctAnswerIndex = 3,
        rewards = listOf(
          ItemReward(Material.ARMOR_STAND),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following is NOT the name of a default Minecraft player skin?",
        answers = listOf("Steve", "Alex", "Sunny", "Zuri", "Efe", "Garth"),
        correctAnswerIndex = 5,
        rewards = run {
          val names = listOf("Steve", "Alex", "Noor", "Sunny", "Ari", "Zuri", "Makena", "Kai", "Efe")
          names.map { PlayerHeadReward(it) }
        },
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "Which of the following is NOT a gameRule?",
        answers = listOf("updateLightRadius", "maxEntityCramming", "keepInventory", "universalAnger", "waterSourceConversion"),
        correctAnswerIndex = 0,
        rewards = listOf(
          ItemReward(ItemStack(Material.TORCH, 16)),
          ItemReward(ItemStack(Material.BLACK_CANDLE, 8)),
          ItemReward(ItemStack(Material.LANTERN, 4)),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "What is the correct spelling of the command to ban an IP address from the server?",
        answers = listOf("/ban-ip", "/banip", "/ban_ip", "/ip-ban", "/ipban", "/ip_ban"),
        correctAnswerIndex = 0,
        rewards = listOf(
          ItemReward(Material.WOODEN_AXE),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "The 2020 April Fools update included a stair block made of netherite. What was it called?",
        answers = listOf("Netherite Stairs", "Nether Stairs", "Floop", "Swaggiest Stairs Ever", "Dark Stairs"),
        correctAnswerIndex = 3,
        rewards = listOf(
          ItemReward(ItemStack(Material.SANDSTONE_STAIRS, 32)),
          ItemReward(ItemStack(Material.ANDESITE_STAIRS, 32)),
        ),
      )
    },
    {
      MultipleChoiceQuestion(
        questionBody = "As defined by the game's source code, what is the name of the bullet entity that Shulkers shoot?",
        answers = listOf("Spark", "Shulker Bullet", "Hunter", "Shulker Seeker"),
        correctAnswerIndex = 0,
        rewards = run {
          val itemStack = ItemStack(Material.POTION)
          val meta = itemStack.itemMeta as PotionMeta
          meta.setBasePotionData(PotionData(PotionType.SLOW_FALLING))
          itemStack.itemMeta = meta
          listOf(ItemReward(itemStack))
        },
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
        questionBody = "Excluding \"Unemployed\" and \"Nitwit\", how many villager professions are there?",
        correctAnswer = 13,
        rewards = listOf(
          ItemReward(Material.VILLAGER_SPAWN_EGG),
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

}
