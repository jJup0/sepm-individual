package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;

import java.time.LocalDate;
import java.util.List;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.MissingAttributeException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
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
public class HorseServiceTest {

    private static final int TEST_DATA_SIZE = 10;

    @Autowired
    HorseService horseService;
    @Autowired
    HorseMapper horseMapper;
    @Autowired
    OwnerMapper ownerMapper;

    @Test
    public void getAllReturnsAllStoredHorses() throws Exception{
        List<Horse> horses = horseService.allHorses();
        assertThat(horses.size()).isEqualTo(TEST_DATA_SIZE);
        assertThat(horses.get(TEST_DATA_SIZE - 1).getId()).isEqualTo(-1);
        assertThat(horses.get(TEST_DATA_SIZE - 1).getName()).isEqualTo("Wendy");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addValidHorses() throws Exception{

        HorseDto newHorseDto1 = new HorseDto(null, "test horse 1", null, LocalDate.now(), HorseBiologicalGender.female, null, null, null);
        Horse addedHorse1 = horseService.addHorse(newHorseDto1);
        long firstId = addedHorse1.getId();
        assertThat(addedHorse1.getId()).isGreaterThan(0);

        HorseDto newHorseDto2 = new HorseDto(null, "test horse 1", null, LocalDate.now(), HorseBiologicalGender.female, null, null, null);
        Horse addedHorse2 = horseService.addHorse(newHorseDto2);
        assertThat(addedHorse2.getId()).isEqualTo(firstId + 1);

        HorseDto newHorseDto3 = new HorseDto(100L, "test horse 1", null, LocalDate.now(), HorseBiologicalGender.male, null, null, null);
        Horse addedHorse3 = horseService.addHorse(newHorseDto3);
        assertThat(addedHorse3.getId()).isEqualTo(firstId + 2);
    }

    @Test
    public void addInvalidHorses() throws Exception {

        HorseDto[] missingAttributeHorses = {
                new HorseDto(null, null, null, LocalDate.now(), HorseBiologicalGender.male, null, null, null),
                new HorseDto(null, "1", null, null, HorseBiologicalGender.male, null, null, null),
                new HorseDto(null, "2", null, LocalDate.now(), null, null, null, null)};

        for (HorseDto incompleteHorse : missingAttributeHorses) {
            assertThatThrownBy(() -> {
                horseService.addHorse(incompleteHorse);
            }).isInstanceOf(MissingAttributeException.class)
                    .hasMessageMatching("failed to add new horse: \\w+ missing");
        }
    }

    // Get horse untested

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void editHorseValid() throws Exception{


        Horse oldWendy = horseService.getHorse(-1);

        LocalDate now = LocalDate.now();
        HorseDto newWendyRequestDto = new HorseDto(-1L, "not wendy", null, now, HorseBiologicalGender.female, null, null, null);
        Horse newWendy = horseService.editHorse(newWendyRequestDto);

        assertThat(newWendy.getName()).isEqualTo("not wendy");
        assertThat(newWendy.getName()).isNotEqualTo(oldWendy.getName());

        assertThat(newWendy.getBirthdate()).isEqualTo(now);
        assertThat(newWendy.getBirthdate()).isNotEqualTo(oldWendy.getBirthdate());

        assertThat(newWendy.getOwner()).isNull();
        assertThat(newWendy.getOwner()).isNotEqualTo(oldWendy.getOwner());
    }
    @Test
    public void editHorseMissingAttributes() throws Exception{

        Horse dakota = horseService.getHorse(-7);
        assertThat(dakota.getName()).isEqualTo("Dakota");

        long dId = dakota.getId();
        String dName = dakota.getName();
        String dDesc = dakota.getDescription();
        LocalDate dBday = dakota.getBirthdate();
        HorseBiologicalGender dSex = dakota.getSex();
        OwnerDto dOwner = ownerMapper.entityToDto(dakota.getOwner());
        HorseDto dMother = horseMapper.entityToDto(dakota.getMother());
        HorseDto dFather = horseMapper.entityToDto(dakota.getFather());

        HorseDto[] editRequestsIncomplete = {
                new HorseDto(null, dName, dDesc, dBday, dSex, dOwner, dMother, dFather),
                new HorseDto(dId, null, dDesc, dBday, dSex, dOwner, dMother, dFather),
                new HorseDto(dId, dName, dDesc, null, dSex, dOwner, dMother, dFather),
                new HorseDto(dId, dName, dDesc, dBday, null, dOwner, dMother, dFather)
        };

        for (HorseDto incompleteHorse : editRequestsIncomplete) {
            assertThatThrownBy(() -> {
                horseService.editHorse(incompleteHorse);
            }).isInstanceOf(MissingAttributeException.class)
                    .hasMessageStartingWith("failed to edit horse");
        }

    }

    // TODO illegal edits test

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteHorse() {
        horseService.deleteHorse(-1L);
        horseService.deleteHorse(-1L);
        assertThatThrownBy(() -> {
            horseService.getHorse(-1);
        }).isInstanceOf(NotFoundException.class);
    }

    // get family tree tested in rest layer
}
