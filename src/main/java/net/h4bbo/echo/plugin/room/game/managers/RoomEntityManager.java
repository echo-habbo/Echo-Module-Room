package net.h4bbo.echo.plugin.room.game.managers;

import net.h4bbo.echo.api.game.entity.EntityType;
import net.h4bbo.echo.api.game.entity.IEntity;
import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.game.room.entities.RoomEntity;
import net.h4bbo.echo.api.game.room.managers.IRoomEntityManager;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.api.services.room.IRoomService;
import net.h4bbo.echo.codecs.PacketCodec;
import net.h4bbo.echo.common.util.collections.SynchronizedMap;
import net.h4bbo.echo.plugin.room.RoomPlugin;
import net.h4bbo.echo.plugin.room.game.Room;
import net.h4bbo.echo.plugin.room.game.entities.RoomPlayerEntity;
import net.h4bbo.echo.plugin.room.game.factory.RoomEntityFactory;
import net.h4bbo.echo.storage.models.user.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RoomEntityManager extends IRoomEntityManager {
    private SynchronizedMap<Integer, IEntity> entityMap;

    public RoomEntityManager(IRoom room) {
        super(room);
        this.entityMap = new SynchronizedMap<>();
    }

    /**
     * Finds the next lowest available instance ID
     * @return the lowest available ID (starting from 0)
     */
    private int getNextAvailableInstanceId() {
        int nextId = 0;

        var occupiedInstanceIds = this.entityMap.values().stream()
                .map(x -> x.getRoomEntity().getInstanceId())
                .collect(Collectors.toSet());

        // Iterate through IDs starting from 0 to find first available
        while (occupiedInstanceIds.contains(nextId)) {
            nextId++;
        }

        return nextId;
    }

    @Override
    public void enter(IEntity entity) {
        var roomEntity = RoomEntityFactory.create(entity);
        roomEntity.setInstanceId(this.getNextAvailableInstanceId());
        entity.attr(RoomEntity.DATA_KEY).set(roomEntity);

        this.entityMap.put(entity.attr(UserData.DATA_KEY).get().getId(), entity);

        if (entity.getType().isClient()) {
            PacketCodec.create(166)
                    .append(DataCodec.STRING, "/client/")
                    .send(entity);

            PacketCodec.create(69)
                    .append(DataCodec.BYTES, this.getRoom().getData().getModel())
                    .append(DataCodec.BYTES, " ")
                    .append(DataCodec.BYTES, this.getRoom().getData().getId())
                    .send(entity);

            //PacketCodec.create(46)
            //        .append(DataCodec.BYTES, "landscape")
            //        .append(DataCodec.BYTES, "/")
            //        .append(DataCodec.BYTES, "0")
            //        .send(entity);

            System.out.println("Instance ID: " + roomEntity.getInstanceId());
        }
    }
}
