package net.h4bbo.echo.plugin.room.game;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import io.netty.util.DefaultAttributeMap;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.game.room.managers.IRoomEntityManager;
import net.h4bbo.echo.plugin.room.RoomPlugin;
import net.h4bbo.echo.plugin.room.game.managers.RoomEntityManager;
import net.h4bbo.echo.storage.models.room.RoomData;

public class Room extends IRoom {
    private final RoomEntityManager entityManager;
    private final RoomPlugin plugin;

    public Room(RoomPlugin plugin, RoomData roomData) {
        super(roomData);
        this.plugin = plugin;
        this.entityManager = new RoomEntityManager(this);
    }

    public RoomPlugin getPlugin() {
        return plugin;
    }

    @Override
    public IRoomEntityManager getEntityManager() {
        return entityManager;
    }


}
