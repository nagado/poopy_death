import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
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
    fun drop_poop_on_death(event: LivingDeathEvent) {
        logger.info("Death Detected")
        logger.info(event.entity.entityDropItem(ItemStack(PoopItem), 0.0F))
    }
}

object PoopItem : Item() {
    init{
        this.registryName = ResourceLocation(MODID, "poop")
        this.unlocalizedName = "poop"
        this.creativeTab = CreativeTabs.MISC
    }
}