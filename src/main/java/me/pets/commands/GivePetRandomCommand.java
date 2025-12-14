package me.pets.commands;

import me.pets.core.PetsPlugin;
import me.pets.pets.PetType;
import me.pets.pets.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GivePetRandomCommand implements CommandExecutor {
    private final PetsPlugin plugin;
    private final Random random = new Random();

    public GivePetRandomCommand(PetsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pets.admin")) {
            sender.sendMessage(plugin.prefix() + "No permission.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(plugin.prefix() + "Usage: /givepetrandom <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.prefix() + "Target must be online.");
            return true;
        }

        PetType rolled = rollPet();
        plugin.getPetManager().addPet(target, rolled);
        sender.sendMessage(plugin.prefix() + "Gave random pet " + rolled.getDisplayName() + " to " + target.getName());
        target.sendMessage(plugin.prefix() + "You received a random pet: " + rolled.getDisplayName());
        return true;
    }

    private PetType rollPet() {
        double total = Arrays.stream(Rarity.values()).mapToDouble(Rarity::getWeight).sum();
        double pick = random.nextDouble() * total;
        Rarity chosen = Rarity.COMMON;
        for (Rarity rarity : Rarity.values()) {
            pick -= rarity.getWeight();
            if (pick <= 0) {
                chosen = rarity;
                break;
            }
        }
        Rarity finalChosen = chosen;
        List<PetType> options = Arrays.stream(PetType.values())
                .filter(type -> type.getRarity() == finalChosen)
                .collect(Collectors.toList());
        return options.get(random.nextInt(options.size()));
    }
}
