package me.pets.commands;

import me.pets.core.PetsPlugin;
import me.pets.pets.PetType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.stream.Collectors;

public class PetsCommand implements CommandExecutor {
    private final PetsPlugin plugin;

    public PetsCommand(PetsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            var data = plugin.getPetManager().getData(player);
            String owned = data.getOwnedPets().stream().map(PetType::getDisplayName).collect(Collectors.joining(", "));
            player.sendMessage(plugin.prefix() + "Equipped: " + (data.getEquippedPet() == null ? "None" : data.getEquippedPet().getDisplayName()));
            player.sendMessage(plugin.prefix() + "Owned: " + (owned.isEmpty() ? "None" : owned));
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            PetType type = parsePet(args[1]);
            if (type == null) {
                player.sendMessage(plugin.prefix() + "Unknown pet name.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                player.sendMessage(plugin.prefix() + "Target must be online.");
                return true;
            }
            if (!plugin.getPetManager().getData(player).ownsPet(type)) {
                player.sendMessage(plugin.prefix() + "You do not own that pet.");
                return true;
            }
            plugin.getPetManager().removePet(player, type);
            plugin.getPetManager().addPet(target, type);
            player.sendMessage(plugin.prefix() + "Gave " + type.getDisplayName() + " to " + target.getName() + ".");
            target.sendMessage(plugin.prefix() + player.getName() + " traded you a " + type.getDisplayName() + " pet.");
            return true;
        }

        player.sendMessage(plugin.prefix() + "Usage: /pets or /pets give <pet> <player>");
        return true;
    }

    private PetType parsePet(String raw) {
        String normalized = raw.toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        try {
            return PetType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
