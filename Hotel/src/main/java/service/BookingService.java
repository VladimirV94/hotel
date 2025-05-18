package service;

import model.Room;
import repository.RoomRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

public class BookingService
{
    private final RoomRepository roomRepository;

    public BookingService(RoomRepository roomRepository)
    {
        this.roomRepository = roomRepository;
    }

    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) 
    {
    	//  Комната не доступна в прошлом
    	if(checkIn.isBefore(LocalDate.now()) || checkOut.isBefore(LocalDate.now()))
    	{
    		return false;
    	}
    	// День заселения не может быть после дня выселения
    	if(!checkIn.isEqual(checkOut) && !checkIn.isBefore(checkOut))
    	{
    		return false;
    	}
    	
    	Collection<LocalDate> reservedDatesByRoom = roomRepository.findDatesByRoomId(room.id());
    	// Получаем период времени в днях с момента заселения до момента выселения включая этот день
    	return !checkIn.datesUntil(checkOut.plusDays(1))
    		// Смотрим, зарезервирован ли уже хотя бы один день в данный период
    		.filter(day_to_reserve_room -> reservedDatesByRoom.contains(day_to_reserve_room))
    		// возвращаем false(комната не доступна) - если хотя бы в один из указанных дней комната занята
    		// возвращаем true(комната доступна), если все дни на данный период свободны
    		.findFirst().isPresent();
    }

    public boolean bookRoom(Room room, LocalDate checkIn, LocalDate checkOut) 
    {
    	if(isRoomAvailable(room, checkIn, checkOut))
    	{
    		roomRepository.saveNewReservation(room.id(), 
    				checkIn.datesUntil(checkOut.plusDays(1)).collect(Collectors.toList()));
    		return true;
    	}
        return false;
    }

    public double calculatePrice(Room room, LocalDate checkIn, LocalDate checkOut) 
    {
    	if(!checkIn.isEqual(checkOut) && !checkIn.isBefore(checkOut))
    	{
    		throw new IllegalArgumentException("День заселения не может быть после дня выселения");
    	}
    	return room.pricePerNight() * ChronoUnit.DAYS.between(checkIn, checkOut.plusDays(1));
    }
}
