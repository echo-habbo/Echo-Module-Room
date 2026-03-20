package net.h4bbo.echo.plugin.room.services;

import net.h4bbo.echo.api.services.room.IRoomService;
import net.h4bbo.echo.storage.StorageContextFactory;
import net.h4bbo.echo.storage.models.room.RoomData;
import net.h4bbo.echo.storage.models.user.UserData;
import net.h4bbo.echo.storage.views.room.RoomDetailsView;
import org.oldskooler.entity4j.Query;
import org.oldskooler.entity4j.select.SelectionOrder;
import org.oldskooler.entity4j.serialization.QuerySerializer;
import org.oldskooler.simplelogger4j.SimpleLog;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RoomService implements IRoomService {
    public static final SimpleLog logger = SimpleLog.of(RoomService.class);

    @Override
    public RoomDetailsView getRoom(int roomId) {
        return this.getRooms(x -> x.equals(RoomDetailsView::getId, roomId))
                .stream()
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Room with ID " + roomId + " not found"));
    }

    @Override
    public List<RoomDetailsView> getRoomsByCategory(int categoryId) {
        return this.getRooms(x -> x.equals(RoomDetailsView::getCategoryId, categoryId));
    }

    @Override
    public List<RoomDetailsView> getRoomsByUserId(int userId) {
        return this.getRooms(x -> x.equals(RoomDetailsView::getOwnerId, userId));
    }

    @Override
    public void saveRoomSlots(int roomId, int slots) {
        try (var ctx = StorageContextFactory.getStorage()) {
            ctx.from(RoomData.class)
                    .filter(x -> x.equals(RoomData::getId, roomId))
                    .update(x -> x.set(RoomData::getVisitorsNow, slots));

        } catch (SQLException e) {
            logger.error("Error loading rooms: ", e);
        }
    }

    @Override
    public List<RoomDetailsView> getRooms(Consumer<Query.Filters<RoomDetailsView>> predicate) {
        try (var ctx = StorageContextFactory.getStorage()) {
            var query = ctx.from(RoomDetailsView.class)
                    .filter(predicate)
                    .orderBy(o -> o
                            .col(RoomData::getVisitorsNow).desc()
                    );

            return query.toList();
        } catch (SQLException e) {
            logger.error("Error loading rooms: ", e);
        }

        return null;
    }
}


