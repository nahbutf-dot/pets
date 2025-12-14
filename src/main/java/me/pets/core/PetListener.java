package me.pets.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PetListener implements Listener {
    private final PetsPlugin plugin;

    public PetListener(PetsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var data = plugin.getPetManager().getData(event.getPlayer());
        if (data.getEquippedPet() != null) {
            plugin.getPetManager().equipPet(event.getPlayer(), data.getEquippedPet());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPetManager().handleQuit(event.getPlayer().getUniqueId());
    }
}
