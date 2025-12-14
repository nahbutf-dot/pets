package me.pets.pets;

public enum Rarity {
    COMMON(0.50),
    UNCOMMON(0.25),
    RARE(0.15),
    EPIC(0.08),
    LEGENDARY(0.02);

    private final double weight;

    Rarity(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
