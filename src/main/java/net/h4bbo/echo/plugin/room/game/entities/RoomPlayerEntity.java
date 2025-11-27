package net.h4bbo.echo.plugin.room.game.entities;

import net.h4bbo.echo.api.game.entity.IEntity;
import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.game.room.entities.RoomEntity;
import net.h4bbo.echo.api.game.room.managers.IRoomEntityManager;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.codecs.PacketCodec;
import net.h4bbo.echo.common.util.collections.SynchronizedMap;
import net.h4bbo.echo.storage.models.user.UserData;

public class RoomPlayerEntity extends RoomEntity {
    public RoomPlayerEntity(IRoom room, IEntity entity) {
        super(room, entity);
    }

    @Override
    public void resetState() {

    }
}