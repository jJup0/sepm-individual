package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;

import java.time.LocalDate;
import java.util.List;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles({"test", "datagen"}) // enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class HorseDaoTest {

  @Autowired
  HorseDao horseDao;

  @Test
  public void getAllReturnsAllStoredHorses() {
    List<Horse> horses = horseDao.getAll();
    assertThat(horses.size()).isEqualTo(1);
    assertThat(horses.get(0).getId()).isEqualTo(-1);
    assertThat(horses.get(0).getName()).isEqualTo("Wendy");
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  public void addOneHorseValid() {

    HorseDto newHorseDto = new HorseDto(null, "test horse 1", "test description 1", LocalDate.now(), HorseBiologicalGender.male, "");
    Horse addedHorse = horseDao.addHorse(newHorseDto);
    assertThat(addedHorse.getId()).isNotNull();


  }

  @Test
  // TODO find better way
  public void addOneHorseInvalid() {

    HorseDto badHorses[] = {
            new HorseDto(null, null, "test description 1", LocalDate.now(), HorseBiologicalGender.male, ""),
            new HorseDto(null, null, "test description 1", null, HorseBiologicalGender.male, ""),
            new HorseDto(null, null, "test description 1", LocalDate.now(), null, "")};

    boolean failed;
    for (int i = 0; i < badHorses.length; i++) {
      failed = false;
      try {
        horseDao.addHorse(badHorses[i]);
      } catch (Exception e) {// TODO make specific exception
        failed = true;
      }
      assertThat(failed).isTrue();
    }

  }
}
