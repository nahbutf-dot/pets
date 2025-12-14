package me.pets.pets;

import org.bukkit.entity.EntityType;

public enum PetType {
    COW("Cow", Rarity.COMMON, EntityType.COW, true, 0.3),
    FISH("Fish", Rarity.COMMON, EntityType.COD, false, 1.0),
    PIG("Pig", Rarity.COMMON, EntityType.PIG, true, 1.0),
    SHEEP("Sheep", Rarity.COMMON, EntityType.SHEEP, true, 1.0),
    BEE("Bee", Rarity.UNCOMMON, EntityType.BEE, false, 1.0),
    RABBIT("Rabbit", Rarity.UNCOMMON, EntityType.RABBIT, true, 1.0),
    CAT("Cat", Rarity.UNCOMMON, EntityType.CAT, true, 1.0),
    SPIDER("Spider", Rarity.UNCOMMON, EntityType.SPIDER, false, 0.5),
    ZOMBIE("Zombie", Rarity.UNCOMMON, EntityType.ZOMBIE, true, 1.0),
    HORSE("Horse", Rarity.RARE, EntityType.HORSE, false, 0.3),
    DOLPHIN("Dolphin", Rarity.RARE, EntityType.DOLPHIN, true, 1.0),
    PUFFERFISH("Pufferfish", Rarity.RARE, EntityType.PUFFERFISH, false, 1.0),
    PARROT("Parrot", Rarity.RARE, EntityType.PARROT, false, 1.0),
    FOX("Fox", Rarity.RARE, EntityType.FOX, true, 1.0),
    IRON_GOLEM("Iron Golem", Rarity.RARE, EntityType.IRON_GOLEM, false, 0.2),
    ARMADILLO("Armadillo", Rarity.RARE, EntityType.ARMADILLO, true, 1.0),
    SKELETON("Skeleton", Rarity.RARE, EntityType.SKELETON, true, 1.0),
    CREEPER("Creeper", Rarity.EPIC, EntityType.CREEPER, false, 1.0),
    ARCTIC_FOX("Arctic Fox", Rarity.EPIC, EntityType.FOX, true, 1.0),
    WITHER_SKELETON("Wither Skeleton", Rarity.EPIC, EntityType.WITHER_SKELETON, false, 1.0),
    ENDERMAN("Enderman", Rarity.EPIC, EntityType.ENDERMAN, false, 1.0),
    BLAZE("Blaze", Rarity.EPIC, EntityType.BLAZE, false, 1.0),
    WITHER_BOSS("Wither Boss", Rarity.LEGENDARY, EntityType.WITHER, false, 1.0),
    SKELETON_HORSE("Skeleton Horse", Rarity.LEGENDARY, EntityType.SKELETON_HORSE, false, 1.0),
    WARDEN("Warden", Rarity.LEGENDARY, EntityType.WARDEN, false, 1.0),
    ENDER_DRAGON("Ender Dragon", Rarity.LEGENDARY, EntityType.ENDER_DRAGON, false, 1.0),
    POLAR_BEAR("Polar Bear", Rarity.LEGENDARY, EntityType.POLAR_BEAR, false, 1.0);

    private final String displayName;
    private final Rarity rarity;
    private final EntityType entityType;
    private final boolean baby;
    private final double scale;

    PetType(String displayName, Rarity rarity, EntityType entityType, boolean baby, double scale) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.entityType = entityType;
        this.baby = baby;
        this.scale = scale;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public boolean isBaby() {
        return baby;
    }

    public double getScale() {
        return scale;
    }
}
