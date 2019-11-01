import net.java.games.input.Mouse
import net.minecraft.block.IGrowable
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemDye
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
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

    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val itemstack = player.getHeldItem(hand)

        if (!player.canPlayerEdit(pos.offset(side), side, itemstack)) {
            return EnumActionResult.FAIL
        } else {
            if (fertilize(itemstack, world, pos, player, hand)) {
                if (!world.isRemote) {
                    world.playEvent(2005, pos, 0)
                }

                return EnumActionResult.SUCCESS
            }
        }
        return EnumActionResult.PASS
    }

    private fun fertilize(stack: ItemStack, worldIn: World, target: BlockPos, player: EntityPlayer, hand: EnumHand): Boolean {
        val iblockstate = worldIn.getBlockState(target)
        val hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, target, iblockstate, stack, hand)
        if (hook != 0) return hook > 0

        if (iblockstate.block is IGrowable) {
            val igrowable = iblockstate.block as IGrowable

            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate)
                    }

                    stack.shrink(1)
                }

                return true
            }
        }

        return false
    }
}