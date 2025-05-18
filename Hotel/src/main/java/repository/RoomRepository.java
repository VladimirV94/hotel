package repository;

import model.Room;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findById(int roomId);

    Collection<Room> findAll();

    Collection<LocalDate> findDatesByRoomId(int roomId);

    void saveNewReservation(int roomId, Collection<LocalDate> dates);
}
