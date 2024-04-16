package cn.timebather.create_route.content.station.screen.board_port;

import net.minecraft.client.Minecraft;

public class BoardPortScreenScreenLoader {
    public static void open(BoardPortScreenBlockEntity screen){
        Minecraft.getInstance().setScreen(new BoardPortScreenEditScreen(screen));
    }
}
