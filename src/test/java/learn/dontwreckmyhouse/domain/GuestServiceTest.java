package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.GuestFileRepositoryDouble;
import learn.dontwreckmyhouse.models.Guest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {
    GuestService guestService = new GuestService(new GuestFileRepositoryDouble());

    @Test
    void shouldFindAll(){
        List<Guest> actual = guestService.findAll();
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Bob", actual.get(0).getFirstName());
    }

    @Test
    void shouldFindByState(){
        List<Guest> actual = guestService.findByState("WI");
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("WI", actual.get(0).getState());
    }

    @Test
    void shouldNotFindIfStateMissing(){
        List<Guest> actual = guestService.findByState("Alberta");
        assertNull(actual);
    }

    @Test
    void shouldNotFindIfStateNull(){
        List<Guest> actual = guestService.findByState(null);
        assertNull(actual);
    }

    @Test
    void shouldFindByEmail(){
        Guest actual = guestService.findByEmail("bob.bob@hotmail.com");
        assertNotNull(actual);
        assertEquals("bob.bob@hotmail.com", actual.getEmail());
    }

    @Test
    void shouldNotFindByMissingEmail(){
        Guest actual = guestService.findByEmail("princessdiana@wall.com");
        assertNull(actual);
    }

    @Test
    void shouldNotFindIfEmailNull(){
        Guest actual = guestService.findByEmail(null);
        assertNull(actual);
    }
}