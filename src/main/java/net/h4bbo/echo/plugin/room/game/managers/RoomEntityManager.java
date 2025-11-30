package net.h4bbo.echo.plugin.room.game.managers;

import net.h4bbo.echo.api.game.entity.IEntity;
import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.game.room.entities.RoomEntity;
import net.h4bbo.echo.api.game.room.managers.IRoomEntityManager;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.api.services.room.IRoomService;
import net.h4bbo.echo.codecs.PacketCodec;
import net.h4bbo.echo.common.util.collections.SynchronizedMap;
import net.h4bbo.echo.plugin.room.game.factory.RoomEntityFactory;
import net.h4bbo.echo.storage.models.user.UserData;

import java.util.*;
import java.util.stream.Collectors;

public class RoomEntityManager extends IRoomEntityManager {
    private final SynchronizedMap<Integer, IEntity> entityMap;

    public RoomEntityManager(IRoom room) {
        super(room);
        this.entityMap = new SynchronizedMap<>();
    }

    public <T> List<T> getEntities(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (Object entity : this.entityMap.values()) {
            try {
                result.add(clazz.cast(entity));
            } catch (ClassCastException e) {
                // Skip incompatible types
            }
        }
        return result;
    }


    /**
     * Finds the next lowest available instance ID
     * @return the lowest available ID (starting from 0)
     */
    private int getNextAvailableInstanceId() {
        int nextId = 0;

        var occupiedInstanceIds = this.entityMap.stream()
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
        Objects.requireNonNull(entity, "Entity parameter cannot be null");

        var roomEntity = RoomEntityFactory.create(entity, this.getRoom());
        Objects.requireNonNull(roomEntity, "RoomEntity instance cannot be null, possibly missing a mapped entity type?");
        entity.attr(RoomEntity.DATA_KEY).set(roomEntity);

        roomEntity.setInstanceId(this.getNextAvailableInstanceId());
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

            var dataService = this.getRoom()
                .getPlugin()
                .getServices()
                .getRequiredService(IRoomService.class);

            dataService.saveRoomSlots(this.getRoom().getData().getId(), this.getEntities(IPlayer.class).size());

            System.out.println("Instance ID: " + roomEntity.getInstanceId());
        }
    }

    @Override
    public void leave(IEntity entity) {
        Objects.requireNonNull(entity, "Entity parameter cannot be null");

        this.entityMap.remove(entity.attr(UserData.DATA_KEY).get().getId());
        entity.attr(RoomEntity.DATA_KEY).set(null);
    }
}
