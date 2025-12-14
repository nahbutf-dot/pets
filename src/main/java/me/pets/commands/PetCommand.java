package me.pets.commands;

import me.pets.core.PetsPlugin;
import me.pets.pets.PetType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class PetCommand implements CommandExecutor {
    private final PetsPlugin plugin;

    public PetCommand(PetsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.prefix() + "Usage: /pet 1|2 or /pet get <player> <pet>");
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("get")) {
            if (!sender.hasPermission("pets.admin")) {
                sender.sendMessage(plugin.prefix() + "No permission.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            PetType type = parsePet(args[2]);
            if (target == null || type == null) {
                sender.sendMessage(plugin.prefix() + "Invalid target or pet.");
                return true;
            }
            plugin.getPetManager().addPet(target, type);
            sender.sendMessage(plugin.prefix() + "Granted " + type.getDisplayName() + " to " + target.getName());
            target.sendMessage(plugin.prefix() + "You received a " + type.getDisplayName() + " pet!");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use abilities.");
            return true;
        }

        if (args[0].equalsIgnoreCase("1")) {
            plugin.getAbilityManager().useAbility(player, 1);
            return true;
        }
        if (args[0].equalsIgnoreCase("2")) {
            plugin.getAbilityManager().useAbility(player, 2);
            return true;
        }

        // allow players to equip directly via /pet <name>
        PetType type = parsePet(args[0]);
        if (type != null) {
            plugin.getPetManager().equipPet(player, type);
            return true;
        }

        sender.sendMessage(plugin.prefix() + "Unknown subcommand.");
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
