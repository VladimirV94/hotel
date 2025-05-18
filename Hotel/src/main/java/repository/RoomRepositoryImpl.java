package repository;

import model.Room;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RoomRepositoryImpl implements RoomRepository 
{
	Set<Room> allRooms = new HashSet<Room>(5);
    {
    	allRooms.add(new Room(1, "тип 1", 100));
    	allRooms.add(new Room(2, "тип 2", 200));
    	allRooms.add(new Room(3, "тип 3", 300));
    	allRooms.add(new Room(4, "тип 4", 400));
    	allRooms.add(new Room(5, "тип 5", 500));
    }
    
    Map<Room, Collection<LocalDate>> reservatedDays = new HashMap<>(allRooms.size());

    @Override
    public Optional<Room> findById(int roomId) 
    {
        return findAll().stream().filter(room -> room.id() == roomId).findFirst();
    }

    @Override
    public Collection<Room> findAll() 
    {
        return allRooms;
    }

    @Override
    public Collection<LocalDate> findDatesByRoomId(int roomId) 
    {
    	return findById(roomId).map(reservatedDays::get).orElseThrow(() -> 
    		new IllegalArgumentException("Комнаты с указанным id не существует"));
    }

    @Override
    public void saveNewReservation(int roomId, Collection<LocalDate> dates) 
    {
    	Room room = findById(roomId).orElseThrow(() -> 
    		new IllegalArgumentException("Комнаты с указанным id не существует"));
		
    	Optional.ofNullable(reservatedDays.get(room)).ifPresentOrElse(
			reservatedDatesByRoom -> reservatedDatesByRoom.addAll(dates),
			() -> reservatedDays.put(room, dates));
    }
}
