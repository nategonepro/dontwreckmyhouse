package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestFileRepositoryDouble;
import learn.dontwreckmyhouse.data.HostFileRepositoryDouble;
import learn.dontwreckmyhouse.data.ReservationFileRepositoryDouble;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {
    ReservationService resService = new ReservationService(
            new ReservationFileRepositoryDouble(),
            new HostFileRepositoryDouble(),
            new GuestFileRepositoryDouble()
    );


    @Test
    void shouldFindByHost(){
        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        List<Reservation> actual = resService.findByHost(host);

        assertNotNull(actual);
        assertEquals(1200000, actual.get(0).getTotal().intValue());
    }

    @Test
    void shouldNotFindByMissingHost(){
        Host host = new Host();
        host.setId("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        List<Reservation> actual = resService.findByHost(host);

        assertNull(actual);
    }

    @Test
    void shouldNotFindByNullHost(){
        Host host = null;

        List<Reservation> actual = resService.findByHost(host);

        assertNull(actual);
    }

    @Test
    void shouldFindByHostIdAndResId(){
        Reservation actual = resService.findByHostIdAndResId("a7fd9dd1-2cee-4efe-a495-5fd002414675", 1);

        assertNotNull(actual);
        assertEquals("Epstein", actual.getHost().getLastName());
        assertEquals("Bob", actual.getGuest().getFirstName());
    }

    @Test
    void shouldNotFindByInvalidHostIdAndResId(){
        Reservation actual = resService.findByHostIdAndResId("aaaaaaaaaaaa", 2);

        assertNull(actual);
    }

    @Test
    void shouldNotFindByNullHostIdAndResId(){
        Reservation actual = resService.findByHostIdAndResId(null, -1);

        assertNull(actual);
    }

    @Test
    void shouldAdd() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 2));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertTrue(resService.add(reservation).isSuccess());
    }

    @Test
    void shouldNotAddPastReservation() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2020, 1, 1));
        reservation.setEndDate(LocalDate.of(2020, 1, 2));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.add(reservation).isSuccess());
    }

    @Test
    void shouldNotAddWithInvalidHost() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("bad host id");

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 2));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.add(reservation).isSuccess());
    }

    @Test
    void shouldNotAddWithNullHost() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = null;

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 2));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.add(reservation).isSuccess());
    }

    @Test
    void shouldNotAddWithInvalidGuest() throws DataException {
        Guest guest = new Guest();
        guest.setId(100);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 2));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.add(reservation).isSuccess());
    }

    @Test
    void shouldNotAddWithNullGuest() throws DataException {
        Guest guest = null;

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 2));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.add(reservation).isSuccess());
    }

    @Test
    void shouldNotAddWithStartDateAfterEndDate() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2022, 1, 5));
        reservation.setEndDate(LocalDate.of(2022, 1, 2));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.add(reservation).isSuccess());
    }

    @Test
    void shouldUpdate() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 10));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertTrue(resService.update(reservation).isSuccess());
    }

    @Test
    void shouldNotUpdateIfStartAfterEnd() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2022, 2, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 10));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.update(reservation).isSuccess());
    }

    @Test
    void shouldNotUpdateWithInvalidGuest() throws DataException {
        Guest guest = new Guest();
        guest.setId(-1);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 10));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.update(reservation).isSuccess());
    }

    @Test
    void shouldNotUpdateWithInvalidHost() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("invalid host id");

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 10));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.update(reservation).isSuccess());
    }

    @Test
    void shouldNotUpdateWithNullGuest() throws DataException {
        Guest guest = null;

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 10));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.update(reservation).isSuccess());
    }

    @Test
    void shouldNotUpdateWithNullHost() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = null;

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 10));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.update(reservation).isSuccess());
    }

    @Test
    void shouldDelete() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2022, 1, 1));
        reservation.setEndDate(LocalDate.of(2022, 1, 10));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertTrue(resService.delete(reservation).isSuccess());
    }

    @Test
    void shouldNotDeletePastReservation() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2020, 1, 1));
        reservation.setEndDate(LocalDate.of(2020, 1, 10));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.TEN);

        assertFalse(resService.delete(reservation).isSuccess());
    }
}