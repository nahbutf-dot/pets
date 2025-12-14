package me.pets.pets;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PetFollower {
    private final UUID owner;
    private final PetType type;
    private Entity entity;

    public PetFollower(Player player, PetType type) {
        this.owner = player.getUniqueId();
        this.type = type;
        spawn(player);
    }

    private void spawn(Player player) {
        Location loc = player.getLocation().subtract(player.getLocation().getDirection().normalize()).add(0.5, 0, 0.5);
        entity = player.getWorld().spawnEntity(loc, type.getEntityType());
        entity.setInvulnerable(true);
        entity.setPersistent(false);
        entity.setSilent(true);
        entity.setGravity(!type.isFloating());
        if (entity instanceof LivingEntity living) {
            living.setRemoveWhenFarAway(false);
            living.setAI(false);
            living.setCollidable(false);
            living.setCanPickupItems(false);
            if (living.getAttribute(Attribute.GENERIC_SCALE) != null) {
                living.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(type.getScale());
            }
        }
        if (entity instanceof Ageable ageable && type.isBaby()) {
            ageable.setBaby();
        }
    }

    public void tick(Player player) {
        if (entity == null || entity.isDead()) return;
        Vector look = player.getLocation().getDirection().clone().setY(0).normalize();
        if (look.lengthSquared() == 0) {
            look = new Vector(0, 0, 1);
        }
        Vector right = look.clone().crossProduct(new Vector(0, 1, 0)).normalize().multiply(0.4);
        Location target = player.getLocation().clone().add(look.clone().multiply(-1)).add(right).add(0, type.isBaby() ? 0.2 : 0, 0);

        Vector offset = target.toVector().subtract(entity.getLocation().toVector());
        if (offset.lengthSquared() > 9) {
            entity.teleport(target);
            return;
        }
        entity.setVelocity(offset.multiply(0.3));
    }

    public void despawn() {
        if (entity != null) {
            entity.remove();
        }
    }

    public UUID getOwner() {
        return owner;
    }

    public PetType getType() {
        return type;
    }
}
