package net.h4bbo.echo.plugin.room;

import net.h4bbo.echo.api.plugin.DependsOn;
import net.h4bbo.echo.api.plugin.JavaPlugin;
import net.h4bbo.echo.api.services.room.IRoomService;
import net.h4bbo.echo.plugin.room.listeners.PlayerListener;
import net.h4bbo.echo.plugin.room.game.managers.RoomManager;
import net.h4bbo.echo.plugin.room.services.RoomService;
import net.h4bbo.echo.storage.StorageContextFactory;
import net.h4bbo.echo.storage.models.room.RoomData;
import org.oldskooler.inject4j.ServiceCollection;

import java.sql.SQLException;

@DependsOn({"HandshakePlugin"})
public class RoomPlugin extends JavaPlugin {
    private RoomManager roomManager;

    @Override
    public void assignServices(ServiceCollection services) {
        services.addTransient(IRoomService.class, RoomService.class);
        services.addSingleton(RoomManager.class);
    }

    @Override
    public void load() {
        this.roomManager = this.getServices().createInstance(RoomManager.class);
        this.getEventManager().register(this, this.getServices().createInstance(PlayerListener.class));
        this.resetVisits();
    }

    private void resetVisits() {
        try (var ctx = StorageContextFactory.getStorage()) {
            int rows = ctx.from(RoomData.class)
                    .filter(f -> f.greater(RoomData::getVisitorsNow, 0))
                    .update(s -> s.set(RoomData::getVisitorsNow, 0));

            this.getLogger().debug("Reset {} room visitors", rows);
        } catch (SQLException ex) {
            this.getLogger().error("Error executing query: ", ex);
        }
    }

    @Override
    public void unload() {

    }

    public RoomManager getRoomManager() {
        return roomManager;
    }
}