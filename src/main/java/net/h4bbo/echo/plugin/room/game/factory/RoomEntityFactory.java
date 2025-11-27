package net.h4bbo.echo.plugin.room.game.factory;

import net.h4bbo.echo.api.game.entity.EntityType;
import net.h4bbo.echo.api.game.entity.IEntity;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.game.room.entities.RoomEntity;
import net.h4bbo.echo.plugin.room.game.entities.RoomPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RoomEntityFactory {
    private static final Map<EntityType, BiFunction<IEntity, IRoom, RoomEntity>> factories = new HashMap<>();

    static {
        factories.put(EntityType.PLAYER, (entity, room) -> new RoomPlayerEntity(room, entity));
    }

    public static RoomEntity create(IEntity entity, IRoom room) {
        Objects.requireNonNull(entity, "Entity parameter cannot be null");
        Objects.requireNonNull(room, "Room parameter cannot be null");
        return factories.get(entity.getType()).apply(entity, room);
    }
}