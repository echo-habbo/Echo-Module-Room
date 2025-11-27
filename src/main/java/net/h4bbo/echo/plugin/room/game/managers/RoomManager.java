package net.h4bbo.echo.plugin.room.game.managers;

import net.h4bbo.echo.api.game.entity.EntityType;
import net.h4bbo.echo.api.game.entity.IEntity;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.services.room.IRoomService;
import net.h4bbo.echo.plugin.room.RoomPlugin;
import net.h4bbo.echo.plugin.room.game.Room;
import net.h4bbo.echo.plugin.room.listeners.PlayerListener;

import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    private final RoomPlugin plugin;
    private final IRoomService roomService;
    private final Map<Integer, IRoom> activeRooms;

    public RoomManager(RoomPlugin plugin, IRoomService roomService) {
        this.plugin = plugin;
        this.roomService = roomService;
        this.activeRooms = new HashMap<>();
    }

    public IRoom tryAddRoom(int roomId) {
        if (activeRooms.containsKey(roomId)) {
            return activeRooms.get(roomId);
        }

        var room = new Room(this.plugin, this.roomService.getRoom(roomId));

        this.activeRooms.putIfAbsent(roomId, room);

        return room;
    }
}
