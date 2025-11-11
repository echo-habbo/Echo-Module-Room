package net.h4bbo.echo.plugin.room.game.factory;

import net.h4bbo.echo.api.game.entity.EntityType;
import net.h4bbo.echo.api.game.entity.IEntity;
import net.h4bbo.echo.api.game.room.entities.RoomEntity;
import net.h4bbo.echo.plugin.room.game.entities.RoomPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RoomEntityFactory {
    private static final Map<EntityType, Function<IEntity, RoomEntity>> factories = new HashMap<>();

    static {
        factories.put(EntityType.PLAYER, RoomPlayerEntity::new);
    }

    public static RoomEntity create(IEntity entity) {
        return factories.get(entity.getType()).apply(entity);
    }
}