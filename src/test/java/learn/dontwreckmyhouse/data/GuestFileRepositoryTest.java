package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/guests_seed.csv";
    static final String TEST_FILE_PATH = "./data/guests_test.csv";
    static final int HOST_COUNT = 1000;

    GuestFileRepository repository = new GuestFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll(){
        List<Guest> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(HOST_COUNT, actual.size());
    }

    @Test
    void shouldFindByState(){
        List<Guest> actual = repository.findByState("WI");
        assertNotNull(actual);
        for(Guest g : actual){
            assertEquals("WI", g.getState());
        }
    }

    @Test
    void shouldNotFindInvalidState(){
        List<Guest> actual = repository.findByState("Alberta");
        assertEquals(0, actual.size());
    }

    @Test
    void shouldBeNullIfNullState(){
        List<Guest> actual = repository.findByState(null);
        assertNull(actual);
    }

    @Test
    void shouldFindById(){
        Guest actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals("Lomas", actual.getLastName());
    }

    @Test
    void shouldNotFindByFakeId(){
        Guest actual = repository.findById(-1);
        assertNull(actual);
    }

    @Test
    void shouldFindByFullName(){
        Guest actual = repository.findByFullName("Sullivan", "Lomas");
        assertNotNull(actual);
        assertEquals(1, actual.getId());
    }

    @Test
    void shouldNotFindByFakeName(){
        Guest actual = repository.findByFullName("#######!!!!!!!!!!!!!", "Tubman");
        assertNull(actual);
    }

    @Test
    void shouldBeNullIfNullName(){
        Guest actual = repository.findByFullName(null, null);
        assertNull(actual);
    }

    @Test
    void shouldFindByEmail(){
        Guest actual = repository.findByEmail("slomas0@mediafire.com");
        assertNotNull(actual);
        assertEquals(1, actual.getId());
    }

    @Test
    void shouldNotFindByFakeEmail(){
        Guest actual = repository.findByEmail("#######!!!!!!!!!!!!!");
        assertNull(actual);
    }

    @Test
    void shouldBeNullIfNullEmail(){
        Guest actual = repository.findByEmail(null);
        assertNull(actual);
    }
}