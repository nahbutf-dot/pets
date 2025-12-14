package me.pets.pets;

import org.bukkit.entity.EntityType;

public enum PetType {
    COW("Cow", Rarity.COMMON, EntityType.COW, true, 0.3, false),
    FISH("Fish", Rarity.COMMON, EntityType.COD, false, 1.0, true),
    PIG("Pig", Rarity.COMMON, EntityType.PIG, true, 1.0, false),
    SHEEP("Sheep", Rarity.COMMON, EntityType.SHEEP, true, 1.0, false),
    BEE("Bee", Rarity.UNCOMMON, EntityType.BEE, false, 1.0, true),
    RABBIT("Rabbit", Rarity.UNCOMMON, EntityType.RABBIT, true, 1.0, false),
    CAT("Cat", Rarity.UNCOMMON, EntityType.CAT, true, 1.0, false),
    SPIDER("Spider", Rarity.UNCOMMON, EntityType.SPIDER, false, 0.5, false),
    ZOMBIE("Zombie", Rarity.UNCOMMON, EntityType.ZOMBIE, true, 1.0, false),
    HORSE("Horse", Rarity.RARE, EntityType.HORSE, false, 0.3, false),
    DOLPHIN("Dolphin", Rarity.RARE, EntityType.DOLPHIN, true, 1.0, true),
    PUFFERFISH("Pufferfish", Rarity.RARE, EntityType.PUFFERFISH, false, 1.0, true),
    PARROT("Parrot", Rarity.RARE, EntityType.PARROT, false, 1.0, true),
    FOX("Fox", Rarity.RARE, EntityType.FOX, true, 1.0, false),
    IRON_GOLEM("Iron Golem", Rarity.RARE, EntityType.IRON_GOLEM, false, 0.2, false),
    ARMADILLO("Armadillo", Rarity.RARE, EntityType.ARMADILLO, true, 1.0, false),
    SKELETON("Skeleton", Rarity.RARE, EntityType.SKELETON, true, 1.0, false),
    CREEPER("Creeper", Rarity.EPIC, EntityType.CREEPER, false, 1.0, false),
    ARCTIC_FOX("Arctic Fox", Rarity.EPIC, EntityType.FOX, true, 1.0, false),
    WITHER_SKELETON("Wither Skeleton", Rarity.EPIC, EntityType.WITHER_SKELETON, false, 1.0, false),
    ENDERMAN("Enderman", Rarity.EPIC, EntityType.ENDERMAN, false, 1.0, false),
    BLAZE("Blaze", Rarity.EPIC, EntityType.BLAZE, false, 1.0, true),
    WITHER_BOSS("Wither Boss", Rarity.LEGENDARY, EntityType.WITHER, false, 1.0, true),
    SKELETON_HORSE("Skeleton Horse", Rarity.LEGENDARY, EntityType.SKELETON_HORSE, false, 1.0, false),
    WARDEN("Warden", Rarity.LEGENDARY, EntityType.WARDEN, false, 1.0, false),
    ENDER_DRAGON("Ender Dragon", Rarity.LEGENDARY, EntityType.ENDER_DRAGON, false, 1.0, true),
    POLAR_BEAR("Polar Bear", Rarity.LEGENDARY, EntityType.POLAR_BEAR, false, 1.0, false);

    private final String displayName;
    private final Rarity rarity;
    private final EntityType entityType;
    private final boolean baby;
    private final double scale;
    private final boolean floating;

    PetType(String displayName, Rarity rarity, EntityType entityType, boolean baby, double scale, boolean floating) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.entityType = entityType;
        this.baby = baby;
        this.scale = scale;
        this.floating = floating;
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

    public boolean isFloating() {
        return floating;
    }
}
