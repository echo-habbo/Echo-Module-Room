package net.h4bbo.echo.plugin.room.listeners;

import net.h4bbo.echo.api.event.EventHandler;
import net.h4bbo.echo.api.event.types.player.PlayerLoginEvent;
import net.h4bbo.echo.plugin.room.RoomPlugin;
import net.h4bbo.echo.plugin.room.messages.GetInterestMessageEvent;
import net.h4bbo.echo.plugin.room.services.RoomService;
import net.h4bbo.echo.storage.models.user.UserData;

public class PlayerListener {
    private RoomPlugin plugin;
    private RoomService roomService;

    public PlayerListener(RoomPlugin plugin, RoomService roomService) {
        this.plugin = plugin;
        this.roomService = roomService;
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        System.out.println("this.plugin" + this.plugin.getName());
        var messageHandler = event.getPlayer().getConnection().getMessageHandler();
        messageHandler.register(this.plugin, GetInterestMessageEvent.class);
        // messageHandler.register(this, UserInfoMessageEvent.class);
    }
}
