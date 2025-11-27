package net.h4bbo.echo.plugin.room.messages;

import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.messages.MessageEvent;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.api.network.codecs.IClientCodec;
import net.h4bbo.echo.codecs.PacketCodec;
import net.h4bbo.echo.plugin.room.RoomPlugin;
import org.bouncycastle.util.Pack;

public class GetRoomObjectsMessageEvent extends MessageEvent<RoomPlugin> {
    @Override
    public void handle(IPlayer player, IClientCodec msg) {
        PacketCodec.create(30)
                .send(player);

        PacketCodec.create(32)
                .append(DataCodec.VL64_INT, 0)
                .send(player);

        player.getConnection().getMessageHandler().deregister(this.getPlugin(), RoomDirectoryMessageEvent.class);
        player.getConnection().getMessageHandler().deregister(this.getPlugin(), GetHeightMapMessageEvent.class);
        player.getConnection().getMessageHandler().deregister(this.getPlugin(), GetRoomAdMessageEvent.class);
        player.getConnection().getMessageHandler().deregister(this.getPlugin(), GetRoomStatusMessageEvent.class);
        player.getConnection().getMessageHandler().deregister(this.getPlugin(), GetRoomObjectsMessageEvent.class);
    }

    @Override
    public int getHeaderId() {
        return 62;
    }
}
