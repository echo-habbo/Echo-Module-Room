package net.h4bbo.echo.plugin.room.services;

import net.h4bbo.echo.api.services.room.IRoomService;
import net.h4bbo.echo.storage.StorageContextFactory;
import net.h4bbo.echo.storage.models.room.RoomData;
import net.h4bbo.echo.storage.models.user.UserData;
import org.oldskooler.entity4j.Query;
import org.oldskooler.simplelogger4j.SimpleLog;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

public class RoomService implements IRoomService {
    public static final SimpleLog logger = SimpleLog.of(RoomService.class);

    @Override
    public RoomData getRoom(int roomId) {
        return this.getRooms(x -> x.equals(RoomData::getId, roomId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Room with ID " + roomId + " not found"));
    }

    @Override
    public List<RoomData> getRoomsByCategory(int categoryId) {
        return this.getRooms(x -> x.equals(RoomData::getCategoryId, categoryId));
    }

    @Override
    public List<RoomData> getRoomsByUserId(int userId) {
        return this.getRooms(x -> x.equals(RoomData::getOwnerId, userId));
    }

    @Override
    public void saveRoomSlots(int roomId, int slots) {

    }

    @Override
    public List<RoomData> getRooms(Function<Query.Filters<RoomData>, Query.Filters<RoomData>> predicate) {
        try (var ctx = StorageContextFactory.getStorage()) {
            var query = ctx.from(RoomData.class).as("r")
                    .select(s -> s
                            .all(RoomData.class)
                            .col(UserData.class, UserData::getName).as("owner_name"))
                    .leftJoin(UserData.class, "u", on ->
                            on.eq(RoomData::getOwnerId, UserData::getId))

                    .filter(predicate)
                    .orderBy(RoomData::getVisitorsNow, false);
            return query.toList();
        } catch (SQLException e) {
            logger.error("Error loading rooms: ", e);
        }

        return null;
    }
}


