package net.h4bbo.echo.plugin.room.messages;

import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.messages.MessageEvent;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.api.network.codecs.IClientCodec;
import net.h4bbo.echo.codecs.PacketCodec;
import net.h4bbo.echo.plugin.room.RoomPlugin;

public class GetInterestMessageEvent extends MessageEvent<RoomPlugin> {
    @Override
    public void handle(IPlayer player, IClientCodec msg) {
        if (player.isInRoom())
            return;

        PacketCodec.create(258)
                .append(DataCodec.VL64_INT, 0)
                .send(player);


        player.getConnection().getMessageHandler().register(this.getPlugin(), RoomDirectoryMessageEvent.class);
        player.getConnection().getMessageHandler().register(this.getPlugin(), GetHeightMapMessageEvent.class);
        player.getConnection().getMessageHandler().register(this.getPlugin(), GetRoomAdMessageEvent.class);
        player.getConnection().getMessageHandler().register(this.getPlugin(), GetRoomStatusMessageEvent.class);
        player.getConnection().getMessageHandler().register(this.getPlugin(), GetRoomObjectsMessageEvent.class);
    }

    @Override
    public int getHeaderId() {
        return 182;
    }
}
