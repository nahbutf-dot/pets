package me.pets.abilities;

import me.pets.core.PetsPlugin;
import me.pets.pets.PetType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PetAbilityManager implements Listener {
    private final PetsPlugin plugin;
    private final Map<UUID, Map<Integer, Long>> cooldowns = new HashMap<>();
    private final Map<UUID, String> queuedHit = new HashMap<>();
    private final Set<PotionEffectType> harmfulEffects = new HashSet<>();

    public PetAbilityManager(PetsPlugin plugin) {
        this.plugin = plugin;
        harmfulEffects.addAll(Set.of(
                PotionEffectType.BLINDNESS,
                PotionEffectType.UNLUCK,
                PotionEffectType.WEAKNESS,
                PotionEffectType.SLOWNESS,
                PotionEffectType.MINING_FATIGUE,
                PotionEffectType.POISON,
                PotionEffectType.WITHER,
                PotionEffectType.HUNGER,
                PotionEffectType.BAD_OMEN,
                PotionEffectType.DARKNESS
        ));
    }

    public void applyPassives(Player player) {
        PetType type = plugin.getPetManager().getEquipped(player);
        if (type == null) return;

        switch (type) {
            case COW -> applyCowPassive(player);
            case PIG -> modifyHealth(player, 4.0);
            case SHEEP -> player.setFallDistance(-15f);
            case BEE -> player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 20 * 99999, 0, true, false, false));
            case RABBIT -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 20 * 99999, 0, true, false, false));
            case CAT -> {
                // handled in damage event
            }
            case SPIDER -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 99999, 0, true, false, false));
            case ZOMBIE -> {
            }
            case HORSE -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 99999, 1, true, false, false));
            case DOLPHIN -> player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 20 * 99999, 0, true, false, false));
            case PUFFERFISH -> {
            }
            case PARROT -> {
            }
            case FOX -> {
            }
            case IRON_GOLEM -> modifyHealth(player, 10.0);
            case ARMADILLO -> modifyHealth(player, 6.0);
            case SKELETON -> {
            }
            case CREEPER -> {
            }
            case ARCTIC_FOX -> {
            }
            case WITHER_SKELETON -> modifyHealth(player, 4.0);
            case ENDERMAN -> modifyHealth(player, 8.0);
            case BLAZE -> player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 99999, 0, true, false, false));
            case WITHER_BOSS -> {
            }
            case SKELETON_HORSE -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 99999, 1, true, false, false));
            case WARDEN -> modifyHealth(player, 8.0);
            case ENDER_DRAGON -> modifyHealth(player, 12.0);
            case POLAR_BEAR -> modifyHealth(player, 8.0);
            case FISH -> {
            }
        }
    }

    public void resetPassives(Player player) {
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attr != null) {
            attr.setBaseValue(20.0);
            if (player.getHealth() > attr.getBaseValue()) {
                player.setHealth(attr.getBaseValue());
            }
        }
        player.removePotionEffect(PotionEffectType.HASTE);
        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.RESISTANCE);
        player.removePotionEffect(PotionEffectType.STRENGTH);
        player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.SATURATION);
    }

    private void applyCowPassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 5, 0, true, false, false));
    }

    private void modifyHealth(Player player, double extra) {
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attr != null) {
            attr.setBaseValue(20.0 + extra);
            if (player.getHealth() > attr.getBaseValue()) {
                player.setHealth(attr.getBaseValue());
            }
        }
    }

    public boolean useAbility(Player player, int slot) {
        PetType type = plugin.getPetManager().getEquipped(player);
        if (type == null) {
            player.sendMessage(plugin.prefix() + "No pet equipped.");
            return false;
        }
        long now = System.currentTimeMillis();
        Map<Integer, Long> playerCooldowns = cooldowns.computeIfAbsent(player.getUniqueId(), id -> new HashMap<>());
        long ready = playerCooldowns.getOrDefault(slot, 0L);
        if (now < ready) {
            long seconds = (ready - now) / 1000L;
            player.sendMessage(plugin.prefix() + "Ability on cooldown for " + seconds + "s.");
            return false;
        }

        switch (type) {
            case COW -> {
                if (slot == 1) {
                    player.getActivePotionEffects().stream()
                            .filter(effect -> harmfulEffects.contains(effect.getType()))
                            .map(PotionEffect::getType)
                            .forEach(player::removePotionEffect);
                    playerCooldowns.put(slot, now + 60_000);
                    player.sendMessage(plugin.prefix() + "Cleansed negative effects!");
                    return true;
                }
            }
            case PIG -> {
                if (slot == 1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 0, false, true, true));
                    playerCooldowns.put(slot, now + 20_000);
                    return true;
                }
            }
            case SHEEP -> {
                if (slot == 1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 5, 1, false, true, true));
                    playerCooldowns.put(slot, now + 20_000);
                    return true;
                }
            }
            case BEE -> {
                if (slot == 1) {
                    queuedHit.put(player.getUniqueId(), "poison");
                    player.sendMessage(plugin.prefix() + "Next hit will poison your target.");
                    playerCooldowns.put(slot, now + 60_000);
                    return true;
                }
            }
            case RABBIT -> {
                if (slot == 1) {
                    Vector dash = player.getLocation().getDirection().normalize().multiply(1.5);
                    dash.setY(0.1);
                    player.setVelocity(dash);
                    playerCooldowns.put(slot, now + 20_000);
                    return true;
                }
            }
            case SPIDER -> {
                if (slot == 1) {
                    queuedHit.put(player.getUniqueId(), "cobweb");
                    playerCooldowns.put(slot, now + 90_000);
                    return true;
                }
            }
            case ZOMBIE -> {
                if (slot == 1) {
                    queuedHit.put(player.getUniqueId(), "hunger");
                    playerCooldowns.put(slot, now + 20_000);
                    return true;
                }
                if (slot == 2) {
                    player.getWorld().spawnEntity(player.getLocation(), PetType.ZOMBIE.getEntityType());
                    playerCooldowns.put(slot, now + 20_000);
                    return true;
                }
            }
            case HORSE -> {
                if (slot == 1) {
                    Vector dash = player.getLocation().getDirection().normalize().multiply(2.5);
                    player.setVelocity(dash);
                    playerCooldowns.put(slot, now + 60_000);
                    return true;
                }
            }
            case DOLPHIN -> {
                if (slot == 1) {
                    int amplifier = player.isInWaterOrRain() ? 1 : 0;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, amplifier, false, true, true));
                    playerCooldowns.put(slot, now + 90_000);
                    return true;
                }
            }
            case PUFFERFISH -> {
                if (slot == 1) {
                    boolean water = player.isInWaterOrRain();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, water ? 1 : 0));
                    playerCooldowns.put(slot, now + 90_000);
                    return true;
                }
            }
            case IRON_GOLEM -> {
                if (slot == 1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 7, 2, false, true, true));
                    playerCooldowns.put(slot, now + 90_000);
                    return true;
                }
            }
            case ARMADILLO -> {
                if (slot == 1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 5, 9, false, true, true));
                    playerCooldowns.put(slot, now + 90_000);
                    return true;
                }
            }
            case SKELETON -> {
                if (slot == 1) {
                    for (int i = 0; i < 5; i++) {
                        Projectile proj = player.launchProjectile(Snowball.class);
                        proj.setVelocity(player.getLocation().getDirection().clone().add(new Vector(Math.random() * 0.1, Math.random() * 0.1, Math.random() * 0.1)));
                    }
                    playerCooldowns.put(slot, now + 120_000);
                    return true;
                }
            }
            case CREEPER -> {
                if (slot == 1) {
                    player.getWorld().createExplosion(player.getLocation(), 4f, false, false, player);
                    playerCooldowns.put(slot, now + 120_000);
                    return true;
                }
            }
            case WITHER_SKELETON -> {
                if (slot == 1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, 1, false, true, true));
                    playerCooldowns.put(slot, now + 150_000);
                    return true;
                }
            }
            case ENDERMAN -> {
                if (slot == 1) {
                    Vector dir = player.getLocation().getDirection().normalize().multiply(30);
                    player.teleport(player.getLocation().add(dir));
                    playerCooldowns.put(slot, now + 45_000);
                    return true;
                }
            }
            case BLAZE -> {
                if (slot == 1) {
                    for (int i = 0; i < 3; i++) {
                        Projectile blazeBall = player.launchProjectile(Snowball.class);
                        blazeBall.setFireTicks(200);
                    }
                    playerCooldowns.put(slot, now + 120_000);
                    return true;
                }
            }
            case WITHER_BOSS -> {
                if (slot == 1) {
                    player.getWorld().spawnEntity(player.getLocation(), PetType.WITHER_BOSS.getEntityType());
                    playerCooldowns.put(slot, now + 180_000);
                    return true;
                }
            }
            case SKELETON_HORSE -> {
                if (slot == 1) {
                    queuedHit.put(player.getUniqueId(), "lightning");
                    playerCooldowns.put(slot, now + 120_000);
                    return true;
                }
            }
            case WARDEN -> {
                if (slot == 1) {
                    player.getNearbyEntities(15, 15, 15).stream()
                            .filter(entity -> entity instanceof LivingEntity && !entity.getUniqueId().equals(player.getUniqueId()))
                            .map(entity -> (LivingEntity) entity)
                            .findFirst()
                            .ifPresent(target -> target.damage(10.0, player));
                    playerCooldowns.put(slot, now + 60_000);
                    return true;
                }
                if (slot == 2) {
                    player.getWorld().spawnEntity(player.getLocation(), PetType.WARDEN.getEntityType());
                    playerCooldowns.put(slot, now + 180_000);
                    return true;
                }
            }
            case ENDER_DRAGON -> {
                if (slot == 1) {
                    Projectile proj = player.launchProjectile(Snowball.class);
                    proj.setVelocity(player.getLocation().getDirection().multiply(1.2));
                    playerCooldowns.put(slot, now + 90_000);
                    return true;
                }
                if (slot == 2) {
                    player.getWorld().spawnEntity(player.getLocation(), PetType.ENDER_DRAGON.getEntityType());
                    playerCooldowns.put(slot, now + 180_000);
                    return true;
                }
            }
            case POLAR_BEAR -> {
                if (slot == 1) {
                    queuedHit.put(player.getUniqueId(), "freeze");
                    playerCooldowns.put(slot, now + 60_000);
                    return true;
                }
                if (slot == 2) {
                    player.getWorld().spawnEntity(player.getLocation(), PetType.POLAR_BEAR.getEntityType());
                    playerCooldowns.put(slot, now + 180_000);
                    return true;
                }
            }
            case ARCTIC_FOX, FOX, PARROT, FISH -> {
                player.sendMessage(plugin.prefix() + "This pet has no active ability implemented yet.");
                return false;
            }
        }
        player.sendMessage(plugin.prefix() + "No ability on this slot.");
        return false;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PetType type = plugin.getPetManager().getEquipped(player);
        if (type == PetType.COW) {
            Material block = player.getLocation().getBlock().getType();
            if (block == Material.GRASS_BLOCK) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 40, 0, false, false, false));
            }
        }
        if (type == PetType.FISH || type == PetType.DOLPHIN || type == PetType.PUFFERFISH) {
            if (player.isInWaterOrRain()) {
                int duration = switch (type) {
                    case FISH -> 20 * 5;
                    case DOLPHIN -> 20 * 30;
                    case PUFFERFISH -> 20 * 45;
                    default -> 0;
                };
                if (duration > 0) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, duration, 0, false, false, false));
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        PetType type = plugin.getPetManager().getEquipped(player);
        if (type == null) return;

        if (type == PetType.CAT) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                event.setCancelled(true);
            }
        }
        if (type == PetType.SHEEP && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (event.getDamage() <= 15) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        PetType type = plugin.getPetManager().getEquipped(player);
        if (type == null) return;

        String queued = queuedHit.remove(player.getUniqueId());
        if (queued == null) return;

        if (event.getEntity() instanceof LivingEntity target) {
            switch (queued) {
                case "poison" -> target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 0));
                case "hunger" -> target.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20 * 5, 0));
                case "cobweb" -> target.getLocation().getBlock().setType(Material.COBWEB);
                case "lightning" -> target.getWorld().strikeLightning(target.getLocation());
                case "freeze" -> target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 5, 4));
            }
        }
    }
}
