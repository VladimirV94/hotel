package tests;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import model.Room;
import repository.RoomRepository;
import service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest 
{
	@InjectMocks
	private BookingService bookingService;
	
	@Mock
	private RoomRepository roomRepository;

	private Room room;

	private LocalDate previousDate;
	private LocalDate currentDate;
	private LocalDate nextDate;

	
	@BeforeEach
    void setUp() 
    {
		room = new Room(1, "тип 1", 100);
		currentDate = LocalDate.now();
		nextDate = currentDate.plusDays(1);
		previousDate = currentDate.minusDays(1);
		// Mockito.when(roomRepository.findAll()).thenReturn(List.of(room));
    }

    @Test
    @DisplayName("Проверка метода isRoomAvailable")
    void givenRoomAndCheckInDate_whenCheckAvailable_thenReturnTrue() 
    {
    	// Комната доступна на несколько дней
    	Assertions.assertEquals(bookingService.isRoomAvailable(room, currentDate, nextDate), true);
    	// Комната доступна на один день
    	Assertions.assertEquals(bookingService.isRoomAvailable(room, currentDate, currentDate), true);
    }

    @Test
    @DisplayName("Проверка метода isRoomAvailable")
    void givenRoomAndCheckInDate_whenCheckAvailable_thenReturnFalse() 
    {
    	// Комната не доступна, когда день выселения до дня заселения
    	Assertions.assertEquals(bookingService.isRoomAvailable(room, nextDate, currentDate), false);
    	// Комната не доступна, когда день заселения в прошлом
    	Assertions.assertEquals(bookingService.isRoomAvailable(room, previousDate, currentDate), false);
    	// Комната не доступна, когда день выселения в прошлом
    	Assertions.assertEquals(bookingService.isRoomAvailable(room, currentDate, previousDate), false);
    	// Комната не доступна, когда оба дня в прошлом
    	Assertions.assertEquals(bookingService.isRoomAvailable(room, previousDate, previousDate), false);
    }

    @Test
    @DisplayName("Проверка функциональности bookRoom")
    void givenRoomAndCheckDates_whenBook_thenReturnTrue() 
    {
    	// Комната забронирована на несколько дней
    	Assertions.assertEquals(bookingService.bookRoom(room, currentDate, nextDate), true);
    	// Комната забронирована на один день
    	Assertions.assertEquals(bookingService.bookRoom(room, currentDate, currentDate), true);
    }

    @Test
    @DisplayName("Проверка функциональности bookRoom")
    void givenRoomAndCheckDates_whenBook_thenReturnFalse() 
    {
    	// Комната не забронирована, когда день выселения до дня заселения
    	Assertions.assertEquals(bookingService.bookRoom(room, nextDate, currentDate), false);
    	// Комната не забронирована, когда день заселения в прошлом
    	Assertions.assertEquals(bookingService.bookRoom(room, previousDate, currentDate), false);
    	// Комната не забронирована, когда день выселения в прошлом
    	Assertions.assertEquals(bookingService.bookRoom(room, currentDate, previousDate), false);
    	// Комната не забронирована, когда оба дня в прошлом
    	Assertions.assertEquals(bookingService.bookRoom(room, previousDate, previousDate), false);
    }

    @Test
    @DisplayName("Проверка функциональности calculatePrice")
    void givenRoomAndCheckDates_whenCalculateSum_thenReturnSum() 
    {
    	// цена за 4 дня
    	Assertions.assertEquals(400, bookingService.calculatePrice(room, currentDate, currentDate.plusDays(3)));
    	// цена за 1 день
    	Assertions.assertEquals(100, bookingService.calculatePrice(room, currentDate, currentDate));
    	
    }
}