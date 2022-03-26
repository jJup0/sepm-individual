package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDtoIdReferences;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;
import at.ac.tuwien.sepm.assignment.individual.exception.ConstraintViolation;
import at.ac.tuwien.sepm.assignment.individual.exception.MissingAttributeException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    public void tearDown() throws Exception {
        List<Horse> horses = horseService.allHorses();
        assertThat(horses.size()).isEqualTo(TEST_DATA_SIZE);
    }

    @Test
    public void getAllReturnsAllStoredHorses() throws Exception {
        List<Horse> horses = horseService.allHorses();
        Collections.sort(horses);
        System.out.println(horses);
        assertThat(horses.size()).isEqualTo(TEST_DATA_SIZE);
        assertThat(horses.get(TEST_DATA_SIZE - 1).getId()).isEqualTo(-1);
        assertThat(horses.get(TEST_DATA_SIZE - 1).getName()).isEqualTo("Wendy");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addValidHorses() throws Exception {

        HorseDtoIdReferences newHorseDto1 = new HorseDtoIdReferences(null, "test horse 1", null, LocalDate.now(), HorseBiologicalGender.female, null, null, null);
        Horse addedHorse1 = horseService.addHorse(newHorseDto1);
        long firstId = addedHorse1.getId();
        assertThat(addedHorse1.getId()).isGreaterThan(0);

        HorseDtoIdReferences newHorseDto2 = new HorseDtoIdReferences(null, "test horse 1", null, LocalDate.now(), HorseBiologicalGender.female, null, null, null);
        Horse addedHorse2 = horseService.addHorse(newHorseDto2);
        assertThat(addedHorse2.getId()).isEqualTo(firstId + 1);

        HorseDtoIdReferences newHorseDto3 = new HorseDtoIdReferences(100L, "test horse 1", null, LocalDate.now(), HorseBiologicalGender.male, null, null, null);
        Horse addedHorse3 = horseService.addHorse(newHorseDto3);
        assertThat(addedHorse3.getId()).isEqualTo(firstId + 2);

        // Clean up
        horseService.deleteHorse(addedHorse1.getId());
        horseService.deleteHorse(addedHorse2.getId());
        horseService.deleteHorse(addedHorse3.getId());
    }

    @Test
    public void addInvalidHorses() throws Exception {

        HorseDtoIdReferences[] missingAttributeHorses = {
                new HorseDtoIdReferences(null, null, null, LocalDate.now(), HorseBiologicalGender.male, null, null, null),
                new HorseDtoIdReferences(null, "1", null, null, HorseBiologicalGender.male, null, null, null),
                new HorseDtoIdReferences(null, "2", null, LocalDate.now(), null, null, null, null)};

        for (HorseDtoIdReferences incompleteHorse : missingAttributeHorses) {
            assertThatThrownBy(() -> {
                horseService.addHorse(incompleteHorse);
            }).isInstanceOf(MissingAttributeException.class)
                    .hasMessageEndingWith("can not be null");
        }

    }


    @Test
    public void editHorseValid() throws Exception {

        Horse oldWendy = horseService.getHorse(-1);

        HorseDtoIdReferences newWendyRequestDto = new HorseDtoIdReferences(-1L, "not wendy", null, oldWendy.getBirthdate().plusDays(1), HorseBiologicalGender.female, null, null, null);
        Horse newWendy = horseService.editHorse(newWendyRequestDto);

        assertThat(newWendy.getName()).isEqualTo("not wendy");
        assertThat(newWendy.getName()).isNotEqualTo(oldWendy.getName());

        assertThat(newWendy.getBirthdate()).isEqualTo(oldWendy.getBirthdate().plusDays(1));

        assertThat(newWendy.getOwner()).isNull();
        assertThat(newWendy.getOwner()).isNotEqualTo(oldWendy.getOwner());

        // Clean up
        horseService.editHorse(horseMapper.recursiveDtoToReferenceIdDto(horseMapper.entityToDto(oldWendy)));
        assertThat(horseService.getHorse(oldWendy.getId()).getName()).isEqualTo(oldWendy.getName());
    }

    @Test
    public void editHorseMissingAttributes() throws Exception {

        Horse dakota = horseService.getHorse(-7);
        assertThat(dakota.getName()).isEqualTo("Dakota");

        long dId = dakota.getId();
        String dName = dakota.getName();
        String dDesc = dakota.getDescription();
        LocalDate dBday = dakota.getBirthdate();
        HorseBiologicalGender dSex = dakota.getSex();
        Long dOwnerId = dakota.getOwner() == null ? null : dakota.getOwner().getId();
        Long dMotherId = dakota.getMother() == null ? null : dakota.getMother().getId();
        Long dFatherId = dakota.getFather() == null ? null : dakota.getFather().getId();

        HorseDtoIdReferences[] editRequestsIncomplete = {
                new HorseDtoIdReferences(null, dName, dDesc, dBday, dSex, dOwnerId, dMotherId, dFatherId),
                new HorseDtoIdReferences(dId, null, dDesc, dBday, dSex, dOwnerId, dMotherId, dFatherId),
                new HorseDtoIdReferences(dId, dName, dDesc, null, dSex, dOwnerId, dMotherId, dFatherId),
                new HorseDtoIdReferences(dId, dName, dDesc, dBday, null, dOwnerId, dMotherId, dFatherId)
        };

        for (HorseDtoIdReferences incompleteHorse : editRequestsIncomplete) {
            assertThatThrownBy(() -> {
                horseService.editHorse(incompleteHorse);
            }).isInstanceOf(MissingAttributeException.class)
                    .hasMessageStartingWith("failed to edit horse");
        }
    }

    @Test

    public void editHorseIllegalEdit() throws Exception {

        LocalDate parentBirthday = LocalDate.of(2000, 1, 1);

        Horse mother = horseService.addHorse(new HorseDtoIdReferences(null, "Mother", null, parentBirthday, HorseBiologicalGender.female, null, null, null));
        Horse father = horseService.addHorse(new HorseDtoIdReferences(null, "Father", null, parentBirthday, HorseBiologicalGender.male, null, null, null));
        Horse child = horseService.addHorse(new HorseDtoIdReferences(null, "Child", null, parentBirthday.plusYears(1), HorseBiologicalGender.male, null, mother.getId(), father.getId()));

        // For testing purposes entities will for a short time not actually be in the database
        // Parent tests:
        Horse[] parents = {mother, father};
        for (Horse parent : parents) {
            // Birthdate:
            parent.setBirthdate(parentBirthday.plusYears(2));
            assertThatThrownBy(() -> {
                horseService.editHorse(horseMapper.recursiveDtoToReferenceIdDto(horseMapper.entityToDto(parent)));
            }).isInstanceOf(ConstraintViolation.class).hasMessageEndingWith("new birthdate can not be later than birthdate of any child");
            parent.setBirthdate(parentBirthday.plusYears(-2));

            // Sex
            HorseBiologicalGender oldSex = parent == mother ? HorseBiologicalGender.female : HorseBiologicalGender.male;
            HorseBiologicalGender newSex = parent == mother ? HorseBiologicalGender.male : HorseBiologicalGender.female;
            parent.setSex(newSex);
            assertThatThrownBy(() -> {
                horseService.editHorse(horseMapper.recursiveDtoToReferenceIdDto(horseMapper.entityToDto(parent)));
            }).isInstanceOf(ConstraintViolation.class).hasMessageEndingWith("sex can not be changed if the horse has children");
            parent.setSex(oldSex);
        }

        // Child tests:
        // Birthdate
        child.setBirthdate(parentBirthday.plusYears(-2));
        assertThatThrownBy(() -> {
            horseService.editHorse(horseMapper.recursiveDtoToReferenceIdDto(horseMapper.entityToDto(child)));
        }).isInstanceOf(ConstraintViolation.class).hasMessageMatching(".*[mother|father] has to be older");
        child.setBirthdate(parentBirthday.plusYears(2));

        // Assign wrong parent:
        child.setMother(father);
        assertThatThrownBy(() -> {
            horseService.editHorse(horseMapper.recursiveDtoToReferenceIdDto(horseMapper.entityToDto(child)));
        }).isInstanceOf(ConstraintViolation.class).hasMessageEndingWith("mother has to be female");
        child.setMother(mother);
        child.setFather(mother);
        assertThatThrownBy(() -> {
            horseService.editHorse(horseMapper.recursiveDtoToReferenceIdDto(horseMapper.entityToDto(child)));
        }).isInstanceOf(ConstraintViolation.class).hasMessageEndingWith("father has to be male");
        child.setFather(father);

        horseService.deleteHorse(child.getId());
        horseService.deleteHorse(mother.getId());
        horseService.deleteHorse(father.getId());
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteHorse() throws Exception {
        Horse wendy = horseService.getHorse(-1);
        Horse wendyClone = horseService.addHorse(horseMapper.recursiveDtoToReferenceIdDto(horseMapper.entityToDto(wendy)));
        assertThat(horseService.allHorses().size()).isEqualTo(TEST_DATA_SIZE + 1);

        List<Horse> all = horseService.allHorses();
        Collections.sort(all);
        for (Horse h : all) {
            System.out.println(h);
        }

        horseService.deleteHorse(wendyClone.getId());
        assertThat(horseService.allHorses().size()).isEqualTo(TEST_DATA_SIZE);

        horseService.deleteHorse(wendyClone.getId());
        assertThat(horseService.allHorses().size()).isEqualTo(TEST_DATA_SIZE);

        all = horseService.allHorses();
        Collections.sort(all);
        for (Horse h : all) {
            System.out.println(h);
        }

        assertThatThrownBy(() -> {
            System.out.println(horseService.getHorse(wendyClone.getId()));
        }).isInstanceOf(NotFoundException.class);
    }

}
