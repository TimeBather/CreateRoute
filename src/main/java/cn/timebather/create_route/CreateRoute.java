package cn.timebather.create_route;

import cn.timebather.create_route.content.http.RouteModHttpTransformer;
import cn.timebather.create_route.content.train.AllMovingBehaviours;
import cn.timebather.create_route.content.train.AllTrainDevices;
import cn.timebather.create_route.infrastructure.http.HttpServer;
import cn.timebather.create_route.infrastructure.http.JsonWsHttpHandler;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateRoute.MODID)
public class CreateRoute
{
    public static final String MODID = "create_route";

    public static final Registrate REGISTRATE = Registrate.create(MODID);

    public static final RouteManager ROUTE_MANAGER = new RouteManager();

    public static final HttpServer HTTP_SERVER = new HttpServer(()-> JsonWsHttpHandler.of(new RouteModHttpTransformer()));

    public CreateRoute()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        AllBlocks.init();
        AllBlockEntities.init();
        AllMenuTypes.init();
        AllTrainDevices.init(modEventBus);
        AllMovingBehaviours.init(modEventBus);
        AllPackets.registerPackets();
        AllItems.init();
        AllCreateMovingBehaviours.init();
        HTTP_SERVER.up();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
