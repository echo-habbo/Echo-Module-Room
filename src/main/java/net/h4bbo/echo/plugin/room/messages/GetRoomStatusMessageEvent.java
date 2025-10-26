package net.h4bbo.echo.plugin.room.messages;

import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.messages.MessageEvent;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.api.network.codecs.IClientCodec;
import net.h4bbo.echo.codecs.PacketCodec;
import net.h4bbo.echo.plugin.room.RoomPlugin;

public class GetRoomStatusMessageEvent extends MessageEvent<RoomPlugin> {
    @Override
    public void handle(IPlayer player, IClientCodec msg) {
        PacketCodec.create(31)
                .send(player);
    }

    @Override
    public int getHeaderId() {
        return 60;
    }
}
