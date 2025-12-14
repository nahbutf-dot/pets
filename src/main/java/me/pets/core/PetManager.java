package me.pets.core;

import me.pets.pets.PetFollower;
import me.pets.pets.PetType;
import me.pets.storage.PlayerData;
import me.pets.storage.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PetManager {
    private final PetsPlugin plugin;
    private final StorageManager storage;
    private final Map<UUID, PetFollower> followers = new HashMap<>();

    public PetManager(PetsPlugin plugin, StorageManager storage) {
        this.plugin = plugin;
        this.storage = storage;

        new BukkitRunnable() {
            @Override
            public void run() {
                followers.values().forEach(follower -> {
                    Player player = plugin.getServer().getPlayer(follower.getOwner());
                    if (player != null && player.isOnline()) {
                        follower.tick(player);
                    }
                });
            }
        }.runTaskTimer(plugin, 20L, 10L);
    }

    public PlayerData getData(Player player) {
        return storage.getData(player.getUniqueId());
    }

    public void equipPet(Player player, PetType type) {
        PlayerData data = getData(player);
        if (!data.ownsPet(type)) {
            player.sendMessage(plugin.prefix() + "You do not own this pet.");
            return;
        }
        if (followers.containsKey(player.getUniqueId())) {
            followers.get(player.getUniqueId()).despawn();
        }
        data.setEquippedPet(type);
        PetFollower follower = new PetFollower(player, type);
        followers.put(player.getUniqueId(), follower);
        storage.saveData(player.getUniqueId());
        plugin.getAbilityManager().applyPassives(player);
        player.sendMessage(plugin.prefix() + "Equipped " + type.getDisplayName() + " pet.");
    }

    public void unequip(Player player) {
        PlayerData data = getData(player);
        data.setEquippedPet(null);
        plugin.getAbilityManager().resetPassives(player);
        PetFollower follower = followers.remove(player.getUniqueId());
        if (follower != null) {
            follower.despawn();
        }
        storage.saveData(player.getUniqueId());
    }

    public PetType getEquipped(Player player) {
        PlayerData data = getData(player);
        return data.getEquippedPet();
    }

    public void addPet(Player player, PetType type) {
        PlayerData data = getData(player);
        data.addPet(type);
        storage.saveData(player.getUniqueId());
    }

    public void removePet(Player player, PetType type) {
        PlayerData data = getData(player);
        data.removePet(type);
        storage.saveData(player.getUniqueId());
    }

    public void handleQuit(UUID uuid) {
        PetFollower follower = followers.remove(uuid);
        if (follower != null) {
            follower.despawn();
        }
        storage.saveData(uuid);
    }
}
