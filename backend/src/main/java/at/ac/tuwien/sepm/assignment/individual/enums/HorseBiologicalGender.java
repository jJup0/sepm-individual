package at.ac.tuwien.sepm.assignment.individual.enums;

public enum HorseBiologicalGender {
    male ("male"),
    female ("female");

    private final String sexString;

    HorseBiologicalGender(String s) {
        sexString = s;
    }

    public String toString() {
        return this.sexString;
    }
}
