package me.pets.storage;

import me.pets.pets.PetType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StorageManager {
    private final Plugin plugin;
    private final File file;
    private FileConfiguration config;
    private final Map<UUID, PlayerData> cachedData = new HashMap<>();

    public StorageManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");
    }

    public void load() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create data file: " + e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data: " + e.getMessage());
        }
    }

    public PlayerData getData(UUID uuid) {
        PlayerData cached = cachedData.get(uuid);
        if (cached != null) {
            return cached;
        }

        PlayerData data = new PlayerData();
        String path = "players." + uuid + ".";
        config.getStringList(path + "owned").forEach(name -> {
            try {
                data.addPet(PetType.valueOf(name));
            } catch (IllegalArgumentException ignored) {
            }
        });
        String equipped = config.getString(path + "equipped");
        if (equipped != null) {
            try {
                data.setEquippedPet(PetType.valueOf(equipped));
            } catch (IllegalArgumentException ignored) {
                data.setEquippedPet(null);
            }
        }
        cachedData.put(uuid, data);
        return data;
    }

    public void saveData(UUID uuid) {
        PlayerData data = cachedData.get(uuid);
        if (data == null) return;
        String path = "players." + uuid + ".";
        config.set(path + "owned", data.getOwnedPets().stream().map(Enum::name).toList());
        config.set(path + "equipped", data.getEquippedPet() == null ? null : data.getEquippedPet().name());
        save();
    }
}
