package at.ac.tuwien.sepm.assignment.individual.entity;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;

import java.time.LocalDate;

public class Horse {
    private Long id;
    private String name;
    private String description;
    private LocalDate birthdate;
    private HorseBiologicalGender sex;
    private String owner;
    private Horse mother;
    private Horse father;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public HorseBiologicalGender getSex() {
        return sex;
    }

    public void setSex(HorseBiologicalGender sex) {
        this.sex = sex;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Horse getMother() {
        return mother;
    }

    public void setMother(Horse mother) {
        this.mother = mother;
    }

    public Horse getFather() {
        return father;
    }

    public void setFather(Horse father) {
        this.father = father;
    }
}
