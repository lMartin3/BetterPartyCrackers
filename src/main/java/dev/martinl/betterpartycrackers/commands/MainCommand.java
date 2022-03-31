package dev.martinl.betterpartycrackers.commands;

import dev.martinl.betterpartycrackers.BetterPartyCrackers;
import dev.martinl.betterpartycrackers.configuration.Config;
import dev.martinl.betterpartycrackers.data.PartyCracker;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor, TabCompleter {
    @Getter
    private static final String COMMAND_NAME = "bpc";

    @SuppressWarnings("ConstantConditions")
    public MainCommand() {
        BetterPartyCrackers.getPlugin().getCommand(COMMAND_NAME).setExecutor(this);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0) {
            showHelpMenu(sender);
            return false;
        }
        switch (args[0]) {
            case "help" -> showHelpMenu(sender);
            case "give" -> {
                if (!sender.hasPermission("betterpartycrackers.give")) {
                    sender.sendMessage(Config.getInst().getPrefix().asFormattedString() + Config.getInst().getNoPermissionMessage().asFormattedString());
                    break;
                }
                if (args.length < 3) {
                    sender.sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.RED + "Usage: /bpc give <player> <type> [<amount>]");
                    break;
                }
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if(targetPlayer==null||!targetPlayer.isOnline()) {
                    sender.sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.RED + "Player \"" + args[1] + "\" is not online.");
                    break;
                }
                PartyCracker chosenType = BetterPartyCrackers.getPlugin().getCrackerManager().getPartyCracker(args[2]);
                if(chosenType==null) {
                    sender.sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.RED + "Party Cracker \"" + args[2] + "\" does not exist.");
                    showCrackerList(sender);
                    break;
                }
                int amount = 1;
                if(args.length>=4) {
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (NumberFormatException ignored) {
                        sender.sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.RED + "Invalid amount: " + args[3] + ".");
                        break;
                    }
                }
                if(amount<1) {
                    sender.sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.RED + "Insert a positive amount.");
                    break;
                }
                targetPlayer.getInventory().addItem(chosenType.buildItem(amount));
            }

            default -> sender.sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.RED + "Invalid subcommand, please use /bpc help for help.");
        }
        return false;
    }

    private void showHelpMenu(CommandSender sender) {
        String line = ChatColor.DARK_GRAY +""+ ChatColor.STRIKETHROUGH + "-----------" + ChatColor.RESET;
        sender.sendMessage(line + ChatColor.YELLOW + " Better Party Crackers v"+ BetterPartyCrackers.getPlugin().getDescription().getVersion() + " " + line);
        sender.sendMessage(ChatColor.YELLOW + "/bpc help");
        if(sender.hasPermission("betterpartycrackers.give")) {

        }
        sender.sendMessage(line + line + line + line);
    }

    private void showCrackerList(CommandSender sender) {
        sender.sendMessage(Config.getInst().getPrefix().asFormattedString() + ChatColor.YELLOW + "Party cracker types: " + ChatColor.GRAY +
                BetterPartyCrackers.getPlugin().getCrackerManager().getPartyCrackerTypeList()
                        .stream().map(PartyCracker::getId).collect(Collectors.joining( ChatColor.DARK_GRAY + ", " + ChatColor.GRAY)) + ChatColor.DARK_GRAY + ".");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if(!command.getName().equalsIgnoreCase(COMMAND_NAME)) return null;
        return List.of("asd");
    }
}
