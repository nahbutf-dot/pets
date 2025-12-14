package me.pets.storage;

import me.pets.pets.PetType;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {
    private final Set<PetType> ownedPets = new HashSet<>();
    private PetType equippedPet;

    public Set<PetType> getOwnedPets() {
        return ownedPets;
    }

    public PetType getEquippedPet() {
        return equippedPet;
    }

    public void setEquippedPet(PetType equippedPet) {
        this.equippedPet = equippedPet;
    }

    public boolean ownsPet(PetType type) {
        return ownedPets.contains(type);
    }

    public void addPet(PetType type) {
        ownedPets.add(type);
    }

    public void removePet(PetType type) {
        ownedPets.remove(type);
        if (equippedPet == type) {
            equippedPet = null;
        }
    }
}
