package cn.timebather.create_route;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;

@Mod.EventBusSubscriber
public class PlayerResourceManager {
    HashMap<Player,HashSet<CloseHandler>> handlers = new HashMap<>();

    @SubscribeEvent
    public void onCloseTriggered(PlayerEvent.PlayerLoggedOutEvent playerEvent){
        this.handlers.get(playerEvent.getEntity()).forEach(CloseHandler::close);
        this.handlers.remove(playerEvent.getEntity());
    }

    public void register(Player player,CloseHandler handler){
        if(!this.handlers.containsKey(player))
            this.handlers.put(player,new HashSet<>());
        this.handlers.get(player).add(handler);
    }

    public void unregister(Player player,CloseHandler handler){
        if(!this.handlers.containsKey(player))
            return;
        this.handlers.get(player).remove(handler);
        if(this.handlers.get(player).isEmpty())
            this.handlers.remove(player);
    }

    public boolean has(Player player,CloseHandler handler){
        return this.handlers.containsKey(player) && this.handlers.get(player).contains(handler);
    }

    @FunctionalInterface
    public static interface CloseHandler{
        public void close();
    }
}
