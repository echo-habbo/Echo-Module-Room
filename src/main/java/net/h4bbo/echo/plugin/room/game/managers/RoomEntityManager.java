package net.h4bbo.echo.plugin.room.game.managers;

import net.h4bbo.echo.api.game.entity.EntityType;
import net.h4bbo.echo.api.game.entity.IEntity;
import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.game.room.managers.IRoomEntityManager;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.api.services.room.IRoomService;
import net.h4bbo.echo.codecs.PacketCodec;
import net.h4bbo.echo.plugin.room.RoomPlugin;
import net.h4bbo.echo.plugin.room.game.Room;

import java.util.HashMap;
import java.util.Map;

public class RoomEntityManager extends IRoomEntityManager {
    public RoomEntityManager(IRoom room) {
        super(room);
    }

    @Override
    public void enter(IEntity entity) {

        if (entity.getType() == EntityType.PLAYER) {
            this.enterForClient((IPlayer) entity);
        }
    }

    private void enterForClient(IPlayer player) {
        try {
            PacketCodec.create(166)
                    .append(DataCodec.STRING, "/client/")
                    .send(player);

            PacketCodec.create(69)
                    .append(DataCodec.STRING, this.getRoom().getData().getModel())
                    .append(DataCodec.STRING, " ")
                    .append(DataCodec.VL64_INT, this.getRoom().getData().getId())
                    .send(player);

            PacketCodec.create(46)
                    .append(DataCodec.BYTES, "landscape")
                    .append(DataCodec.BYTES, "/")
                    .append(DataCodec.BYTES, "0")
                    .send(player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
