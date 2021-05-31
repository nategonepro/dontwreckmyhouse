package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/hosts_seed.csv";
    static final String TEST_FILE_PATH = "./data/hosts_test.csv";
    static final int HOST_COUNT = 1000;

    HostFileRepository repository = new HostFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setup() throws IOException{
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllHosts(){
        List<Host> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(HOST_COUNT, actual.size());
    }

    @Test
    void shouldFindByState(){
        List<Host> actual = repository.findByState("WI");
        assertNotNull(actual);
        for(Host h : actual){
            assertEquals("WI", h.getState());
        }
    }

    @Test
    void shouldNotFindInvalidState(){
        List<Host> actual = repository.findByState("Alberta");
        assertEquals(0, actual.size());
    }

    @Test
    void shouldBeNullIfNullState(){
        List<Host> actual = repository.findByState(null);
        assertNull(actual);
    }

    @Test
    void shouldFindByCity(){
        List<Host> actual = repository.findByCity("Milwaukee");
        assertNotNull(actual);
        for(Host h : actual){
            assertEquals("Milwaukee", h.getCity());
        }
    }

    @Test
    void shouldNotFindByFakeCity(){
        List<Host> actual = repository.findByCity("#######!!!!!!!!!!!!!");
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void shouldBeNullIfNullCity(){
        List<Host> actual = repository.findByCity(null);
        assertNull(actual);
    }

    @Test
    void shouldFindByLastName(){
        List<Host> actual = repository.findByLastName("Johnson");
        assertNotNull(actual);
        for(Host h : actual){
            assertEquals("Johnson", h.getLastName());
        }
    }

    @Test
    void shouldNotFindByFakeLastName(){
        List<Host> actual = repository.findByLastName("#######!!!!!!!!!!!!!");
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void shouldBeNullIfNullLastName(){
        List<Host> actual = repository.findByLastName(null);
        assertNull(actual);
    }

    @Test
    void shouldFindById(){
        Host actual = repository.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertNotNull(actual);
        assertEquals("Yearnes", actual.getLastName());
    }

    @Test
    void shouldNotFindByFakeId(){
        Host actual = repository.findById("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        assertNull(actual);
    }

    @Test
    void shouldBeNullIfNullId(){
        Host actual = repository.findById(null);
        assertNull(actual);
    }

    @Test
    void shouldFindByEmail(){
        Host actual = repository.findByEmail("eyearnes0@sfgate.com");
        assertNotNull(actual);
        assertEquals("Yearnes", actual.getLastName());
    }

    @Test
    void shouldNotFindByFakeEmail(){
        Host actual = repository.findByEmail("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        assertNull(actual);
    }

    @Test
    void shouldBeNullIfNullEmail(){
        Host actual = repository.findByEmail(null);
        assertNull(actual);
    }
}