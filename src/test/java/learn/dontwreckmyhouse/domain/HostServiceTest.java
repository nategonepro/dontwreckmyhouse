package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.HostFileRepositoryDouble;
import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {
    HostService hostService = new HostService(new HostFileRepositoryDouble());

    @Test
    void shouldFindAll(){
        List<Host> actual = hostService.findAll();
        assertNotNull(actual);
        assertEquals("Epstein", actual.get(0).getLastName());
    }

    @Test
    void shouldFindByState(){
        List<Host> actual = hostService.findByState("FL");
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("FL", actual.get(0).getState());
    }

    @Test
    void shouldNotFindByMissingState(){
        List<Host> actual = hostService.findByState("Albequerque");
        assertNull(actual);
    }

    @Test
    void shouldNotFindByNullState(){
        List<Host> actual = hostService.findByState(null);
        assertNull(actual);
    }

    @Test
    void shouldFindByEmail(){
        Host actual = hostService.findByEmail("captain@lolexpress.com");
        assertNotNull(actual);
        assertEquals("Epstein", actual.getLastName());
    }

    @Test
    void shouldNotFindByMissingEmail(){
        Host actual = hostService.findByEmail("cellguard@nyc.gov");
        assertNull(actual);
    }

    @Test
    void shouldNotFindByNullEmail(){
        Host actual = hostService.findByEmail(null);
        assertNull(actual);
    }
}