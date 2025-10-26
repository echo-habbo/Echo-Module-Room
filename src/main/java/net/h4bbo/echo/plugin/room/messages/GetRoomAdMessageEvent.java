package net.h4bbo.echo.plugin.room.messages;

import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.messages.MessageEvent;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.api.network.codecs.IClientCodec;
import net.h4bbo.echo.codecs.PacketCodec;
import net.h4bbo.echo.plugin.room.RoomPlugin;
import net.h4bbo.echo.plugin.room.game.managers.RoomManager;

public class GetRoomAdMessageEvent extends MessageEvent<RoomPlugin> {
    @Override
    public void handle(IPlayer player, IClientCodec msg) {
        PacketCodec.create(208)
                .append(DataCodec.VL64_INT, 0)
                .send(player);
    }

    @Override
    public int getHeaderId() {
        return 126;
    }
}
