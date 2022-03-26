package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDtoIdReferences;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
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
    @Autowired
    HorseMapper mapper;


    @Test
    public void getAllReturnsAllStoredHorses() throws Exception {
        List<Horse> horses = horseDao.getAll();
        Collections.sort(horses);
        assertThat(horses.size()).isEqualTo(TEST_DATA_SIZE);
        assertThat(horses.get(TEST_DATA_SIZE - 1).getId()).isEqualTo(-1);
        assertThat(horses.get(TEST_DATA_SIZE - 1).getName()).isEqualTo("Wendy");
    }

    @Test
    public void addHorseValid() throws Exception {
        HorseDtoIdReferences newHorseDto = new HorseDtoIdReferences(null, "test horse 1", "test description 1", LocalDate.now(), HorseBiologicalGender.male, null, null, null);
        Horse addedHorse = horseDao.addHorse(newHorseDto);
        assertThat(addedHorse.getId()).isGreaterThan(0);
        assertThat(horseDao.getAll().size()).isEqualTo(TEST_DATA_SIZE + 1);

        // Clean up
        horseDao.deleteHorse(addedHorse.getId());
    }


    @Test
    public void editHorseValid() throws Exception {
        Horse wendy = horseDao.getHorse(-1);
        assertThat(wendy.getName()).isEqualTo("Wendy");
        HorseDtoIdReferences newWendyDto = new HorseDtoIdReferences(-1L, "not wendy", "test description 1", LocalDate.now(), HorseBiologicalGender.female, null, null, null);
        Horse newWendy = horseDao.editHorse(newWendyDto);
        assertThat(newWendy.getName()).isEqualTo("not wendy");

        // Clean up
        horseDao.editHorse(mapper.recursiveDtoToReferenceIdDto(mapper.entityToDto(wendy)));
    }

    @Test
    public void editHorseNonExistent() throws Exception {
        HorseDtoIdReferences newWendyDto = new HorseDtoIdReferences(-101L, "not wendy", "test description 1", LocalDate.now(), HorseBiologicalGender.female, null, null, null);
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

