package at.ac.tuwien.sepm.assignment.individual.entity;

import at.ac.tuwien.sepm.assignment.individual.enums.HorseBiologicalGender;

import java.time.LocalDate;

/**
 * Class for Horse Entities with owner, mother and father as entities
 * Contains all common properties
 */
public class Horse implements Comparable<Horse>{
    private Long id;
    private String name;
    private String description;
    private LocalDate birthdate;
    private HorseBiologicalGender sex;
    private Owner owner;
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
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

    @Override
    public int compareTo(Horse o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "Horse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", birthdate=" + birthdate +
                ", sex=" + sex +
                ", owner=" + owner +
                ", mother=" + mother +
                ", father=" + father +
                '}';
    }
}