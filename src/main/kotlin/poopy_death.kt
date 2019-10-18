import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.LogManager

const val MODID = "poopy_death"
const val NAME = "Poopy Death"
const val VERSION = "0.1.0"

val logger = LogManager.getLogger(MODID)


@Mod(modid=MODID, name=NAME, version=VERSION, modLanguageAdapter="net.shadowfacts.forgelin.KotlinAdapter")
object PoopyDeathMod{
    @Mod.EventHandler()
    fun preInit(event: FMLPreInitializationEvent) {
        logger.info("Pre-init fired for ${event.side}")

    }

    @Mod.EventHandler()
    fun init(event: FMLInitializationEvent) {
        logger.info("Init fired for ${event.side}")

    }

    @Mod.EventHandler()
    fun postInit(event: FMLPostInitializationEvent) {
        logger.info("Post-init fired for ${event.side}")
    }
}

@Mod.EventBusSubscriber(modid=MODID)
object EventHandler {
    @JvmStatic
    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Item>) {
        event.registry.register(PoopItem)
    }

    @JvmStatic
    @SubscribeEvent
    fun registerModels(event: ModelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(PoopItem, 0, ModelResourceLocation(PoopItem.registryName ?: return, "inventory"))
    }
}

@Mod.EventBusSubscriber(modid=MODID)
object DeathEventHandler {
    @SubscribeEvent
    fun drop_poop_on_death(event: LivingDropsEvent) {
        logger.info("Death Detected")
        val dead = event.entityLiving
        val world = dead.entityWorld
        event.drops.add(EntityItem(world, dead.posX, dead.posY, dead.posZ, ItemStack(PoopItem)))
    }
}

object PoopItem : Item() {
    init {
        this.registryName = ResourceLocation(MODID, "poop")
        this.unlocalizedName = "poop"
        this.creativeTab = CreativeTabs.MISC
        this.canItemEditBlocks()
    }

    fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand, item: EntityItem) {
        return this.onItemRightClick(world, player, hand, item)  // Need to turn this into bonemeal action
    }
}