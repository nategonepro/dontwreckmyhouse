package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/reservations_seed.csv";
    static final String TEST_FILE_PATH = "./data/reservations-test/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_DIR_PATH = "./data/reservations-test";
    static final int RES_COUNT = 12;

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllReservations(){
        HashMap<String, List<Reservation>> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals(RES_COUNT, all.get("2e72f86c-b8fe-4265-b4f1-304dea8762db").size());
    }

    @Test
    void shouldFindByHostId(){
        List<Reservation> actual = repository.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(RES_COUNT, actual.size());
    }

    @Test
    void shouldNotFindInvalidHostId(){
        List<Reservation> actual = repository.findByHostId("12345");
        assertEquals(0, actual.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2020, 5, 1));
        reservation.setEndDate(LocalDate.of(2020, 5, 10));

        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);

        Host host = new Host();
        host.setId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        reservation.setHost(host);

        reservation.setTotal(new BigDecimal(100));

        reservation = repository.add(reservation);

        assertEquals(13, reservation.getId());
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2020, 5, 1));
        reservation.setEndDate(LocalDate.of(2020, 5, 10));

        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);

        Host host = new Host();
        host.setId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        reservation.setHost(host);
        reservation.setId(1);

        reservation.setTotal(new BigDecimal(100));

        assertTrue(repository.update(reservation));
    }

    @Test
    void shouldNotUpdateWithMissingHost() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2020, 5, 1));
        reservation.setEndDate(LocalDate.of(2020, 5, 10));

        Guest guest = new Guest();
        guest.setId(1);
        reservation.setGuest(guest);

        Host host = new Host();
        host.setId("234234afglakfgshsgh");
        reservation.setHost(host);
        reservation.setId(1);

        reservation.setTotal(new BigDecimal(100));

        assertFalse(repository.update(reservation));
    }
}