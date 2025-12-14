package me.pets.core;

import me.pets.abilities.PetAbilityManager;
import me.pets.commands.GivePetRandomCommand;
import me.pets.commands.PetCommand;
import me.pets.commands.PetsCommand;
import me.pets.storage.StorageManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PetsPlugin extends JavaPlugin {
    private StorageManager storageManager;
    private PetManager petManager;
    private PetAbilityManager abilityManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        storageManager = new StorageManager(this);
        storageManager.load();
        petManager = new PetManager(this, storageManager);
        abilityManager = new PetAbilityManager(this);

        getServer().getPluginManager().registerEvents(abilityManager, this);
        getServer().getPluginManager().registerEvents(new PetListener(this), this);

        getCommand("pets").setExecutor(new PetsCommand(this));
        getCommand("pet").setExecutor(new PetCommand(this));
        getCommand("givepetrandom").setExecutor(new GivePetRandomCommand(this));
    }

    @Override
    public void onDisable() {
        storageManager.save();
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public PetManager getPetManager() {
        return petManager;
    }

    public PetAbilityManager getAbilityManager() {
        return abilityManager;
    }

    public String prefix() {
        return "§a[Pets] §r";
    }
}
