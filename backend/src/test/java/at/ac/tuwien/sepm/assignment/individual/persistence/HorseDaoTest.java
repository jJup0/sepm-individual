package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;

import java.time.LocalDate;
import java.util.List;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class HorseDaoTest {

    private static final int TEST_DATA_SIZE = 10;

    @Autowired
    HorseDao horseDao;

    @Test
    public void getAllReturnsAllStoredHorses() throws Exception{
        List<Horse> horses = horseDao.getAll();
        assertThat(horses.size()).isEqualTo(TEST_DATA_SIZE);
        assertThat(horses.get(TEST_DATA_SIZE-1).getId()).isEqualTo(-1);
        assertThat(horses.get(TEST_DATA_SIZE-1).getName()).isEqualTo("Wendy");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addHorseValid() throws Exception{
        HorseDto newHorseDto = new HorseDto(null, "test horse 1", "test description 1", LocalDate.now(), HorseBiologicalGender.male, null, null, null);
        Horse addedHorse = horseDao.addHorse(newHorseDto);
        assertThat(addedHorse.getId()).isGreaterThan(0);
    }



    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void editHorseValid() throws Exception {
        Horse wendy = horseDao.getHorse(-1);
        assertThat(wendy.getName()).isEqualTo("Wendy");
        HorseDto newWendyDto = new HorseDto(-1L, "not wendy", "test description 1", LocalDate.now(), HorseBiologicalGender.female, null, null, null);
        Horse newWendy = horseDao.editHorse(newWendyDto);
        assertThat(newWendy.getName()).isEqualTo("not wendy");
    }

    @Test
    public void editHorseNonExistent() throws Exception {
        HorseDto newWendyDto = new HorseDto(-101L, "not wendy", "test description 1", LocalDate.now(), HorseBiologicalGender.female, null, null, null);
        assertThatThrownBy(() -> {
            Horse newWendy = horseDao.editHorse(newWendyDto);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteHorse() {
        horseDao.deleteHorse(-1L);
        horseDao.deleteHorse(-1L);
        horseDao.deleteHorse(-102L);
    }

}

