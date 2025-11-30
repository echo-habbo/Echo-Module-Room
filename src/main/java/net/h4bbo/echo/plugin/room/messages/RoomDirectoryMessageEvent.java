package net.h4bbo.echo.plugin.room.messages;

import net.h4bbo.echo.api.game.player.IPlayer;
import net.h4bbo.echo.api.game.room.IRoom;
import net.h4bbo.echo.api.messages.MessageEvent;
import net.h4bbo.echo.api.network.codecs.DataCodec;
import net.h4bbo.echo.api.network.codecs.IClientCodec;
import net.h4bbo.echo.plugin.room.RoomPlugin;
import net.h4bbo.echo.plugin.room.game.managers.RoomManager;

public class RoomDirectoryMessageEvent extends MessageEvent<RoomPlugin> {
    private final RoomManager roomManager;

    public RoomDirectoryMessageEvent(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public void handle(IPlayer player, IClientCodec msg) {
        player.getConnection().getMessageHandler().deregister(this.getPlugin(), RoomDirectoryMessageEvent.class);

        var bytes = msg.readBytes(1);
        var isPublic = bytes[0] == 'A';

        if (!isPublic) {
            return;
        }

        int roomId = msg.get(DataCodec.VL64_INT);
        IRoom room = this.roomManager.tryAddRoom(roomId);
        room.getEntityManager().enter(player);

        if (!player.getConnection().getMessageHandler().isRegistered(LeaveRoomMessageEvent.class)) {
            player.getConnection().getMessageHandler().register(this.getPlugin(), LeaveRoomMessageEvent.class);
        }
    }

    @Override
    public int getHeaderId() {
        return 2;
    }
}
